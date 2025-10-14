package `in`.eoninfotech.eontechnician.responses

import com.google.gson.annotations.SerializedName

data class LoginDetail(

    var usr_id: String? = null,
    var verno: String? = null,
    var usrtype: String? = null,
    var usrname: String? = null,
    var displayname: String? = null,
    var zone: String? = null,
    var contact: String? = null,
    var location: String? = null,
    var image: String? = null,
    var track_status: String? = null,
    var track_interval: String? = null,
    var bill_amt_limit: String? = null,
) {
}