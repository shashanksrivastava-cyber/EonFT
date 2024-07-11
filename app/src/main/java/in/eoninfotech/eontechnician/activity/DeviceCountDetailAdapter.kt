package `in`.eoninfotech.eontechnicianactivity

import android.content.Context
import `in`.eoninfotech.eontechnician.responses.DeviceCountDetail
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import `in`.eoninfotech.eontechnician.databinding.DeviceCountDetailAdapterBinding

class DeviceCountDetailAdapter (
    private val context: Context,
    var lr: ArrayList<DeviceCountDetail>,
) : RecyclerView.Adapter<DeviceCountDetailAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceCountDetailAdapter.ViewHolder {
        val binding = DeviceCountDetailAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(lr?.get(position)) {

            }
        }
    }

    override fun getItemCount(): Int {
        return lr!!.size
    }

    inner class ViewHolder(val binding: DeviceCountDetailAdapterBinding)
        :RecyclerView.ViewHolder(binding.root)


}