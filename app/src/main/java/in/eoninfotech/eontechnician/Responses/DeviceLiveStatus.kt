package `in`.eoninfotech.eontechnician.Responses

data class DeviceLiveStatus (
    val vts_id: String,
    val reg_no: String,
    val serial_no: String,
    val depot: String,
    val veh_type_name: String,
    val drum_sensor: String,
    val lid_sensor: String,
    val speed: String,
    val gps: String,
    val gsm: String,
    val power: String,
    val battery: String,
    val status: String
) {
}