package `in`.eoninfotech.eontechnician.responses

data class TechReturnDetails(
        @JvmField val status: String,
        @JvmField val pcb_sr_no: String,
        @JvmField val cust_name: String,
) {
}