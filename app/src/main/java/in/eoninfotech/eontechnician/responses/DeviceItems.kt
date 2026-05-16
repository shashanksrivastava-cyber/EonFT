package `in`.eoninfotech.eontechnician.responses

data class DeviceItems(

    val id: String,
    val item: String,
    val quantity: String,
    val recv_count: String,

    var selectedQty: Int = 0
) {
}