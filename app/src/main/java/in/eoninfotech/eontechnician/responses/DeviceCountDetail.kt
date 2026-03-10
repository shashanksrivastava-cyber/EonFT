package `in`.eoninfotech.eontechnician.responses

data class DeviceCountDetail(

    @JvmField val pcb_sr_no: String,
    @JvmField val vts_id: String,
    @JvmField val customer: String,
    @JvmField val status: String,
    @JvmField val date_time: String,

){

}