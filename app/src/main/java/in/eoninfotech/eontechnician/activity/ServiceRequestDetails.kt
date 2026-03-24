//package `in`.eoninfotech.eontechnician.activity
//
//import android.content.Intent
//import android.content.res.ColorStateList
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.telephony.TelephonyManager
//import android.view.Gravity
//import android.view.View
//import android.widget.TableRow
//import android.widget.TextView
//import androidx.activity.OnBackPressedCallback
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import com.google.firebase.FirebaseApp
//import dagger.hilt.android.AndroidEntryPoint
//import `in`.eoninfotech.eontechnician.AppPreferences
//import `in`.eoninfotech.eontechnician.R
//import `in`.eoninfotech.eontechnician.databinding.ActivityServiceRequestBinding
//import `in`.eoninfotech.eontechnician.databinding.ServiceRequestDetailsBinding
//import `in`.eoninfotech.eontechnician.di.SharedPreferenceManager
//import `in`.eoninfotech.eontechnician.helper.CheckConnection
//import `in`.eoninfotech.eontechnician.storage.LocationPrefs
//import `in`.eoninfotech.eontechnician.viewModel.ViewModelServiceRequestDetails
//import jakarta.inject.Inject
//import retrofit2.adapter.rxjava2.Result.response
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//@AndroidEntryPoint
//class ServiceRequestDetails : AppCompatActivity() {
//    @Inject
//    lateinit var prefManager: SharedPreferenceManager
//    @Inject
//    lateinit var appPrefs: AppPreferences
//    @Inject
//    lateinit var locationPrefs: LocationPrefs
//    @Inject
//    lateinit var sharedPref: SharedPreferenceManager
//    @Inject
//    lateinit var telephonyManager: TelephonyManager
//    @Inject
//    lateinit var chk: CheckConnection
//
//    lateinit var   binding: ServiceRequestDetailsBinding
//    private var name: String? = ""
//    private var req_id: String? = ""
//    var isExpanded = false
//
//    lateinit var viewModelServiceRequestDetails: ViewModelServiceRequestDetails
//
//
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        binding = ServiceRequestDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        FirebaseApp.initializeApp(this)
//
//        name = intent.getStringExtra("activity_type")
//        req_id = intent.getStringExtra("request_id")
//
//
//        initViewModel()
//        observeServiceRequests()
//        fatchServiceRequestDetails()
//
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            title = "Service Request Details"
//            // If you want a more premium look, set elevation to 0
//            elevation = 0f
//        }
//
//        onBackPressedDispatcher.addCallback(
//            this,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    finish()
//                }
//            }
//        )
//
//        binding.layoutVehicleHeader.setOnClickListener {
//
//            isExpanded = !isExpanded
//
//            if (isExpanded) {
//
//                binding.layoutVehicleContent.visibility = View.VISIBLE
//                binding.imgExpand.animate().rotation(180f).setDuration(200)
//
//            } else {
//
//                binding.layoutVehicleContent.visibility = View.GONE
//                binding.imgExpand.animate().rotation(0f).setDuration(200)
//            }
//        }
//
//    }
//
//    private fun observeServiceRequests() {
//
//        viewModelServiceRequestDetails.serviceDetails.observe(this) { responses ->
//
//            if (responses.isNullOrEmpty()) return@observe
//
//            val request = responses[0]
//
//            binding.tvRequestNo.text = "Req No.: ${request.req_no}"
//            binding.tvDate.text = request.request_date
//
//            val status = request.status ?: ""
//            binding.tvStatus.text = status
//
//            when (status.lowercase()) {
//
//                "completed" -> {
//                    binding.tvStatus.setChipBackgroundColor(
//                        ColorStateList.valueOf(Color.parseColor("#E8F5E9"))
//                    )
//                    binding.tvStatus.setTextColor(Color.parseColor("#2E7D32"))
//                    binding.tvStatus.setChipIconResource(android.R.drawable.checkbox_on_background)
//                }
//
//                "pending" -> {
//                    binding.tvStatus.setChipBackgroundColor(
//                        ColorStateList.valueOf(Color.parseColor("#FFEBEE"))
//                    )
//                    binding.tvStatus.setTextColor(Color.parseColor("#C62828"))
//                    binding.tvStatus.setChipIconResource(android.R.drawable.ic_menu_close_clear_cancel)
//                }
//            }
//
//            binding.tvMainCustomer.text = "Main Customer: ${request.main_customer}"
//            binding.tvSubCustomer.text = "Region: ${request.sub_customer}"
//            binding.tvLocation.text = "Location: ${request.location}"
//
//            binding.tvPlantIncharge.text = "Incharge: ${request.pl_name}"
//            binding.tvContact.text = "Contact: ${request.pl_contact}"
//
//            binding.tvVehicleDate.text =
//                "Vehicle Available Date: ${formatVehicleDate(request.veh_avail_date)}"
//
//            if (name.equals("I", true)) {
//
//                binding.headerVehType.text = "Veh Type"
//                binding.headerRegNo.text = "Inst Status"
//
//                binding.headerDate.visibility = View.GONE
//                binding.headerDeviceStatus.visibility = View.GONE
//                binding.headerRequestStatus.visibility = View.GONE
//
//            } else {
//
//                binding.headerVehType.text = "Veh Type"
//                binding.headerRegNo.text = "Reg No"
//                binding.headerDate.text = "Dehired Date"
//                binding.headerDeviceStatus.text = "Device Status"
//                binding.headerRequestStatus.text = "Request Status"
//
//                binding.headerDate.visibility = View.VISIBLE
//                binding.headerDeviceStatus.visibility = View.VISIBLE
//                binding.headerRequestStatus.visibility = View.VISIBLE
//            }
//
//            // -------- Populate Vehicle Table --------
//
//            val table = binding.tableVehicle
//
//            if (table.childCount > 1) {
//                table.removeViews(1, table.childCount - 1)
//            }
//
//            if (name.equals("I", true)) {
//
//                request.inst_details.forEach { vehicle ->
//
//                    val row = TableRow(this)
//
//                    row.addView(createCell(vehicle.veh_type))
//                        //row.addView(createCell(vehicle.reg_no))
//                    row.addView(createCell(vehicle.inst_status))
//
//                    table.addView(row)
//                }
//
//            } else if (name.equals("R", true)) {
//
//                request.removal_details.forEach { vehicle ->
//
//                    val row = TableRow(this)
//
//                    row.addView(createCell(vehicle.veh_type))
//                    row.addView(createCell(vehicle.reg_no))
//                    row.addView(createCell(vehicle.dehired_date))
//                    row.addView(createCell(vehicle.device_status))
//                    row.addView(createCell(vehicle.removal_status))
//
//                    table.addView(row)
//                }
//            }
//        }
//    }
//
//    private fun formatVehicleDate(vehAvailDate: String): String {
//        return try {
//            val input = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault())
//            val output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//            val parsed = input.parse(vehAvailDate ?: "")
//            output.format(parsed!!)
//        } catch (e: Exception) {
//            vehAvailDate ?: ""
//        }
//    }
//
//    private fun createCell(text: String?): TextView {
//
//        val tv = TextView(this)
//
//        tv.text = text ?: "-"
//        tv.gravity = Gravity.CENTER
//        tv.setPadding(16, 10, 16, 10)
//
//        return tv
//    }
//
//    private fun fatchServiceRequestDetails() {
//        viewModelServiceRequestDetails.getServiceRequestDetails(
//            reqNo = req_id,
//            activityType = name,
//            username = sharedPref.getUsername(),
//            zone = sharedPref.getZone()
//        )
//    }
//
//    private fun initViewModel() {
//        viewModelServiceRequestDetails = ViewModelProvider(this).get(ViewModelServiceRequestDetails:: class.java)
//    }
//
//
//    override fun onSupportNavigateUp(): Boolean {
//        finish()
//        return true
//    }
//}

