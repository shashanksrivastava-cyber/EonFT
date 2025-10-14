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
                val val1 = 1
                val k = position.plus(val1).toString()
                binding.materialName.text= k.plus(". ").plus(this.item)
                binding.quantity.text= this.quantity
                binding.addText.text= this.quantity

                binding.deleteButton.setOnClickListener {

                    var value = binding.addText.text.toString().toInt()

                    if (value > 0) {
                        value--
                        binding.addText.text = value.toString()
                    }
                }

                binding.addButton.setOnClickListener {

                    var value = binding.addText.text.toString().toInt()
                    value++
                    binding.addText.text = value.toString()
                }

                if(status.equals("Received")){
                    binding.addCount.setVisibility(View.GONE)
                }else {
                    if(status.equals("Send")){
                        binding.addCount.setVisibility(View.GONE)
                    }else{
                    binding.addCount.setVisibility(View.VISIBLE)
                    }
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