package `in`.eoninfotech.eontechnician.activity

import android.Manifest
import android.annotation.SuppressLint
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import dmax.dialog.SpotsDialog
import `in`.eoninfotech.eontechnician.AppPreferences
import `in`.eoninfotech.eontechnician.BuildConfig
import `in`.eoninfotech.eontechnician.MainActivity
import `in`.eoninfotech.eontechnician.MainActivityNew
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.databinding.LoginactivityBinding
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.responses.LoginDetail
import `in`.eoninfotech.eontechnician.responses.LoginResponse
import `in`.eoninfotech.eontechnician.viewModel.ViewModelLogin
import `in`.eoninfotech.eontechnician.storage.LocationPrefs
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.net.NetworkInterface

//class LoginActivityNew : AppCompatActivity() {
//
//    private lateinit var binding: LoginactivityBinding
//    private lateinit var sharedPrefs: SharedPreferences
//    private lateinit var editor: SharedPreferences.Editor
//    private lateinit var telephonyManager: TelephonyManager
//    private lateinit var viewModelLogin: ViewModelLogin
//    private lateinit var appPrefs: AppPreferences
//    private lateinit var progressDialog: SpotsDialog
//    private lateinit var locationPrefs: LocationPrefs
//
//    private val chk by lazy { CheckConnection(this) }
//    private val permissionList = arrayOf(
//        Manifest.permission.INTERNET,
//        Manifest.permission.READ_PHONE_STATE,
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.CAMERA
//    )
//
//    private var token = ""
//    private var imsiSIM1 = ""
//    private var userId = ""
//    private var pUsr = ""
//    private var pPass = ""
//
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = LoginactivityBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        init()
//
//        requestPermissionsIfNeeded()
//
//        setStatusBarColor()
//
//        binding.appVersion.text = "Version ${getVersion()}"
//
//        if (appPrefs.getproviderInfo() != "true") addRecord()
//
//        binding.login.setOnClickListener {
//            onLoginClicked()
//        }
//
//        binding.phnnum.setOnClickListener {
//            makePhoneCall()
//        }
//
//        binding.emadd.setOnClickListener {
//            sendSupportEmail()
//        }
//    }
//
//    private fun init() {
//        sharedPrefs = getSharedPreferences("login_user_pass", MODE_PRIVATE)
//        editor = sharedPrefs.edit()
//        appPrefs = AppPreferences(applicationContext)
//        progressDialog = SpotsDialog(this, R.style.Custom)
//        locationPrefs = LocationPrefs(this)
//        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//
//        binding.username.setText(sharedPrefs.getString("usname", ""))
//        binding.passwordd.setText(sharedPrefs.getString("pass", ""))
//
//        viewModelLogin = ViewModelProvider(this)[ViewModelLogin::class.java]
//        getFCMToken()
//    }
//
//    private fun requestPermissionsIfNeeded() {
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}.launch(permissionList)
//    }
//
//    private fun setStatusBarColor() {
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.eonBlue)
//    }
//
//    private fun getFCMToken() {
//        // Replace with coroutine Firebase Messaging token fetch if needed
//        lifecycleScope.launch {
//            // token = fetchFirebaseToken()
//        }
//    }
//
//    private fun onLoginClicked() {
//        pUsr = binding.username.text.toString().trim()
//        pPass = binding.passwordd.text.toString().trim()
//
//        if (pUsr.isEmpty()) {
//            binding.user.error = "Please Enter Username"
//            return
//        } else {
//            binding.user.error = null
//        }
//
//        if (pPass.isEmpty()) {
//            binding.pass.error = "Please Enter Password"
//            return
//        } else {
//            binding.pass.error = null
//        }
//
//        editor.putString("usname", pUsr)
//        editor.putString("pass", pPass).apply()
//
//        if (chk.isConnected()) {
//            getLogin()
//        } else {
//            chk.showConnectionErrorDialog()
//        }
//    }
//
//
//    private fun getLogin() {
//        progressDialog.show()
//
//        viewModelLogin.login(
//            pUsr,
//            pPass,
//            imsiSIM1,
//            BuildConfig.VERSION_NAME,
//            token,
//            object : ViewModelLogin.LoginCallback {
//                override fun onSuccess(response: LoginResponse) {
//                    progressDialog.dismiss()
//                    if (response.type == 1 && response.loginDetails.isNotEmpty()) {
//                        val loginDetail = response.loginDetails.first()
//                        saveLoginDetails(loginDetail)
//                        appPrefs.setLoggedIn(true)
//                        val intent = Intent(this@LoginActivityNew, MainActivity::class.java)
//                        //val intent = Intent(this@LoginActivityNew, MainActivityNew::class.java)
//                        intent.putExtra("intent", "")
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        Toast.makeText(
//                            this@LoginActivityNew,
//                            "Username/Password Incorrect",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                override fun onError(throwable: Throwable) {
//                    progressDialog.dismiss()
//                    Toast.makeText(
//                        this@LoginActivityNew,
//                        "Login failed: ${throwable.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Log.e("LoginActivity", "Login error", throwable)
//                }
//            }
//        )
//    }
//
//    private fun saveLoginDetails(detail: LoginDetail) {
//        with(sharedPrefs.edit()) {
//            putString("s_user_id", detail.usr_id)
//            putString("s_uuser", detail.usrname)
//            putString("location", detail.location)
//            putString("usrtype", detail.usrtype)
//            putString("zone", detail.zone)
//            putString("version", detail.verno)
//            putString("contact", detail.contact)
//            putString("dis_user", detail.displayname)
//            putString("image", detail.image)
//            putString("imei1", imsiSIM1)
//            putString("track_status", detail.track_status)
//            putString("track_interval", detail.track_interval)
//            putString("bill_amt_limit", detail.bill_amt_limit)
//            apply() // single apply instead of multiple
//        }
//    }
//
//    private fun getMacAddr(): String {
//        return try {
//            val interfaces = NetworkInterface.getNetworkInterfaces().toList()
//            for (nif in interfaces) {
//                if (nif.name.equals("wlan0", ignoreCase = true)) {
//                    val macBytes = nif.hardwareAddress ?: return ""
//                    val mac = macBytes.joinToString(":") { "%02X".format(it) }
//                    editor.putString("MacAddress", mac).apply()
//                    return mac
//                }
//            }
//            ""
//        } catch (ex: Exception) {
//        ""
//        }
//    }
//
//    private fun makePhoneCall() {
//        val intent = Intent(Intent.ACTION_CALL).apply {
//            data = Uri.parse("tel:01725044700")
//        }
//        try {
//            startActivity(intent)
//        } catch (e: Exception) {
//            Toast.makeText(this, "Enable calling permissions", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun sendSupportEmail() {
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            putExtra(Intent.EXTRA_EMAIL, arrayOf("support@eoninfotech.com"))
//            type = "text/html"
//            setPackage("com.google.android.gm")
//        }
//        startActivity(Intent.createChooser(intent, "Send mail"))
//    }
//
//    private fun getVersion(): String {
//        return try {
//            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
//            packageInfo.versionName ?: "Unknown"
//        } catch (e: PackageManager.NameNotFoundException) {
//            "Unknown"
//        }
//    }
//
//    private fun addRecord() {
//        appPrefs.saveProviderInfo("true")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        progressDialog.dismiss()
//    }
//
//    override fun onBackPressed() {
//        Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show()
//        super.onBackPressed()
//    }
//}

@AndroidEntryPoint
class LoginActivityNew : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    @Inject lateinit var appPrefs: AppPreferences
    @Inject lateinit var locationPrefs: LocationPrefs
    @Inject lateinit var telephonyManager: TelephonyManager
    @Inject lateinit var chk: CheckConnection

    private lateinit var binding: LoginactivityBinding
    private lateinit var progressDialog: SpotsDialog
    private lateinit var editor: SharedPreferences.Editor

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
    private var userId = ""
    private var pUsr = ""
    private var pPass = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        requestPermissionsIfNeeded()
        setStatusBarColor()

        binding.appVersion.text = "Version ${getVersion()}"

        if (appPrefs.getproviderInfo() != "true") addRecord()

        binding.login.setOnClickListener { onLoginClicked() }
        binding.phnnum.setOnClickListener { makePhoneCall() }
        binding.emadd.setOnClickListener { sendSupportEmail() }
    }

    private fun init() {
        editor = sharedPrefs.edit()
        progressDialog = SpotsDialog(this, R.style.Custom)

        binding.username.setText(sharedPrefs.getString("usname", ""))
        binding.passwordd.setText(sharedPrefs.getString("pass", ""))

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

        editor.putString("usname", pUsr)
        editor.putString("pass", pPass).apply()

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
                        saveLoginDetails(loginDetail)
                        appPrefs.setLoggedIn(true)
                        startActivity(Intent(this@LoginActivityNew, MainActivity::class.java))
                        intent.putExtra("intent", "")
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

    private fun saveLoginDetails(detail: LoginDetail) {
        with(sharedPrefs.edit()) {
            putString("s_user_id", detail.usr_id)
            putString("s_uuser", detail.usrname)
            putString("location", detail.location)
            putString("usrtype", detail.usrtype)
            putString("zone", detail.zone)
            putString("version", detail.verno)
            putString("contact", detail.contact)
            putString("dis_user", detail.displayname)
            putString("image", detail.image)
            putString("imei1", imsiSIM1)
            putString("track_status", detail.track_status)
            putString("track_interval", detail.track_interval)
            putString("bill_amt_limit", detail.bill_amt_limit)
            apply()
        }
    }

    private fun getMacAddr(): String {
        return try {
            val interfaces = NetworkInterface.getNetworkInterfaces().toList()
            for (nif in interfaces) {
                if (nif.name.equals("wlan0", ignoreCase = true)) {
                    val macBytes = nif.hardwareAddress ?: return ""
                    val mac = macBytes.joinToString(":") { "%02X".format(it) }
                    editor.putString("MacAddress", mac).apply()
                    return mac
                }
            }
            ""
        } catch (ex: Exception) {
            ""
        }
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

    override fun onBackPressed() {
        Toast.makeText(this, "Press back to exit", Toast.LENGTH_SHORT).show()
        super.onBackPressed()
    }
}