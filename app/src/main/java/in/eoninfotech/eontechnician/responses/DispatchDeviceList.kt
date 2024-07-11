package `in`.eoninfotech.eontechnician.responses;

data class DispatchDeviceList(
    val id: String,
    val type: String,
    val ref_no: String,
    val dispatch_id: String,
    val transit_name: String,
    val transit_type: String,
    val transit_through: String,
    val dispatched_date: String,
    val tech_received_date: String,
    val status: String,
) {

}
