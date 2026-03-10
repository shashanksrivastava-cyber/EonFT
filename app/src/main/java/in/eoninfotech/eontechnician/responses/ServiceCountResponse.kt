package `in`.eoninfotech.eontechnician.responses

import com.google.gson.annotations.SerializedName

data class ServiceCountResponse(

    @JvmField val type: String,
    @JvmField val total_count: String,
    @JvmField val install_pending_count: String,
    @JvmField val removal_pending_count: String,
    @JvmField val message: String,


){

}
