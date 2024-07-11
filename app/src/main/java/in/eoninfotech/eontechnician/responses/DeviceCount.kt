package `in`.eoninfotech.eontechnician.responses

data class DeviceCount(

    @JvmField val total: String,
    @JvmField val working: String,
    @JvmField val faulty: String,
    @JvmField val in_transit_store: String,
    @JvmField val in_transit_tech: String,
){

}
