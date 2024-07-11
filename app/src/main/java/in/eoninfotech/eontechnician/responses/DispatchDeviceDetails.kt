package `in`.eoninfotech.eontechnician.responses

data class DispatchDeviceDetails(

    val device_list: ArrayList<DeviceList>,
    var device_items: ArrayList<DeviceItems>,

) {
}