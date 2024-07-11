package `in`.eoninfotech.eontechnician.responses

data class TechReturnDevice (
        @JvmField val sr_no: String,
        @JvmField val id_no: String,
        @JvmField val tech_name: String,
        @JvmField val device_count: String,
        @JvmField val transit_type: String,
        @JvmField val transit_name: String,
        @JvmField val transit_through: String,
        @JvmField val dispatched_to: String,
        @JvmField val dispatched_date: String,
        @JvmField val received_date: String,
        @JvmField val status: String,
) {
}