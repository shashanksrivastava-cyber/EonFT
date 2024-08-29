package `in`.eoninfotech.eontechnician.utils


import `in`.eoninfotech.eontechnician.responses.DeviceCountDetail

import androidx.recyclerview.widget.DiffUtil;
class DashboardDiffutil(

    private val oldList: List<DeviceCountDetail>,
    private val newList: List<DeviceCountDetail>
) : DiffUtil.Callback() {

    override fun getOldListSize():Int{
       return oldList.size
    }

    override fun getNewListSize(): Int{
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPsition: Int):Boolean{

        return oldList[oldItemPosition].pcb_sr_no == newList[newItemPsition].pcb_sr_no
    }

    override fun areContentsTheSame(oldItemPosition: Int,newItemPsition: Int):Boolean{

        return when{
            oldList[oldItemPosition].pcb_sr_no != newList[newItemPsition].pcb_sr_no -> {
                false
            }
            else -> true
        }
    }

}