package `in`.eoninfotech.eontechnician.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import `in`.eoninfotech.eontechnician.AppPreferences
import `in`.eoninfotech.eontechnician.databinding.ServiceRequestDetailsBinding
import `in`.eoninfotech.eontechnician.di.SharedPreferenceManager
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.storage.LocationPrefs
import `in`.eoninfotech.eontechnician.view.ServiceRequestDetailUiState
import `in`.eoninfotech.eontechnician.viewModel.ViewModelServiceRequestDetails
import jakarta.inject.Inject
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

    lateinit var binding: ServiceRequestDetailsBinding
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
        fetchServiceRequestDetails()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Service Request Details"
            elevation = 0f
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

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
        lifecycleScope.launchWhenStarted {
            viewModelServiceRequestDetails.uiState.collect { state ->
                when (state) {

                    is ServiceRequestDetailUiState.Idle -> {
                        // do nothing
                    }

                    is ServiceRequestDetailUiState.Loading -> {
                        // ✅ Show progress, hide content
                        binding.progressBar.isVisible = true
                        binding.scrollContent.visibility = View.GONE  // hide all cards
                    }

                    is ServiceRequestDetailUiState.Success -> {
                        // ✅ Hide progress, show content
                        binding.progressBar.isVisible = false
                        binding.scrollContent.visibility = View.VISIBLE

                        val responses = state.data
                        if (responses.isEmpty()) return@collect

                        val request = responses[0]

                        binding.tvRequestNo.text = "Req No.: ${request.req_no}"
                        binding.tvDate.text = request.request_date

                        val status = request.status ?: ""
                        binding.tvStatus.text = status

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

                        binding.tvMainCustomer.text = "Main Customer: ${request.main_customer}"
                        binding.tvSubCustomer.text = "Region: ${request.sub_customer}"
                        binding.tvLocation.text = "Location: ${request.location}"
                        binding.tvPlantIncharge.text = "Incharge: ${request.pl_name}"
                        binding.tvContact.text = "Contact: ${request.pl_contact}"
                        binding.btnCall.setOnClickListener {
                            val numberFromApi = request.pl_contact// replace with your API value
                            makePhoneCall(numberFromApi)
                        }
                        binding.tvVehicleDate.text =
                            "Vehicle Available Date: ${formatVehicleDate(request.veh_avail_date)}"

                        if (name.equals("I", true)) {
                            binding.headerVehType.text = "Veh Type"
                            binding.headerRegNo.text = "Inst Status"
                            binding.headerDate.visibility = View.GONE
                            binding.headerDeviceStatus.visibility = View.GONE
                            binding.headerRequestStatus.visibility = View.GONE
                        } else {
                            binding.headerVehType.text = "Veh Type"
                            binding.headerRegNo.text = "Reg No"
                            binding.headerDate.text = "Dehired Date"
                            binding.headerDeviceStatus.text = "Device Status"
                            binding.headerRequestStatus.text = "Request Status"
                            binding.headerDate.visibility = View.VISIBLE
                            binding.headerDeviceStatus.visibility = View.VISIBLE
                            binding.headerRequestStatus.visibility = View.VISIBLE
                        }

                        val table = binding.tableVehicle
                        if (table.childCount > 1) {
                            table.removeViews(1, table.childCount - 1)
                        }

                        if (name.equals("I", true)) {
                            request.inst_details.forEach { vehicle ->
                                val row = TableRow(this@ServiceRequestDetails)
                                row.addView(createCell(vehicle.veh_type))
                                row.addView(createCell(vehicle.inst_status))
                                table.addView(row)
                            }
                        } else if (name.equals("R", true)) {
                            request.removal_details.forEach { vehicle ->
                                val row = TableRow(this@ServiceRequestDetails)
                                row.addView(createCell(vehicle.veh_type))
                                row.addView(createCell(vehicle.reg_no))
                                row.addView(createCell(formatVehicleDate(vehicle.dehired_date)))
                                row.addView(createCell(vehicle.device_status))
                                row.addView(createCell(vehicle.removal_status))
                                table.addView(row)
                            }
                        }
                    }
                    is ServiceRequestDetailUiState.Error -> {
                        // ✅ Hide progress, show actual API error message
                        binding.progressBar.isVisible = false
                        binding.scrollContent.visibility = View.GONE
                        //Toast.makeText(this, state.message, Toast.LENGTH_LONG).sh()
                    }
                }
            }
        }
    }

    private fun makePhoneCall(phoneNumber: String){
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)

    }

    private fun formatVehicleDate(vehAvailDate: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val output = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val parsed = input.parse(vehAvailDate ?: "")
            output.format(parsed!!)
        } catch (e: Exception) {
            vehAvailDate ?: ""
        }
    }

    private fun createCell(text: String?): TextView {
        val tv = TextView(this)
        tv.text = text ?: "-"
        tv.gravity = Gravity.CENTER
        tv.setPadding(16, 10, 16, 10)
        return tv
    }

    private fun fetchServiceRequestDetails() {
        viewModelServiceRequestDetails.getServiceRequestDetails(
            reqNo = req_id,
            activityType = name,
            username = sharedPref.getUsername(),
            zone = sharedPref.getZone()
        )
    }

    private fun initViewModel() {
        viewModelServiceRequestDetails =
            ViewModelProvider(this).get(ViewModelServiceRequestDetails::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}