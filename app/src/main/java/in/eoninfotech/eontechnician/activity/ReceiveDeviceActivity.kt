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
import `in`.eoninfotech.eontechnician.Responses.DispatchDeviceDetails
import `in`.eoninfotech.eontechnician.Responses.DispatchDeviceList
import `in`.eoninfotech.eontechnician.Responses.MainResponse
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
    var binding: ActivityReceiveDeviceBinding? = null
    var sharedprefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var version: String? = null
    var username: String? = null
    var techid: String? = null
    var status: String? = "A"
    var current_date: String? = null
    var s_from_date : String ? =null
    var s_to_date : String ? =null
    lateinit var fromdate : EditText
    lateinit var todate: EditText
    lateinit var spinner : MySearchableSpinner
    var chk = CheckConnection(this)
    var calen = Calendar.getInstance()
    var receiveDeviceController: ReceiveDeviceController? = null
    var recyclerView: RecyclerView? = null
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveDeviceBinding.inflate(
            layoutInflater
        )
        // getting our root layout in our view.
        val view: View = binding!!.root
        // below line is to set
        // Content view for our layout.
        setContentView(view)
        var  sharedprefs = getSharedPreferences("login_user_pass", MODE_PRIVATE)
        var editor = sharedprefs.edit()
        username = sharedprefs.getString("s_uuser", "")
        version = sharedprefs.getString("version", "")
        techid = sharedprefs.getString("s_user_id", "")

        var actionBar = getSupportActionBar()

        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle("Receive Devices")
        }
        setDate()
        initView()
    }

    override fun onSupportNavigateUp(): kotlin.Boolean {
        onBackPressed()
        return true
    }


    private fun setDate() {
        val dateFormat = SimpleDateFormat("HH:mm")
        var year = calen.get(Calendar.YEAR)
        var month = calen.get(Calendar.MONTH)
        var day = calen.get(Calendar.DAY_OF_MONTH)
        month = month + 1
        if (month + 1 < 10) {
            current_date = day.toString() + "-0" + month + "-" + year
        } else {
            current_date = day.toString() + "-" + month + "-" + year
        }
        binding!!.fromDate.setText(current_date)
        binding!!.toDate.setText(current_date)

        s_from_date = K.getDateFormatWithMonthNameHyphenYear(current_date)
        s_to_date = K.getDateFormatWithMonthNameHyphenYear(current_date)

    }

    private fun initView() {

        val layoutManager = LinearLayoutManager(applicationContext)
        binding!!.recyclerView!!.layoutManager = layoutManager
        binding!!.recyclerView!!.setAdapter(receiveDeviceAdapter)

        binding!!.fromDate.setInputType(InputType.TYPE_NULL)
        binding!!.toDate.setInputType(InputType.TYPE_NULL)

        receiveDeviceController = ReceiveDeviceController()
        progressDialog = SpotsDialog(this@ReceiveDeviceActivity, R.style.CustomInstallation)

        binding!!.swipeRefresh.setOnRefreshListener {
            getReceiveData()
        }

        val c  = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding!!.fromDate.setOnClickListener{

            val datePickerDialog = DatePickerDialog( // on below line we are passing context.
                this@ReceiveDeviceActivity,
                { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
                    binding!!.fromDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    var selected_from_date = binding!!.fromDate.text.toString()
                    val cmp = selected_from_date.compareTo(s_to_date.toString())
                    if(cmp!=0){
                        s_from_date = K.getDateFormatWithMonthNameHyphenYear(selected_from_date)
                        getReceiveData()
                    }else {
                        Toast.makeText(
                            this,
                            "To date must be greater than from date",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                },  // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day
            )
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show()
        }

        binding!!.toDate.setOnClickListener{

            val datePickerDialog1 = DatePickerDialog( // on below line we are passing context.
                this@ReceiveDeviceActivity,
                { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
                    binding!!.toDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    var selected_to_date = binding!!.toDate.text.toString()
                    val cmp = selected_to_date.compareTo(s_from_date.toString())
                    if(cmp>0){
                       s_to_date = K.getDateFormatWithMonthNameHyphenYear(selected_to_date)
                        getReceiveData()
                    }else {
                        Toast.makeText(
                            this,
                            "From date must be greater than to date",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },  // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day
            )
            datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog1.show()
        }

        binding!!.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                status = binding!!.statusSpinner.toString()
                val text: String = parent?.getItemAtPosition(position).toString()
                if (text.equals("Received", ignoreCase = true)) {
                    status="R"
                    getReceiveData()
                } else if (text.equals("Not Received", ignoreCase = true)) {
                    status="NR"
                    getReceiveData()
                } else {
                    status="A"
                    getReceiveData()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun getReceiveData() {
        if (chk.isConnected) {
            progressDialog!!.show()
            receivedDeviceData
        } else {
            chk.showConnectionErrorDialog()
        }
    }

    private val receivedDeviceData: Unit
        private get() {
            receiveDeviceController?.requestReceiveDevice(s_from_date,s_to_date,status,techid,this)
        }

    override fun receiveDeviceResponse(response: MainResponse) {
        progressDialog!!.hide()
        var lr: ArrayList<DispatchDeviceList?>? = ArrayList()
        lr = response.dispatched_device_list
        binding!!.swipeRefresh?.setRefreshing(false)
        if(response.type==1){
            receiveDeviceAdapter = ReceiveDeviceAdapter(this@ReceiveDeviceActivity,lr)
            binding!!.recyclerView?.setAdapter(receiveDeviceAdapter)
            receiveDeviceAdapter!!.notifyDataSetChanged()
            binding!!.recyclerView?.setVisibility(View.VISIBLE)
            binding!!.txtContentUnavailable?.setVisibility(View.GONE)
        }else {
            binding!!.recyclerView?.setVisibility(View.GONE)
            binding!!.txtContentUnavailable?.setVisibility(View.VISIBLE)
        }
    }

    override fun receiveDispatchMaterial(response: MainResponse?) {

    }

    override fun returnDeviceresponse(response: MainResponse?) {
    }

    override fun dispatchFromTechResponse(response: MainResponse?) {

    }

    override fun onResume() {
        getReceiveData()
        super.onResume()
    }

}