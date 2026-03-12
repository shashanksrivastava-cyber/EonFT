package `in`.eoninfotech.eontechnician.responses

import com.squareup.wire.internal.JvmField

data class ServiceRequestDetailResponse(

    @JvmField val id_no: String,
    @JvmField val req_no: String,
    @JvmField val request_date: String,
    @JvmField val main_customer: String,
    @JvmField val sub_customer: String,
    @JvmField val location: String,
    @JvmField val pl_name: String,
    @JvmField val pl_contact: String,
    @JvmField val veh_avail_date: String,
    @JvmField val status: String,

    @JvmField val inst_details: List<InstallationDetails>,

    @JvmField val removal_details: List<RemovalDetails>
) {

}
