package `in`.eoninfotech.eontechnicianactivity

import android.content.Context
import `in`.eoninfotech.eontechnician.responses.DeviceCountDetail
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.databinding.DeviceCountDetailAdapterBinding

class DeviceCountDetailAdapter (
    private val context: Context,
    var lr: ArrayList<DeviceCountDetail>,
) : RecyclerView.Adapter<DeviceCountDetailAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeviceCountDetailAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(lr?.get(position)) {
                binding!!.srNo.text = this?.pcb_sr_no
                binding!!.vtsId.text=this?.vts_id
                binding!!.custName.text = this?.customer

                binding!!.status.text= this?.status
                if(!this?.status.equals("Working")){
                    binding.status.setBackgroundResource(R.color.dash_red)
                }else {
                    binding.status.setBackgroundResource(R.color.green)
                }

                if((!this?.status.equals("Working"))&&(!this?.status.equals("Faulty"))&&(!this?.status.equals("Total"))){
                    binding.changeDeviceStatus.setVisibility(View.GONE)
                    }else {
                    binding.changeDeviceStatus.setVisibility(View.VISIBLE)
                    }

            }
        }
    }

    override fun getItemCount(): Int {
        return lr!!.size
    }

    inner class ViewHolder(val binding: DeviceCountDetailAdapterBinding)
        :RecyclerView.ViewHolder(binding.root)


}