package `in`.eoninfotech.eontechnician.Responses


data class DeviceLiveStatus (
    @JvmField val sr_no: String,
    @JvmField val vts_id: String,
    @JvmField val reg_no: String,
    @JvmField val serial_no: String,
    @JvmField val depot: String,
    @JvmField val veh_type_name: String,
    @JvmField val drum_sensor: String,
    @JvmField val lid_sensor: String,
    @JvmField val speed: String,
    @JvmField val gps: String,
    @JvmField val gsm: String,
    @JvmField val power: String,
    @JvmField val battery: String,
    @JvmField val status: String,
    @JvmField val status_type: String
) {
}