package `in`.eoninfotech.eontechnician.responses

data class ServiceCountDetailResponse(

    @JvmField val id: String,
    @JvmField val req_no: String,
    @JvmField val request_date: String,
    @JvmField val main_customer: String,
    @JvmField val sub_customer: String,
    @JvmField val location: String,
    @JvmField val status: String,
    @JvmField val total_req: String,
    @JvmField val pending_req: String,

){

}
