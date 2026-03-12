package `in`.eoninfotech.eontechnician

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import `in`.eoninfotech.eontechnician.responses.DispatchDeviceList
import `in`.eoninfotech.eontechnician.activity.ReceiveDeviceDetails
import `in`.eoninfotech.eontechnician.databinding.DispatchedMaterialListBinding



class ReceiveDeviceAdapter(
    private val context: Context,
    var lr: ArrayList<DispatchDeviceList?>?
) : RecyclerView.Adapter<ReceiveDeviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DispatchedMaterialListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = lr?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lr?.get(position) ?: return
        with(holder.binding) {
            refNo.text = item.ref_no
            type.text = item.transit_type
            dispatchDate.text = item.dispatched_date
            docket.text = item.transit_through

            if (item.transit_name.isNullOrEmpty()) {
                name.visibility = View.GONE
            } else {
                name.visibility = View.VISIBLE
                name.text = item.transit_name
            }

            status.text = item.status
            if (item.status != "Received") {
                status.setBackgroundResource(R.color.dash_red)
            } else {
                status.setBackgroundResource(R.color.green)
            }

            details.setOnClickListener {
                val intent = Intent(context, ReceiveDeviceDetails::class.java).apply {
                    putExtra("dispatch_id", item.dispatch_id)
                    putExtra("ref_no", item.ref_no)
                    putExtra("transit_through", item.transit_through)
                    putExtra("status", item.status)
                    putExtra("id", item.id)
                    putExtra("type", item.type)
                }
                context.startActivity(intent)
            }
        }
    }

    inner class ViewHolder(val binding: DispatchedMaterialListBinding)
        : RecyclerView.ViewHolder(binding.root)

    /**
     * This function efficiently updates the list using DiffUtil
     */
    fun updateList(newList: ArrayList<DispatchDeviceList?>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = lr?.size ?: 0
            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = lr?.get(oldItemPosition)
                val newItem = newList[newItemPosition]
                // Compare unique IDs or primary key
                return oldItem?.id == newItem?.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = lr?.get(oldItemPosition)
                val newItem = newList[newItemPosition]
                // Compare the entire object or specific fields
                return oldItem == newItem
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        lr = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
