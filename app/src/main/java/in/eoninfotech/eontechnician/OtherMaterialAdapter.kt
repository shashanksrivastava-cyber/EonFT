package `in`.eoninfotech.eontechnician

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.eoninfotech.eontechnician.Responses.DeviceItems
import `in`.eoninfotech.eontechnician.databinding.OtherMaterialAdapterBinding
import android.widget.Toast

class OtherMaterialAdapter(
    private val context: Context,
    var lr: ArrayList<DeviceItems>,
): RecyclerView.Adapter<OtherMaterialAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherMaterialAdapter.ViewHolder {
        val binding = OtherMaterialAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(lr?.get(position)) {
                val val1 = 1
                var k = position.plus(val1)!!.toString()
                binding!!.materialName.text= k.plus(". ").plus(this?.item)
                binding!!.quantity.text= this?.quantity
                binding!!.addText.text= this?.quantity

                binding!!.deleteButton.setOnClickListener {

                    var value = binding!!.addText.text.toString()!!.toInt()

                    if (value > 0) {
                        value--
                        binding!!.addText.text = value.toString()

                        //callback.onItemClicked(item)
                    }
                }

                binding!!.addButton.setOnClickListener {

                    var value = binding!!.addText.text.toString()!!.toInt()
                    value++
                    binding!!.addText.text = value.toString()
                }

//                if(status.equals("Received")){
//                    binding!!.addCount.setEnabled(false)
//                }else {
//                    binding!!.addCount.setEnabled(true)
//                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lr!!.size
    }

    inner class ViewHolder(val binding: OtherMaterialAdapterBinding)
        :RecyclerView.ViewHolder(binding.root)


//    interface AdapterCallback {
//        fun onItemClicked(data: HashMap<String, Any>)
//    }

}