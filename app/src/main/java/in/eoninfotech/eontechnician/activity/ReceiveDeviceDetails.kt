package `in`.eoninfotech.eontechnician.activity

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dmax.dialog.SpotsDialog
import `in`.eoninfotech.eontechnician.OtherMaterialAdapter
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.responses.DeviceItems
import `in`.eoninfotech.eontechnician.responses.DeviceList
import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener
import `in`.eoninfotech.eontechnician.controllers.ReceiveDeviceController
import `in`.eoninfotech.eontechnician.databinding.ActivityReceiveDeviceDetailsBinding
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import androidx.lifecycle.lifecycleScope

class ReceiveDeviceDetails : AppCompatActivity(), ReceiveDeviceListener {

    private var otherMaterialAdapter: OtherMaterialAdapter? = null
    var binding: ActivityReceiveDeviceDetailsBinding? = null
    var sharedprefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var version: String? = null
    var username: String? = null
    var dispatch_id: String? = null
    var transit_through: String? = null
    var status: String? = null
    var itemsCollected: String? = ""
    var techid: String? = null
    var main_id: String? = null
    var key: String = ""
    var value: String = ""
    var type: String? = null
    var chk = CheckConnection(this)
    var receiveDeviceController: ReceiveDeviceController? = null
    var list_change_values = ArrayList<DeviceList>()
    var lr = ArrayList<DeviceItems>()
    var accessories = ArrayList<String>()
    var value_name = java.util.ArrayList<String>()
    var adapter: ArrayAdapter<String>? = null
    var recyclerView: RecyclerView? = null
    private var progressDialog: AlertDialog? = null
    var items = emptyMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveDeviceDetailsBinding.inflate(
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
            actionBar.setTitle("Received Device Details")
        }
        initView()
    }

    override fun onSupportNavigateUp(): kotlin.Boolean {
        onBackPressed()
        return true
    }

    private fun initView() {

        dispatch_id = intent.getStringExtra("dispatch_id").toString()
        transit_through = intent.getStringExtra("transit_through").toString()
        status = intent.getStringExtra("status").toString()
        main_id = intent.getStringExtra("id").toString()
        type = intent.getStringExtra("type").toString()
        receiveDeviceController = ReceiveDeviceController()
        val layoutManager = LinearLayoutManager(applicationContext)
        binding!!.recyclerView.layoutManager = layoutManager
        binding!!.recyclerView.setAdapter(otherMaterialAdapter)
        progressDialog = SpotsDialog(this@ReceiveDeviceDetails, R.style.CustomInstallation)

        if(status.equals("Received")){
            binding!!.remarksReceive.setVisibility(View.GONE)
            binding!!.btnAcceptReceive.setVisibility(View.GONE)
            binding!!.actualQty.setVisibility(View.GONE)
        }else {
            binding!!.btnAcceptReceive.setVisibility(View.VISIBLE)
            binding!!.actualQty.setVisibility(View.VISIBLE)
        }

        getData();

        binding!!.btnAcceptReceive.setOnClickListener {

            itemsCollected = ""
            accessories.clear()

            // ✅ Safe null check — ListView may have no adapter when device_list is empty
            val checked: SparseBooleanArray? = binding!!.deviceDetailListReceive.checkedItemPositions

            if (checked != null) {
                for (i in 0 until checked.size()) {
                    val position = checked.keyAt(i)
                    if (checked.valueAt(i) && position < list_change_values.size) {
                        val device = list_change_values[position]
                        if (!device.status.equals("At Technician", true)) {
                            itemsCollected += device.pcb_id + ":"
                        }
                    }
                }
            }

            for (i in 0 until lr.size) {
                accessories.add(
                    "${lr[i].id}:" + binding!!.recyclerView
                        .findViewHolderForAdapterPosition(i)
                        ?.itemView
                        ?.findViewById<TextView>(R.id.addText)
                        ?.text.toString()
                )
            }

            if (list_change_values.size > 0) {
                // Device list exists — must select at least one device
                if (itemsCollected.equals("")) {
                    Toast.makeText(applicationContext, "Please select at least one device!!", Toast.LENGTH_LONG).show()
                } else {
                    if (chk.isConnected) submitData() else chk.showConnectionErrorDialog()
                }
            } else {
                // ✅ No device list — only accessories, skip device selection check
                if (chk.isConnected) submitData() else chk.showConnectionErrorDialog()
            }
        }
    }

    private fun submitData() {
        confirmationDialog()
    }

    private fun confirmationDialog() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to receive device ?")
            .setPositiveButton("Yes") { dialog, which ->
                progressDialog!!.show()
                receiveDeviceController?.receiveDispatchedMaterial(dispatch_id,main_id,techid,itemsCollected,accessories.toString(),binding!!.remarksReceive.text.toString(),this)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun getData() {
        if (chk.isConnected) {
            progressDialog!!.show()
            dispatchedData
        } else {
            chk.showConnectionErrorDialog()
        }
    }

    private val dispatchedData: Unit
        private get() {
            receiveDeviceController?.requestDispatchedDevice(dispatch_id,transit_through,techid,main_id,type,this)
        }

    override fun receiveDeviceResponse(response: MainResponse?) {
        progressDialog!!.dismiss()
        if (response!!.type == 1) {
            binding!!.incentiveAmt.setText("Total Device - " + response.total_received_count)
            try {
                lr = response.dispatched_device_details[0].device_items
                list_change_values = response.dispatched_device_details[0].device_list

                // ✅ Always handle device_items (accessories) regardless of device_list
                if (lr.size > 0) {
                    otherMaterialAdapter = OtherMaterialAdapter(this@ReceiveDeviceDetails, lr, status)
                    binding!!.recyclerView.setAdapter(otherMaterialAdapter)
                    otherMaterialAdapter!!.notifyDataSetChanged()
                    binding!!.recyclerView.setVisibility(View.VISIBLE)
                    binding!!.txtContentUnavailable.setVisibility(View.GONE)
                } else {
                    binding!!.recyclerView.setVisibility(View.GONE)
                    binding!!.txtContentUnavailable.setVisibility(View.VISIBLE)
                }

                // ✅ Handle device_list separately
                if (list_change_values.size > 0) {
                    value_name.clear()

                    for (i in list_change_values.indices) {
                        val k = (i + 1).toString()
                        value_name.add("$k. ${list_change_values[i].pcb_sr_no}")
                    }

                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        if (list_change_values.size > 5)
                            80 * list_change_values.size + 1
                        else
                            80 * list_change_values.size
                    )
                    binding!!.deviceDetailListReceive.setLayoutParams(layoutParams)

                    adapter = object : ArrayAdapter<String>(
                        this@ReceiveDeviceDetails,
                        R.layout.simple_custom_list_item,
                        value_name
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val device = list_change_values[position]
                            val inflater = LayoutInflater.from(context)
                            val view = if (device.status.equals("In-Transit", true)) {
                                inflater.inflate(R.layout.simple_custom_list_item, parent, false)
                            } else {
                                inflater.inflate(R.layout.custom_list_item_disable, parent, false)
                            }
                            view.findViewById<TextView>(R.id.text1).text = value_name[position]
                            return view
                        }
                    }

                    binding!!.deviceDetailListReceive.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
                    binding!!.deviceDetailListReceive.setAdapter(adapter)
                    binding!!.deviceDetailListReceive.setVisibility(View.VISIBLE) // ✅ show list

                } else {
                    // ✅ Hide the device list view when empty
                    binding!!.deviceDetailListReceive.setVisibility(View.GONE)
                    binding!!.txtContentUnavailableSr.setVisibility(View.VISIBLE)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            binding!!.recyclerView.setVisibility(View.GONE)
            binding!!.txtContentUnavailable.setVisibility(View.VISIBLE)
            binding!!.txtContentUnavailableSr.setVisibility(View.VISIBLE)
        }
    }

    override fun receiveDispatchMaterial(response: MainResponse?) {
        progressDialog!!.hide()
        if(response!!.type==1){
            val toast = Toast.makeText(applicationContext, ""+response.msg, Toast.LENGTH_LONG)
            toast.show()
            this.finish()
        }else {
             val toast = Toast.makeText(applicationContext, ""+response.msg, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun returnDeviceresponse(response: MainResponse?) {

    }

    override fun dispatchFromTechResponse(response: MainResponse?) {

    }

}