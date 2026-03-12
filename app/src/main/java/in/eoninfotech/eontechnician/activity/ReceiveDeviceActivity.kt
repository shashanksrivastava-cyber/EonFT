package `in`.eoninfotech.eontechnician.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dmax.dialog.SpotsDialog
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.ReceiveDeviceAdapter
import `in`.eoninfotech.eontechnician.responses.DispatchDeviceDetails
import `in`.eoninfotech.eontechnician.responses.DispatchDeviceList
import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener
import `in`.eoninfotech.eontechnician.controllers.ReceiveDeviceController
import `in`.eoninfotech.eontechnician.databinding.ActivityReceiveDeviceBinding
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.helper.K
import `in`.eoninfotech.eontechnician.view.MySearchableSpinner
import java.text.SimpleDateFormat
import java.util.*


class ReceiveDeviceActivity : AppCompatActivity(), ReceiveDeviceListener {

    private var receiveDeviceAdapter: ReceiveDeviceAdapter? = null
    private var binding: ActivityReceiveDeviceBinding? = null
    private var sharedprefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var version: String? = null
    private var username: String? = null
    private var techid: String? = null
    private var status: String? = "A"
    private var current_date: String? = null
    private var s_from_date: String? = null
    private var s_to_date: String? = null
    private lateinit var fromdate: EditText
    private lateinit var todate: EditText
    private lateinit var spinner: MySearchableSpinner
    private var chk = CheckConnection(this)
    private var calen = Calendar.getInstance()
    private var receiveDeviceController: ReceiveDeviceController? = null
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveDeviceBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE)
        editor = sharedprefs!!.edit()
        username = sharedprefs!!.getString("s_uuser", "")
        version = sharedprefs!!.getString("version", "")
        techid = sharedprefs!!.getString("s_user_id", "")

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Receive Devices"
        }

        setDate()
        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setDate() {
        val year = calen.get(Calendar.YEAR)
        var month = calen.get(Calendar.MONTH) + 1
        val day = calen.get(Calendar.DAY_OF_MONTH)
        current_date = "$day-${if (month < 10) "0$month" else "$month"}-$year"

        binding!!.fromDate.setText(current_date)
        binding!!.toDate.setText(current_date)

        s_from_date = K.getDateFormatWithMonthNameHyphenYear(current_date)
        s_to_date = K.getDateFormatWithMonthNameHyphenYear(current_date)
    }

    private fun initView() {
        receiveDeviceAdapter = ReceiveDeviceAdapter(this, arrayListOf())

        binding!!.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReceiveDeviceActivity)
            adapter = receiveDeviceAdapter
        }

        binding!!.fromDate.inputType = InputType.TYPE_NULL
        binding!!.toDate.inputType = InputType.TYPE_NULL

        receiveDeviceController = ReceiveDeviceController()
        progressDialog = SpotsDialog(this, R.style.CustomInstallation)

        binding!!.swipeRefresh.setOnRefreshListener {
            getReceiveData()
        }

        setupDatePickers()
        setupStatusSpinner()
    }

    private fun setupDatePickers() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding!!.fromDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                val selectedDate = "$d-${m + 1}-$y"
                binding!!.fromDate.setText(selectedDate)
                s_from_date = K.getDateFormatWithMonthNameHyphenYear(selectedDate)
                getReceiveData()
            }, year, month, day)

            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding!!.toDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                val selectedDate = "$d-${m + 1}-$y"
                binding!!.toDate.setText(selectedDate)
                s_to_date = K.getDateFormatWithMonthNameHyphenYear(selectedDate)
                getReceiveData()
            }, year, month, day)

            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun setupStatusSpinner() {
        binding!!.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val text = parent.getItemAtPosition(position).toString()
                status = when (text.lowercase()) {
                    "received" -> "R"
                    "not received" -> "NR"
                    else -> "A"
                }
                getReceiveData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getReceiveData() {
        if (chk.isConnected) {
            progressDialog?.show()
            receiveDeviceController?.requestReceiveDevice(s_from_date, s_to_date, status, techid, this)
        } else {
            chk.showConnectionErrorDialog()
        }
    }

    override fun receiveDeviceResponse(response: MainResponse) {
        progressDialog?.dismiss()
        binding!!.swipeRefresh.isRefreshing = false

        val newList = response.dispatched_device_list ?: arrayListOf()

        if (response.type == 1 && newList.isNotEmpty()) {
            receiveDeviceAdapter?.updateList(newList)
            binding!!.recyclerView.visibility = View.VISIBLE
            binding!!.txtContentUnavailable.visibility = View.GONE
        } else {
            binding!!.recyclerView.visibility = View.GONE
            binding!!.txtContentUnavailable.visibility = View.VISIBLE
        }
    }

    override fun receiveDispatchMaterial(response: MainResponse?) {}
    override fun returnDeviceresponse(response: MainResponse?) {}
    override fun dispatchFromTechResponse(response: MainResponse?) {}

    override fun onResume() {
        super.onResume()
        getReceiveData()
    }

}