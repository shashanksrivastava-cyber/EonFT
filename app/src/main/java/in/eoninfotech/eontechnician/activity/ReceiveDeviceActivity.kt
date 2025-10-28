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

//    private var receiveDeviceAdapter: ReceiveDeviceAdapter? = null
//    var binding: ActivityReceiveDeviceBinding? = null
//    var sharedprefs: SharedPreferences? = null
//    var editor: SharedPreferences.Editor? = null
//    var version: String? = null
//    var username: String? = null
//    var techid: String? = null
//    var status: String? = "A"
//    var current_date: String? = null
//    var s_from_date : String? =null
//    var s_to_date : String? =null
//    lateinit var fromdate : EditText
//    lateinit var todate: EditText
//    lateinit var spinner : MySearchableSpinner
//    var chk = CheckConnection(this)
//    var calen = Calendar.getInstance()
//    var receiveDeviceController: ReceiveDeviceController? = null
//    var recyclerView: RecyclerView? = null
//    private var progressDialog: AlertDialog? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityReceiveDeviceBinding.inflate(
//            layoutInflater
//        )
//        // getting our root layout in our view.
//        val view: View = binding!!.root
//        // below line is to set
//        // Content view for our layout.
//        setContentView(view)
//        var  sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE)
//        var editor = sharedprefs.edit()
//        username = sharedprefs.getString("s_uuser", "")
//        version = sharedprefs.getString("version", "")
//        techid = sharedprefs.getString("s_user_id", "")
//
//        var actionBar = getSupportActionBar()
//
//        // showing the back button in action bar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setTitle("Receive Devices")
//        }
//        setDate()
//        initView()
//    }
//
//    override fun onSupportNavigateUp(): kotlin.Boolean {
//        onBackPressed()
//        return true
//    }
//
//
//    private fun setDate() {
//        val dateFormat = SimpleDateFormat("HH:mm")
//        var year = calen.get(Calendar.YEAR)
//        var month = calen.get(Calendar.MONTH)
//        var day = calen.get(Calendar.DAY_OF_MONTH)
//        month = month + 1
//        if (month + 1 < 10) {
//            current_date = day.toString() + "-0" + month + "-" + year
//        } else {
//            current_date = day.toString() + "-" + month + "-" + year
//        }
//        binding!!.fromDate.setText(current_date)
//        binding!!.toDate.setText(current_date)
//
//        s_from_date = K.getDateFormatWithMonthNameHyphenYear(current_date)
//        s_to_date = K.getDateFormatWithMonthNameHyphenYear(current_date)
//
//    }
//
//    private fun initView() {
//
//        val layoutManager = LinearLayoutManager(applicationContext)
//        binding!!.recyclerView.layoutManager = layoutManager
//        binding!!.recyclerView.setAdapter(receiveDeviceAdapter)
//
//        binding!!.fromDate.setInputType(InputType.TYPE_NULL)
//        binding!!.toDate.setInputType(InputType.TYPE_NULL)
//
//        receiveDeviceController = ReceiveDeviceController()
//        progressDialog = SpotsDialog(this@ReceiveDeviceActivity, R.style.CustomInstallation)
//
//        binding!!.swipeRefresh.setOnRefreshListener {
//            getReceiveData()
//        }
//
//        val c  = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//
//        binding!!.fromDate.setOnClickListener{
//
//            val datePickerDialog = DatePickerDialog( // on below line we are passing context.
//                this@ReceiveDeviceActivity,
//                { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
//                    binding!!.fromDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
//                    var selected_from_date = binding!!.fromDate.text.toString()
//                    val cmp = selected_from_date.compareTo(s_to_date.toString())
//                    if(cmp!=0){
//                        s_from_date = K.getDateFormatWithMonthNameHyphenYear(selected_from_date)
//                        getReceiveData()
//                    }else {
//                        Toast.makeText(
//                            this,
//                            "To date must be greater than from date",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                },  // on below line we are passing year,
//                // month and day for selected date in our date picker.
//                year, month, day
//            )
//            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//            // at last we are calling show to
//            // display our date picker dialog.
//            datePickerDialog.show()
//        }
//
//        binding!!.toDate.setOnClickListener{
//
//            val datePickerDialog1 = DatePickerDialog( // on below line we are passing context.
//                this@ReceiveDeviceActivity,
//                { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
//                    binding!!.toDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
//                    var selected_to_date = binding!!.toDate.text.toString()
//                    val cmp = selected_to_date.compareTo(s_from_date.toString())
//                    if(cmp>0){
//                       s_to_date = K.getDateFormatWithMonthNameHyphenYear(selected_to_date)
//                        getReceiveData()
//                    }else {
//                        Toast.makeText(
//                            this,
//                            "From date must be greater than to date",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                },  // on below line we are passing year,
//                // month and day for selected date in our date picker.
//                year, month, day
//            )
//            datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
//            // at last we are calling show to
//            // display our date picker dialog.
//            datePickerDialog1.show()
//        }
//
//        binding!!.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                status = binding!!.statusSpinner.toString()
//                val text: String = parent.getItemAtPosition(position).toString()
//                if (text.equals("Received", ignoreCase = true)) {
//                    status="R"
//                    getReceiveData()
//                } else if (text.equals("Not Received", ignoreCase = true)) {
//                    status="NR"
//                    getReceiveData()
//                } else {
//                    status="A"
//                    getReceiveData()
//                }
//            }
//            override fun onNothingSelected(parent: AdapterView<*>) {
//
//            }
//        }
//    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                return true
//            }
//        }
//        return super.onContextItemSelected(item)
//    }
//
//    private fun getReceiveData() {
//        if (chk.isConnected) {
//            progressDialog!!.show()
//            receivedDeviceData
//        } else {
//            chk.showConnectionErrorDialog()
//        }
//    }
//
//    private val receivedDeviceData: Unit
//        private get() {
//            receiveDeviceController?.requestReceiveDevice(s_from_date,s_to_date,status,techid,this)
//        }
//
//    override fun receiveDeviceResponse(response: MainResponse) {
//        progressDialog!!.hide()
//        var lr: ArrayList<DispatchDeviceList?>? = ArrayList()
//        lr = response.dispatched_device_list
//        binding!!.swipeRefresh.setRefreshing(false)
//        if(response.type==1){
//            receiveDeviceAdapter = ReceiveDeviceAdapter(this@ReceiveDeviceActivity,lr)
//            binding!!.recyclerView.setAdapter(receiveDeviceAdapter)
//            receiveDeviceAdapter!!.notifyDataSetChanged()
//            binding!!.recyclerView.setVisibility(View.VISIBLE)
//            binding!!.txtContentUnavailable.setVisibility(View.GONE)
//        }else {
//            binding!!.recyclerView.setVisibility(View.GONE)
//            binding!!.txtContentUnavailable.setVisibility(View.VISIBLE)
//        }
//    }
//
//    override fun receiveDispatchMaterial(response: MainResponse?) {
//
//    }
//
//    override fun returnDeviceresponse(response: MainResponse?) {
//    }
//
//    override fun dispatchFromTechResponse(response: MainResponse?) {
//
//    }
//
//    override fun onResume() {
//        getReceiveData()
//        super.onResume()
//    }

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