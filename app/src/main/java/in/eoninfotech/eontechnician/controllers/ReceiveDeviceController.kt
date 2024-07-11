package `in`.eoninfotech.eontechnician.controllers

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.androidadvance.topsnackbar.R
import com.androidadvance.topsnackbar.TSnackbar
import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener
import `in`.eoninfotech.eontechnician.webservice.ApiHolder
import `in`.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiveDeviceController : Controller() {

    var version: String? = null
    var v: View? = null
    var client_att: ApiHolder = ServiceConnectionNewURL.getClient(version).create<ApiHolder>(
        ApiHolder::class.java
    )
    var clientCall: Call<MainResponse>? = null

    fun requestReceiveDevice(
        from_date: String?,
        to_date: String?,
        status: String?,
        tech_id: String?,
        listener: ReceiveDeviceListener
    ) {
        clientCall = client_att.get_dispatch_material_status(from_date, to_date, status, tech_id)
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.receiveDeviceResponse(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun requestDispatchDevice(
            from_date: String?,
            to_date: String?,
            status: String?,
            tech_id: String?,
            listener: ReceiveDeviceListener
    ) {
        clientCall = client_att.get_return_material_status(from_date, to_date, status, tech_id)
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.receiveDeviceResponse(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                            v!!,
                            "Server Response Timeout, Try Again!",
                            TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun requestDispatchedDevice(dispatch_id: String?,transit_through: String?,techid: String?,mainid: String?,type: String?, listener: ReceiveDeviceListener) {
        clientCall = client_att.get_dispatched_device(dispatch_id,transit_through,techid,mainid,type)
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.receiveDeviceResponse(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun receiveDispatchedMaterial(dispatch_id: String?,challan_id: String?,tech_id: String?,items_collected: String?,accessories_collected: String?,remarks: String?, listener: ReceiveDeviceListener) {
        clientCall = client_att.receive_dispatched_material(dispatch_id,challan_id,tech_id,items_collected,accessories_collected,remarks)
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.receiveDispatchMaterial(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun deviceList(listener: ReceiveDeviceListener) {
        clientCall = client_att.get_item_list()
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.receiveDispatchMaterial(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun transitList(listener: ReceiveDeviceListener) {
        clientCall = client_att.get_dispatch_type_list()
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.receiveDeviceResponse(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun return_material(
        tech_id: String?,
        sr_no: String?,
        item_qty: String?,
        transit_type:String,
        transit_name:String,
        transit_through:String,
        remarks: String?,other_tech_id:String?, listener: ReceiveDeviceListener) {
        clientCall = client_att.return_material(tech_id,sr_no,item_qty,transit_type,transit_name,transit_through,remarks,other_tech_id)
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.dispatchFromTechResponse(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }

    fun returnDeviceList(tech_id: String?, listener: ReceiveDeviceListener) {
        clientCall = client_att.removed_device_list(tech_id)
        clientCall!!.enqueue(object : Callback<MainResponse?> {
            override fun onResponse(call: Call<MainResponse?>, response: Response<MainResponse?>) {
                listener.returnDeviceresponse(response.body())
            }

            override fun onFailure(call: Call<MainResponse?>, t: Throwable) {
                try {
                    val snackbar = TSnackbar.make(
                        v!!,
                        "Server Response Timeout, Try Again!",
                        TSnackbar.LENGTH_LONG
                    )
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.RED)
                    val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    snackbar.show()
                } catch (e: Exception) {
                }
            }
        })
    }
}