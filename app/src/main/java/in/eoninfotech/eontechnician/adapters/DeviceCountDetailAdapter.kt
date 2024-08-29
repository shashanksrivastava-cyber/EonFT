package `in`.eoninfotech.eontechnicianactivity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import `in`.eoninfotech.eontechnician.responses.DeviceCountDetail
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.databinding.DeviceCountDetailAdapterBinding
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.RadioButton

import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import `in`.eoninfotech.eontechnician.utils.DashboardDiffutil
import androidx.recyclerview.widget.DiffUtil;


class DeviceCountDetailAdapter (
    private val context: Context,
    var lr: ArrayList<DeviceCountDetail>,
    var out_status: String? = "",
    var change_status: String? = "",
    var listener: MessageAdapterListener,
) : RecyclerView.Adapter<DeviceCountDetailAdapter.ViewHolder>(){

    private var oldPersonList = emptyList<DeviceCountDetail>()
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

                if(out_status.equals("ITT")){
                    binding.changeDeviceStatus.setVisibility(View.GONE)
                    binding.status.setVisibility(View.GONE)
                    binding.dateTime.setVisibility(View.VISIBLE)
                    binding!!.dateTime.text = this?.date_time
                }else if(out_status.equals("ITS")){
                    binding.changeDeviceStatus.setVisibility(View.GONE)
                    binding.status.setVisibility(View.GONE)
                    binding.dateTime.setVisibility(View.VISIBLE)
                    binding!!.dateTime.text = this?.date_time
                }else {
                    binding.changeDeviceStatus.setVisibility(View.VISIBLE)
                    binding.status.setVisibility(View.VISIBLE)
                    binding.dateTime.setVisibility(View.GONE)
                }

                binding.changeDeviceStatus.setOnClickListener{

                    if(this?.status.equals("Working")){
                        showDialog(this?.pcb_sr_no,"W")
                    }else {
                        showDialog(this?.pcb_sr_no,"F")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lr!!.size
    }

    fun setData(newPersonList: List<DeviceCountDetail>){
        val diffUtil  = DashboardDiffutil(oldPersonList,newPersonList)
        val resultDiffUtil = DiffUtil.calculateDiff(diffUtil)
        oldPersonList = newPersonList
        resultDiffUtil.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: DeviceCountDetailAdapterBinding)
        :RecyclerView.ViewHolder(binding.root)


    private fun showDialog(pcb_sr_no: String?,btn_status: String?){

        var alet_view: View? = null
        val alertDialogBuilder = Dialog(context)
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialogBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mInflater = this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        alet_view = mInflater.inflate(R.layout.dialog_custom, null)
        alertDialogBuilder.setContentView(alet_view!!)
        var bt_close = alertDialogBuilder.findViewById<AppCompatButton>(R.id.bt_close)
        var bt_continue = alertDialogBuilder.findViewById<AppCompatButton>(R.id.bt_continue)
        var radioGroup = alertDialogBuilder.findViewById<RadioGroup>(R.id.radioGroup)

        if(btn_status.equals("W")){
            radioGroup.check(R.id.rb_working)
            change_status = "W"
        }else {
            radioGroup.check(R.id.rb_faulty)
            change_status = "F"
        }

        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
               if(checkedId ==R.id.rb_working){
                   change_status = "W"
               }else {
                   change_status = "F"
               }
            })

        bt_close.setOnClickListener{
            alertDialogBuilder.dismiss()
        }

        bt_continue.setOnClickListener{
            alertDialogBuilder.dismiss()
            listener.onButtonClick(pcb_sr_no,change_status)
        }

        alertDialogBuilder.window!!
            .setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
        alertDialogBuilder.setCanceledOnTouchOutside(true)
        alertDialogBuilder.show()
    }

    interface MessageAdapterListener {
        open fun onButtonClick(pcb_sr_no: String?, change_status: String?)

    }

}