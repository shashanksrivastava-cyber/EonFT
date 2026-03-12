package `in`.eoninfotech.eontechnician.responses

import com.squareup.wire.internal.JvmField

data class InstallationDetails(
    @JvmField var veh_type: String,
    @JvmField var inst_status: String,

)
