//package `in`.eoninfotech.eontechnician.fragments
//
//import android.content.SharedPreferences
//import android.graphics.Color
//import androidx.fragment.app.Fragment
//import `in`.eoninfotech.eontechnician.databinding.AdditionalMaterialRequirementBinding
//import `in`.eoninfotech.eontechnician.databinding.FieldsAdditionalVtsBinding
//import `in`.eoninfotech.eontechnician.databinding.FieldAdditionalAccessoryBinding
//import `in`.eoninfotech.eontechnician.helper.CheckConnection
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import `in`.eoninfotech.eontechnician.controllers.NewInstallmentController
//import `in`.eoninfotech.eontechnician.callbacks.ClientListener
//import `in`.eoninfotech.eontechnician.responses.ClientLocationResponse
//
//import `in`.eoninfotech.eontechnician.responses.ClientResponse
//
//import `in`.eoninfotech.eontechnician.responses.CollectedItemsResponse
//
//import `in`.eoninfotech.eontechnician.responses.DeviceCountDetail
//
//import `in`.eoninfotech.eontechnician.responses.DisconnectionResponse
//
//import `in`.eoninfotech.eontechnician.responses.FaultResponse
//
//import `in`.eoninfotech.eontechnician.responses.MainClientList
//
//import `in`.eoninfotech.eontechnician.responses.MainResponse
//
//import `in`.eoninfotech.eontechnician.responses.NotAvailActivityResponse
//
//import `in`.eoninfotech.eontechnician.responses.PaymentMethodResponse
//
//import `in`.eoninfotech.eontechnician.responses.RemovalActivityResponse
//
//import `in`.eoninfotech.eontechnician.responses.RemovalResponse
//
//import `in`.eoninfotech.eontechnician.responses.ReplaceReason
//
//import `in`.eoninfotech.eontechnician.responses.SimOperatorResponse
//
//import `in`.eoninfotech.eontechnician.responses.SimReplaceResponse
//
//import `in`.eoninfotech.eontechnician.responses.VTSResponse
//
//import `in`.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse
//
//import `in`.eoninfotech.eontechnician.responses.VehicleTypeResponse
//
//import `in`.eoninfotech.eontechnician.responses.WorkTypeResponse
//
//import `in`.eoninfotech.eontechnician.viewModel.ViewModelCountDetails
//
//import `in`.eoninfotech.eontechnician.viewModel.ViewModelDeviceDashboard
//
//import `in`.eoninfotech.eontechnician.webservice.ApiHolder
//
//import `in`.eoninfotech.eontechnician.webservice.BillResponse
//
//import `in`.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL
//import java.util.ArrayList
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.ImageButton
//import android.widget.LinearLayout
//import android.widget.Spinner
//import android.widget.TextView
//import android.widget.Toast
//import com.google.android.material.snackbar.Snackbar
//import `in`.eoninfotech.eontechnician.R
//import `in`.eoninfotech.eontechnician.helper.K
//import `in`.eoninfotech.eontechnician.webservice.ServiceConnection
//import java.lang.Exception
//import `in`.eoninfotech.eontechnician.responses.DeviceTypeOtherAis
//import `in`.eoninfotech.eontechnician.callbacks.ReceiveDeviceListener
//
//import `in`.eoninfotech.eontechnician.controllers.ReceiveDeviceController
//import `in`.eoninfotech.eontechnician.responses.ItemList
//import android.widget.EditText
//
//
//class AdditionalMaterialFragment : Fragment(), ClientListener, ReceiveDeviceListener {
//
//    var binding : AdditionalMaterialRequirementBinding?= null
//    val _binding get() = binding!!
//
//    var acc_binding : FieldAdditionalAccessoryBinding?=null
//
//    var rowView: View? = null
//    var accRowView: View? = null
//    var parentLinearLayout: LinearLayout? = null
//    var accessoryLinearLayout: LinearLayout? = null
//    var spinner: Spinner? = null
//    var sharedprefs: SharedPreferences? = null
//    var editor: SharedPreferences.Editor? = null
//    var version: String? = null
//    var username: String? = null
//    var techid: String? = null
//    var type_id: Int? = null
//    var device_id: String? = null
//    var acc_id: String? = null
//    var type_name: String? = null
//    var device_name: String? = null
//    var acc_name: String? = null
//    var chk = CheckConnection(activity)
//    var newInstallmentController: NewInstallmentController? = null
//    var receiveDeviceController: ReceiveDeviceController? = null
//    var mainclientList = ArrayList<MainClientList>()
//    var mainClientDetail = ArrayList<String>()
//    var adapter: ArrayAdapter<String>? = null
//    var deviceTypeOtherAis_arr: ArrayList<DeviceTypeOtherAis> = ArrayList<DeviceTypeOtherAis>()
//    var arr_device_types = ArrayList<String>()
//    var itemList: ArrayList<ItemList> = ArrayList<ItemList>()
//    var itemDetails = ArrayList<String>()
//    var delete_button: ImageButton? = null
//    var acc_delete_button: ImageButton? = null
//    var newMainClients: Spinner? = null
//    var accessorySpinner: Spinner? = null
//    var device_type: Spinner? = null
//    var parent_linear_layout: LinearLayout? = null
//    var vehicletype = ArrayList<String>()
//
//    override fun onCreateView(
//
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        // inflate the layout and bind to the _binding
//        binding = AdditionalMaterialRequirementBinding.inflate(inflater, container, false)
////        val view = _binding.root
////        acc_binding = FieldAdditionalAccessoryBinding.bind(view);
//
//        var  sharedprefs = activity?.getSharedPreferences("login_user_pass", AppCompatActivity.MODE_PRIVATE)
//        var editor = sharedprefs?.edit()
//        username = sharedprefs?.getString("s_uuser", "")
//        version = sharedprefs?.getString("version", "")
//        techid = sharedprefs?.getString("s_user_id", "")
//
//        initView()
//
//        return _binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//
//    private fun initView() {
//
//        newInstallmentController = NewInstallmentController()
//        receiveDeviceController = ReceiveDeviceController()
//        addClients()
//        addDevice()
//        getAccessoryList()
//
//        binding!!.deleteButton.setOnClickListener(object : android.view.View.OnClickListener {
//            override fun onClick(v: android.view.View?) {
//                binding!!.parentLinearLayout.removeView(v?.getParent() as android.view.View?)
//            }
//        })
//
//        binding!!.accDeleteButton.setOnClickListener(object : android.view.View.OnClickListener {
//            override fun onClick(v: android.view.View?) {
//                binding!!.accessoryLinearLayout.removeView(v?.getParent() as android.view.View?)
//            }
//        })
//
//        binding!!.addAccessory.setOnClickListener(object : android.view.View.OnClickListener {
//            override fun onClick(v: android.view.View?) {
//                val inflater: android.view.LayoutInflater? =
//                    requireContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater?
//                accRowView = inflater!!.inflate(`in`.eoninfotech.eontechnician.R.layout.field_additional_accessory, null)
//                acc_delete_button = accRowView!!.findViewById(R.id.acc_delete_button)
//                accessorySpinner = accRowView!!.findViewById(R.id.accessory_spinner)
//                accessoryLinearLayout = accRowView!!.findViewById(R.id.accessory_linear_layout)
//                binding!!.accessoryLinearLayout!!.addView(accRowView, binding!!.accessoryLinearLayout!!.getChildCount() - 1)
//
//                adapter = activity?.let{ArrayAdapter<String>(it, R.layout.simple_custom_spinner_item, itemDetails)}
//                adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                accessorySpinner!!.setAdapter(adapter)
//
//                accessorySpinner!!.onItemSelectedListener = object :
//                    AdapterView.OnItemSelectedListener {
//                    override fun onItemSelected(parent: AdapterView<*>,
//                                                view: View, i: Int, id: Long) {
//                        getAccessory(i)
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>) {
//                        // write code to perform some action
//                    }
//                }
//
//                acc_delete_button!!.setOnClickListener(object : android.view.View.OnClickListener {
//                    override fun onClick(view: android.view.View?) {
//                        binding!!.accessoryLinearLayout!!.removeView(view?.getParent() as android.view.View?)
//
//                    }
//                })
//            }
//        })
//
//        binding!!.addMaterial.setOnClickListener(object : android.view.View.OnClickListener {
//            override fun onClick(v: android.view.View?) {
//                val inflater: android.view.LayoutInflater? =
//                    requireContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater?
//                rowView = inflater!!.inflate(`in`.eoninfotech.eontechnician.R.layout.fields_additional_vts, null)
//                delete_button = rowView!!.findViewById(R.id.delete_button)
//                newMainClients = rowView!!.findViewById(R.id.new_main_clients)
//                device_type = rowView!!.findViewById(R.id.device_type)
//                parent_linear_layout = rowView!!.findViewById(R.id.parent_linear_layout)
//                binding!!.parentLinearLayout!!.addView(rowView, binding!!.parentLinearLayout!!.getChildCount() - 1)
//
//                adapter = activity?.let{ArrayAdapter<String>(it, R.layout.simple_custom_spinner_item, mainClientDetail)}
//                adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                newMainClients!!.setAdapter(adapter)
//
//                adapter = activity?.let{ArrayAdapter<String>(it, R.layout.simple_custom_spinner_item, arr_device_types)}
//                adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                device_type!!.setAdapter(adapter)
//
//                delete_button!!.setOnClickListener(object : android.view.View.OnClickListener {
//                    override fun onClick(view: android.view.View?) {
//                        binding!!.parentLinearLayout!!.removeView(view?.getParent() as android.view.View?)
//                    }
//                })
//            }
//        })
//
//        binding!!.newMainClients.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>,
//                                        view: View, i: Int, id: Long) {
//
//                if (i == 0) {
//                    return
//                } else {
//                     var i = i - 1
//                }
//                type_id = mainclientList.get(i-1).getClient_Id()
//                type_name = mainclientList.get(i-1).getClient_Name()
//
//                Toast.makeText(getActivity(), ""+type_name, Toast.LENGTH_LONG).show()
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }
//
//        binding!!.deviceType.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>,
//                                        view: View, j: Int, id: Long) {
//
//                if (j == 0) {
//                    return
//                } else {
//                    var j = j - 1
//                }
//                device_id = deviceTypeOtherAis_arr.get(j-1).getId()
//                device_name = deviceTypeOtherAis_arr.get(j-1).getName()
//
//                Toast.makeText(getActivity(), ""+device_name, Toast.LENGTH_LONG).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }
//
//        binding!!.accessorySpinner.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>,
//                                        view: View, i: Int, id: Long) {
//                getAccessory(i)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // write code to perform some action
//            }
//        }
//
//        binding!!.submit.setOnClickListener(object : android.view.View.OnClickListener {
//            override fun onClick(view: android.view.View?) {
//
//                  for (i in 0 until binding!!.accessoryLinearLayout!!.childCount-2) {
//                      var view1 = binding!!.accessoryLinearLayout!!.getChildAt(i);
//
//                         spinner = view1.findViewById<Spinner>(R.id.accessory_spinner);
//                         val ed_value = view1.findViewById<EditText>(R.id.acc_etQuantity)
//
//                      for (entry in itemList) {
//
//                          if ((entry.name.equals(binding!!.accessorySpinner.getSelectedItem().toString(), ignoreCase = true))) {
//                              vehicletype.add(entry.getId() + ":" + binding!!.accEtQuantity.getText().toString())
//                              android.widget.Toast.makeText(getActivity(), ""+vehicletype.toString(), android.widget.Toast.LENGTH_LONG).show()
//                          }
//
////                          if ((entry.name.equals(binding!!.accessorySpinner.getSelectedItem().toString(), ignoreCase = true))||(entry.name.equals(accessorySpinner!!.getSelectedItem().toString(), ignoreCase = true))) {
////                              vehicletype.add(entry.getId() + ":" + binding!!.accEtQuantity.getText().toString())
////                              android.widget.Toast.makeText(getActivity(), ""+vehicletype.toString(), android.widget.Toast.LENGTH_LONG).show()
////                          }
////                          if ((entry.name.equals(spinner!!.getSelectedItem().toString(), ignoreCase = true))) {
////                              vehicletype.add(entry.getId() + ":" + ed_value.getText().toString())
////                              android.widget.Toast.makeText(getActivity(), ""+vehicletype.toString(), android.widget.Toast.LENGTH_LONG).show()
////                          }
//                      }
//                  }
//            }
//        })
//    }
//
//    private fun addClients(){
//        newInstallmentController?.reqeuestMainClientList(this)
//    }
//
//    private fun getAccessoryList(){
//        receiveDeviceController?.deviceList(this)
//    }
//
//    public fun getAccessory(i : Int){
//        if (i == 0) {
//            return
//        } else {
//            var i = i - 1
//        }
//        acc_id = itemList.get(i-1).getId()
//        acc_name = itemList.get(i-1).getName()
//
//        Toast.makeText(getActivity(), ""+acc_name, Toast.LENGTH_LONG).show()
//    }
//
//    private fun addDevice() {
//        val get_list = ServiceConnection.getClient(version).create(
//            ApiHolder::class.java
//        )
//        val call = get_list.deviceTypes
//        call.enqueue(object : retrofit2.Callback<MainResponse?> {
//            override fun onResponse(
//                call: retrofit2.Call<MainResponse?>?,
//                response: retrofit2.Response<MainResponse?>?
//            ) {
//                try {
//                    if (response!!.body()!!.getType() == 1) {
//                        deviceTypeOtherAis_arr = response.body()!!.getDeviceTypesArr()
//                        try {
//                            try {
//                                arr_device_types.clear()
//                            } catch (e: java.lang.Exception) {
//                                e.printStackTrace()
//                            }
//                            arr_device_types.add("SELECT DEVICE TYPE")
//                            for (i in deviceTypeOtherAis_arr.indices) {
//                                arr_device_types.add(deviceTypeOtherAis_arr.get(i).getName())
//                            }
//                            adapter = ArrayAdapter<String>(
//                                requireContext(),
//                                R.layout.simple_custom_spinner_item,
//                                arr_device_types
//                            )
//                            adapter = activity?.let{ArrayAdapter<String>(it, R.layout.simple_custom_spinner_item, arr_device_types)}
//                            adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            binding!!.deviceType.setAdapter(adapter)
//
//                        } catch (npe: java.lang.NullPointerException) {
//                            npe.printStackTrace()
//                        }
//                    }
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                    try {
//                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show()
//                    } catch (e1: java.lang.Exception) {
//                        e1.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<MainResponse?>?, t: kotlin.Throwable?) {
//                t!!.printStackTrace()
//            }
//        })
//
//    }
//    override fun clientResponse(response: ClientResponse?) {}
//
//    override fun locationResponse(response: ClientLocationResponse?) {}
//
//    override fun workTypeResponse(response: WorkTypeResponse?) {}
//
//    override fun vehicleTypeResponse(response: VehicleTypeResponse?) {}
//
//    override fun replaceResponse(response: ReplaceReason?) {}
//
//    override fun disconnectionResponse(response: DisconnectionResponse?) {}
//
//    override fun removalActivityResponse(response: RemovalActivityResponse?) {}
//
//    override fun removalResponse(response: RemovalResponse?) {}
//
//    override fun faultListResponse(response: FaultResponse?) {}
//
//    override fun damageResponse(response: RemovalResponse?) {}
//
//    override fun collectItemResponse(response: CollectedItemsResponse?) {}
//
//    override fun simOperatorResponse(response: SimOperatorResponse?) {}
//
//    override fun simReplaceReason(response: SimReplaceResponse?) {}
//
//    override fun notAvailActivity(response: NotAvailActivityResponse?) {}
//
//    override fun vehicleNotAvailReason(response: VehNotAvailReasonResponse?) {}
//
//    override fun vtsResponses(response: VTSResponse?) {}
//
//    override fun vtsResponse(response: VTSResponse?) {}
//
//    override fun pMethod(response: PaymentMethodResponse?) {}
//
//    override fun updateDataResponse(response: MainResponse?) {}
//
//    override fun mainClientResponse(response: MainResponse?) {
//
//        if(response!!.type==1) {
//            try {
//                mainclientList = response.getMain_client_list()
//                try  {
//                    mainClientDetail.clear()
//                }catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                }
//                mainClientDetail.add("SELECT CLIENT")
//                for (i in mainclientList.indices)  {
//                    mainClientDetail.add(mainclientList.get(i)!!.getClient_Name())
//                }
//                adapter = activity?.let{ArrayAdapter<String>(it, R.layout.simple_custom_spinner_item, mainClientDetail)}
//                adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding!!.newMainClients.setAdapter(adapter)
//        }catch (e: java.lang.Exception) {
//                e.printStackTrace()
//        }
//        } else {
////            binding!!.recyclerView.setVisibility(View.GONE)
////            binding!!.txtContentUnavailable.setVisibility(View.VISIBLE)
////            binding!!.txtContentUnavailableSr.setVisibility(View.VISIBLE)
//        }
//    }
//
//    override fun vtsAccResponses(response: MainResponse?) {}
//
//    override fun receiveDeviceResponse(response: MainResponse?){}
//    override fun receiveDispatchMaterial(response: MainResponse?){
//        if (response!!.getType() == 1) {
//            try  {
//                itemList = response.getItems_list()
//                try  {
//                    try  {
//                        itemDetails.clear()
//                    }catch (e: java.lang.Exception) {
//                        e.printStackTrace()
//                    }
//                    itemDetails.add(" SELECT ITEMS")
//                    for (i in itemList.indices)  {
//                        itemDetails.add(itemList.get(i).getName())
//                    }
//                    adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_custom_spinner_item, itemDetails)
//                    adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    binding!!.accessorySpinner.setAdapter(adapter)
//                }catch (npe: java.lang.NullPointerException) {
//                    npe.printStackTrace()
//                }
//            }catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            Toast.makeText(getActivity(), "" + response.getMsg(), Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun returnDeviceresponse(response: MainResponse?){}
//
//    override fun dispatchFromTechResponse(response: MainResponse?){}
//
//}