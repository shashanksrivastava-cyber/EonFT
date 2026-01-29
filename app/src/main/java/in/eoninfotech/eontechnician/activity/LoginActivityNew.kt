package `in`.eoninfotech.eontechnician.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import `in`.eoninfotech.eontechnician.AppPreferences
import `in`.eoninfotech.eontechnician.MainActivity
import `in`.eoninfotech.eontechnician.MainActivityNew
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.databinding.LoginactivityBinding
import `in`.eoninfotech.eontechnician.di.SharedPreferenceManager
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.responses.LoginDetail
import `in`.eoninfotech.eontechnician.responses.LoginResponse
import `in`.eoninfotech.eontechnician.viewModel.ViewModelLogin
import `in`.eoninfotech.eontechnician.storage.LocationPrefs
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.net.NetworkInterface


//----------------------------Login with updated hilt-------------------

@AndroidEntryPoint
class LoginActivityNew : BaseActivity() {

    @Inject lateinit var prefManager: SharedPreferenceManager
    @Inject lateinit var appPrefs: AppPreferences
    @Inject lateinit var locationPrefs: LocationPrefs
    @Inject lateinit var telephonyManager: android.telephony.TelephonyManager
    @Inject lateinit var chk: CheckConnection

    private lateinit var binding: LoginactivityBinding
    private lateinit var progressDialog: SpotsDialog

    private val viewModelLogin: ViewModelLogin by viewModels()

    private val permissionList = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private var token = ""
    private var imsiSIM1 = ""
    private var pUsr = ""
    private var pPass = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        // Initialize Firebase Remote Config
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                // Handle the updated status if needed
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            val appNameFromFirebase =
                                remoteConfig.getString("app_name_text")
                            val secondNameFromFirebase =
                                remoteConfig.getString("second_name_text")


                            val appNameTv: TextView = findViewById(R.id.appName)
                            val secondNameTv: TextView = findViewById(R.id.name)

                            if (appNameFromFirebase.isNotEmpty()) {
                                appNameTv.text = appNameFromFirebase
                                secondNameTv.text = secondNameFromFirebase
                            }

                            Log.d(TAG, "Remote app name: $appNameFromFirebase")

                        } else {
                            Log.e(TAG, "Remote config fetch failed")
                        }
                    }
            }

        init()
        requestPermissionsIfNeeded()
        setStatusBarColor()

        binding.appVersion.text = "Version ${getVersion()}"
        if (appPrefs.getproviderInfo() != "true") addRecord()

        binding.login.setOnClickListener { onLoginClicked() }
        binding.phnnum.setOnClickListener { makePhoneCall() }
        binding.emadd.setOnClickListener { sendSupportEmail() }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            }
        )
    }

    private fun init() {
        progressDialog = SpotsDialog(this, R.style.Custom)

        // Load saved username/password
        binding.username.setText(prefManager.getUsername())
        binding.passwordd.setText(prefManager.getPassword())

        getFCMToken()
    }

    private fun requestPermissionsIfNeeded() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}.launch(permissionList)
    }

    private fun setStatusBarColor() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.eonBlue)
    }

    private fun getFCMToken() {
        lifecycleScope.launch {
            // token = fetchFirebaseToken()
        }
    }

    private fun onLoginClicked() {
        pUsr = binding.username.text.toString().trim()
        pPass = binding.passwordd.text.toString().trim()

        if (pUsr.isEmpty()) {
            binding.user.error = "Please Enter Username"
            return
        } else binding.user.error = null

        if (pPass.isEmpty()) {
            binding.pass.error = "Please Enter Password"
            return
        } else binding.pass.error = null

        // Save login credentials
        prefManager.saveLoginCredentials(pUsr, pPass)

        if (chk.isConnected()) {
            getLogin()
        } else {
            chk.showConnectionErrorDialog()
        }
    }

    private fun getLogin() {
        progressDialog.show()

        viewModelLogin.login(
            pUsr,
            pPass,
            imsiSIM1,
            BuildConfig.VERSION_NAME,
            token,
            object : ViewModelLogin.LoginCallback {
                override fun onSuccess(response: LoginResponse) {
                    progressDialog.dismiss()
                    if (response.type == 1 && response.loginDetails.isNotEmpty()) {
                        val loginDetail = response.loginDetails.first()
                        prefManager.saveLoginDetails(loginDetail, imsiSIM1)
                        appPrefs.setLoggedIn(true)
                        Log.d("PERF_TEST", "Login success: launching MainActivity at " + System.currentTimeMillis());
                        startActivity(Intent(this@LoginActivityNew, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivityNew,
                            "Username/Password Incorrect",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(throwable: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@LoginActivityNew,
                        "Login failed: ${throwable.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginActivity", "Login error", throwable)
                }
            }
        )
    }

    private fun makePhoneCall() {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:01725044700")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Enable calling permissions", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSupportEmail() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@eoninfotech.com"))
            type = "text/html"
            setPackage("com.google.android.gm")
        }
        startActivity(Intent.createChooser(intent, "Send mail"))
    }

    private fun getVersion(): String {
        return try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName ?: "Unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    private fun addRecord() {
        appPrefs.saveProviderInfo("true")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::progressDialog.isInitialized) progressDialog.dismiss()
    }
}