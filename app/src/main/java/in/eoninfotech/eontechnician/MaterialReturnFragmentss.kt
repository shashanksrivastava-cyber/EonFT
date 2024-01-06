package `in`.eoninfotech.eontechnician

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import `in`.eoninfotech.eontechnician.Responses.DeviceItems
import `in`.eoninfotech.eontechnician.Responses.DeviceList
import `in`.eoninfotech.eontechnician.controllers.ReceiveDeviceController
import `in`.eoninfotech.eontechnician.databinding.ActivityReturnDeviceBinding
import `in`.eoninfotech.eontechnician.helper.CheckConnection

class MaterialReturnFragmentss : Fragment() {

    var binding :  ActivityReturnDeviceBinding?= null
    var sharedprefs: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var version: String? = null
    var username: String? = null
    var techid: String? = null
    var chk = CheckConnection(activity)
    var receiveDeviceController: ReceiveDeviceController? = null
    var list_change_values = ArrayList<DeviceList>()
    var lr = ArrayList<DeviceItems>()
    var value_name = java.util.ArrayList<String>()
    var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReturnDeviceBinding.inflate(
            layoutInflater
        )
        // getting our root layout in our view.
        val view: View = binding!!.root
        // below line is to set
        // Content view for our layout.
        activity?.setContentView(view)
        var  sharedprefs = activity?.getSharedPreferences("login_user_pass", AppCompatActivity.MODE_PRIVATE)
        var editor = sharedprefs?.edit()
        username = sharedprefs?.getString("s_uuser", "")
        version = sharedprefs?.getString("version", "")
        techid = sharedprefs?.getString("s_user_id", "")
        initView()
    }

    private fun initView() {

    }
}