package `in`.eoninfotech.eontechnician.responses

import com.squareup.wire.internal.JvmField

data class RemovalDetails(
    @JvmField var veh_type: String,
    @JvmField var reg_no: String,
    @JvmField var dehired_date: String,
    @JvmField var device_status: String,
    @JvmField var removal_status: String,
){

}
