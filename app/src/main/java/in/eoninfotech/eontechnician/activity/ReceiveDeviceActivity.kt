package `in`.eoninfotech.eontechnician.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import `in`.eoninfotech.eontechnician.ReceiveDeviceDetails
import `in`.eoninfotech.eontechnician.Responses.MainResponse
import `in`.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener
import `in`.eoninfotech.eontechnician.controllers.ReceiveDeviceController
import `in`.eoninfotech.eontechnician.databinding.ActivityReceiveDeviceBinding
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import java.text.SimpleDateFormat
import java.util.*


class ReceiveDeviceActivity : AppCompatActivity(), ReceiveDeviceListener {

    var binding: ActivityReceiveDeviceBinding? = null
    var sharedprefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var version: String? = null
    var username: String? = null
    var current_date: String? = null
    lateinit var fromdate : EditText
    lateinit var todate: EditText
    var chk = CheckConnection(this)
    var calen = Calendar.getInstance()
    var receiveDeviceController: ReceiveDeviceController? = null

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
        initView()
        setDate()
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
    }

    private fun initView() {

        binding!!.fromDate.setInputType(InputType.TYPE_NULL)
        binding!!.toDate.setInputType(InputType.TYPE_NULL)

        receiveDeviceController = ReceiveDeviceController()

        binding!!.cardViewReceived.setOnClickListener {
            val intent = Intent(this@ReceiveDeviceActivity, ReceiveDeviceDetails::class.java)
            startActivity(intent)
        }

        binding!!.swipeRefresh.setOnRefreshListener {
            if (chk.isConnected) {
                receivedDeviceData
            } else {
                chk.showConnectionErrorDialog()
            }
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
                },  // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day
            )
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show()
        }

        binding!!.toDate.setOnClickListener{

            val datePickerDialog1 = DatePickerDialog( // on below line we are passing context.
                this@ReceiveDeviceActivity,
                { view, year, monthOfYear, dayOfMonth -> // on below line we are setting date to our edit text.
                    binding!!.toDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },  // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day
            )
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog1.show()
        }
        if (chk.isConnected) {
            receivedDeviceData
        } else {
            chk.showConnectionErrorDialog()
        }
    }

    private val receivedDeviceData: Unit
        private get() {
            binding!!.swipeRefresh.isRefreshing
            binding!!.swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
            //receiveDeviceController?.requestReceiveDevice(this)
        }

    override fun receiveDeiceResponse(response: MainResponse) {}
}