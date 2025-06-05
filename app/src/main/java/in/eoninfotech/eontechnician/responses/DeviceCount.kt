package `in`.eoninfotech.eontechnician.responses

data class DeviceCount(

    @JvmField val total: String,
    @JvmField val working: String,
    @JvmField val faulty: String,
    @JvmField val in_transit_store: String,
    @JvmField val in_transit_tech: String,
    @JvmField val total_drs: String,
    @JvmField val working_drs: String,
    @JvmField val faulty_drs: String,
){

}
