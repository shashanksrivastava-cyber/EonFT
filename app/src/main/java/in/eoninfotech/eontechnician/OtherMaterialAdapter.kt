package `in`.eoninfotech.eontechnician

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.eoninfotech.eontechnician.Responses.DeviceItems
import `in`.eoninfotech.eontechnician.Responses.DispatchDeviceList
import `in`.eoninfotech.eontechnician.activity.ReceiveDeviceDetails
import `in`.eoninfotech.eontechnician.databinding.DispatchedMaterialListBinding
import `in`.eoninfotech.eontechnician.databinding.OtherMaterialAdapterBinding

class OtherMaterialAdapter(
    private val context: Context,
    var lr: ArrayList<DeviceItems>
): RecyclerView.Adapter<OtherMaterialAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherMaterialAdapter.ViewHolder {
        val binding = OtherMaterialAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(lr?.get(position)) {
                //binding!!.refNo.text= this?.ref_no
                binding!!.materialName.text= this?.item
                binding!!.quantity.text= this?.quantity + " "+"No."
            }
        }
    }

    override fun getItemCount(): Int {
        return lr!!.size
    }

    inner class ViewHolder(val binding: OtherMaterialAdapterBinding)
        :RecyclerView.ViewHolder(binding.root)
}