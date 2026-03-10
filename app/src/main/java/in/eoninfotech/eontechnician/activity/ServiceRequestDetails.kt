package `in`.eoninfotech.eontechnician.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import `in`.eoninfotech.eontechnician.AppPreferences
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.databinding.ActivityServiceRequestBinding
import `in`.eoninfotech.eontechnician.databinding.ServiceRequestDetailsBinding
import `in`.eoninfotech.eontechnician.di.SharedPreferenceManager
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.storage.LocationPrefs
import `in`.eoninfotech.eontechnician.viewModel.ViewModelServiceRequestDetails
import jakarta.inject.Inject
import retrofit2.adapter.rxjava2.Result.response
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ServiceRequestDetails : AppCompatActivity() {
    @Inject
    lateinit var prefManager: SharedPreferenceManager
    @Inject
    lateinit var appPrefs: AppPreferences
    @Inject
    lateinit var locationPrefs: LocationPrefs
    @Inject
    lateinit var sharedPref: SharedPreferenceManager
    @Inject
    lateinit var telephonyManager: TelephonyManager
    @Inject
    lateinit var chk: CheckConnection

    lateinit var   binding: ServiceRequestDetailsBinding
    private var name: String? = ""
    private var req_id: String? = ""
    var isExpanded = false

    lateinit var viewModelServiceRequestDetails: ViewModelServiceRequestDetails


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ServiceRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        name = intent.getStringExtra("activity_type")
        req_id = intent.getStringExtra("request_id")


        initViewModel()
        observeServiceRequests()
        fatchServiceRequestDetails()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Service Request Details"
            // If you want a more premium look, set elevation to 0
            elevation = 0f
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            }
        )

        binding.layoutVehicleHeader.setOnClickListener {

            isExpanded = !isExpanded

            if (isExpanded) {

                binding.layoutVehicleContent.visibility = View.VISIBLE
                binding.imgExpand.animate().rotation(180f).setDuration(200)

            } else {

                binding.layoutVehicleContent.visibility = View.GONE
                binding.imgExpand.animate().rotation(0f).setDuration(200)
            }
        }

    }

    private fun observeServiceRequests() {
        viewModelServiceRequestDetails.serviceDetails.observe(this){ responses ->

            responses?.let {

                binding.tvRequestNo.text =    "Req No.: ${it.get(0).req_no}"
                binding.tvDate.text = it.get(0).request_date

                binding.tvStatus.text = it.get(0).status

                val status = it.get(0).status ?: ""

                when (status.lowercase()) {

                    "completed" -> {

                        binding.tvStatus.setChipBackgroundColor(
                            ColorStateList.valueOf(Color.parseColor("#E8F5E9"))
                        )

                        binding.tvStatus.setTextColor(Color.parseColor("#2E7D32"))

                        binding.tvStatus.setChipIconResource(android.R.drawable.checkbox_on_background)
                    }

                    "pending" -> {

                        binding.tvStatus.setChipBackgroundColor(
                            ColorStateList.valueOf(Color.parseColor("#FFEBEE"))
                        )

                        binding.tvStatus.setTextColor(Color.parseColor("#C62828"))

                        binding.tvStatus.setChipIconResource(android.R.drawable.ic_menu_close_clear_cancel)
                    }

                }

                binding.tvMainCustomer.text = "Main Customer: ${it.get(0).main_customer}"
                binding.tvSubCustomer.text = "Region: ${it.get(0).sub_customer}"
                binding.tvLocation.text = "Location: ${it.get(0).location}"

                binding.tvPlantIncharge.text = "Incharge: ${it.get(0).pl_name}"
                binding.tvContact.text = "Contact: ${it.get(0).pl_contact}"

                binding.tvVehicleDate.text = "Vehicle Avilable Date: ${formatVehicleDate(it.get(0).veh_avail_date)}"

            }

        }
    }

    private fun formatVehicleDate(vehAvailDate: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault())
            val output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val parsed = input.parse(vehAvailDate ?: "")
            output.format(parsed!!)
        } catch (e: Exception) {
            vehAvailDate ?: ""
        }
    }


    private fun fatchServiceRequestDetails() {
        viewModelServiceRequestDetails.getServiceRequestDetails(
            reqNo = req_id,
            activityType = name,
            username = sharedPref.getUsername(),
            zone = sharedPref.getZone()
        )
    }


    private fun initViewModel() {
        viewModelServiceRequestDetails = ViewModelProvider(this).get(ViewModelServiceRequestDetails:: class.java)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}