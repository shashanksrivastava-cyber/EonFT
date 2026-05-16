package `in`.eoninfotech.eontechnician

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.eoninfotech.eontechnician.responses.DeviceItems
import `in`.eoninfotech.eontechnician.databinding.OtherMaterialAdapterBinding
import android.widget.Toast
import android.view.View

class OtherMaterialAdapter(
    private val context: Context,
    var lr: ArrayList<DeviceItems>,
    var status: String?,
): RecyclerView.Adapter<OtherMaterialAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherMaterialAdapter.ViewHolder {
        val binding = OtherMaterialAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){

            with(lr.get(position)) {

                val k = (position + 1).toString()
                binding.materialName.text = "$k. $item"
//                binding.quantity.text = this.quantity
//                binding.addText.text = this.quantity

//                val totalQty = this.quantity.toIntOrNull() ?: 0
//                val receivedQty = this.recv_count.toIntOrNull() ?: 0

                val totalQty = this.quantity?.toIntOrNull() ?: 0
                val receivedQty = this.recv_count?.toIntOrNull() ?: 0

                //val remainingQty = totalQty - receivedQty

//                binding.quantity.text = totalQty.toString()
//                binding.addText.text = remainingQty.toString()

                val maxQuantity = maxOf(0, totalQty - receivedQty)

                if (this.selectedQty == 0 && maxQuantity > 0) {
                    this.selectedQty = maxQuantity
                }

                binding.quantity.text = totalQty.toString()
                binding.addText.text = this.selectedQty.toString()

//                binding.deleteButton.setOnClickListener {
//                    var value = binding.addText.text.toString().toIntOrNull() ?: 0
//
//                    if (value > 0) {
//                        value--
//                        binding.addText.text = value.toString()
//                    }
//                }

                binding.deleteButton.setOnClickListener {

                    if (this.selectedQty > 0) {
                        this.selectedQty--
                        binding.addText.text = this.selectedQty.toString()
                    }
                }

                binding.addButton.setOnClickListener {

                    if (this.selectedQty < maxQuantity) {
                        this.selectedQty++
                        binding.addText.text = this.selectedQty.toString()
                    } else {
                        Toast.makeText(context, "Max quantity reached", Toast.LENGTH_SHORT).show()
                    }
                }

//                binding.addButton.setOnClickListener {
//                    var value = binding.addText.text.toString().toIntOrNull() ?: 0
//
//                    if (value < maxQuantity) {
//                        value++
//                        binding.addText.text = value.toString()
//                    } else {
//                        // Optional: show message
//                        Toast.makeText(binding.root.context, "Max quantity reached", Toast.LENGTH_SHORT).show()
//                    }
//                }

//                if (status == "Received" || status == "Send") {
//                    binding.addCount.visibility = View.GONE
//                } else {
//                    binding.addCount.visibility = View.VISIBLE
//                }

                if (status == "Received" || status == "Send" || totalQty == receivedQty) {
                    binding.addCount.visibility = View.GONE
                } else {
                    binding.addCount.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lr.size
    }

    inner class ViewHolder(val binding: OtherMaterialAdapterBinding)
        :RecyclerView.ViewHolder(binding.root)
}