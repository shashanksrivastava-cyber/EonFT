package `in`.eoninfotech.eontechnician

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.eoninfotech.eontechnician.Responses.DispatchDeviceList
import `in`.eoninfotech.eontechnician.activity.ReceiveDeviceDetails
import `in`.eoninfotech.eontechnician.databinding.DispatchedMaterialListBinding


class ReceiveDeviceAdapter(
    private val context: Context,
    var lr: ArrayList<DispatchDeviceList?>?
) : RecyclerView.Adapter<ReceiveDeviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DispatchedMaterialListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return lr!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(lr?.get(position)) {
                binding.refNo.text= this?.ref_no
                binding.type.text= this?.transit_type
                binding.dispatchDate.text= this?.dispatched_date
                binding.docket.text= this?.transit_through

                if(this!!.transit_name.equals("")){
                    binding.name.setVisibility(View.GONE)
                }else {
                    binding.name.text= this.transit_name
                }
                binding.status.text= this.status
                if(!this.status.equals("Received")){
                   binding.status.setBackgroundResource(R.color.dash_red)
                }else {
                    binding.status.setBackgroundResource(R.color.green)
                }

                binding.details.setOnClickListener{
                    val intent = Intent(context, ReceiveDeviceDetails::class.java)
                    intent.putExtra("dispatch_id",this.dispatch_id)
                    intent.putExtra("ref_no",this.ref_no)
                    intent.putExtra("transit_through",this.transit_through)
                    intent.putExtra("status",this.status)
                    intent.putExtra("id",this.id)
                    intent.putExtra("type",this.type)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class ViewHolder(val binding: DispatchedMaterialListBinding)
        :RecyclerView.ViewHolder(binding.root)

}
