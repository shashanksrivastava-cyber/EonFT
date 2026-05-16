package in.eoninfotech.eontechnician.controllers;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import in.eoninfotech.eontechnician.R;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.DamageResponse;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnicianactivity.DeviceCountDetailAdapter;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 11/1/19.
 */

public class NewInstallmentController extends Controller {

    String version;
    View v;
    ApiHolder client_att = ServiceConnectionNewURL.getClient(version).create(ApiHolder.class);
    Call<ClientResponse> clientCall;
    Call<ClientLocationResponse> locCall;
    Call<WorkTypeResponse>workCall;
    Call<VehicleTypeResponse>vehCall;
    Call<FaultResponse>faultCall;
    Call<ReplaceReason>replaceCall;
    Call<DisconnectionResponse>discCall;
    Call<RemovalActivityResponse>removeActivityCall;
    Call<RemovalResponse>removalResponseCall;
    Call<DamageResponse>damageResponseCall;
    Call<CollectedItemsResponse>collectedItemsCall;
    Call<SimOperatorResponse>simOperatorCall;
    Call<SimReplaceResponse>simReplaceCall;
    Call<NotAvailActivityResponse>notAvailActivityCall;
    Call<VehNotAvailReasonResponse>notAvailReasonCall;
    Call<VTSResponse>vtsResponseCall;
    Call<PaymentMethodResponse>pMethodCall;
    Call<MainResponse>updateDataCall;
    Call<MainResponse>mainCustCall;
    Call<MainResponse>liveDeviceCountCall;

    public void reqeuestClientList(String c_id,ClientListener listener) {

        clientCall = client_att.reqeuestClientList(c_id);
        clientCall.enqueue(new Callback<ClientResponse>() {
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                listener.clientResponse(response.body());
            }
            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void reqeuestMainClientList(ClientListener listener) {

        mainCustCall = client_att.reqeuestMainClientList();
        mainCustCall.enqueue(new Callback<MainResponse>() {
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.mainClientResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void reqeuestClientLocation(String clientId,String server,String dbname, ClientListener listener) {

        locCall = client_att.reqeuestClientLocation(clientId,server,dbname);
        locCall.enqueue(new Callback<ClientLocationResponse>() {
            @Override
            public void onResponse(Call<ClientLocationResponse> call, Response<ClientLocationResponse> response) {
                listener.locationResponse(response.body());
            }
            @Override
            public void onFailure(Call<ClientLocationResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestworkType(ClientListener listener) {
        workCall = client_att.reqeuestworkType();
        workCall.enqueue(new Callback<WorkTypeResponse>() {
            @Override
            public void onResponse(Call<WorkTypeResponse> call, Response<WorkTypeResponse> response) {
                listener.workTypeResponse(response.body());
            }
            @Override
            public void onFailure(Call<WorkTypeResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void reqeuestvehicleType(ClientListener listener) {
        vehCall = client_att.reqeuestvehicleType();
        vehCall.enqueue(new Callback<VehicleTypeResponse>() {
            @Override
            public void onResponse(Call<VehicleTypeResponse> call, Response<VehicleTypeResponse> response) {
                listener.vehicleTypeResponse(response.body());
            }
            @Override
            public void onFailure(Call<VehicleTypeResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void reqeuestFaultList(ClientListener listener) {
        faultCall = client_att.reqeuestFaultList();
        faultCall.enqueue(new Callback<FaultResponse>() {
            @Override
            public void onResponse(Call<FaultResponse> call, Response<FaultResponse> response) {
                listener.faultListResponse(response.body());
            }
            @Override
            public void onFailure(Call<FaultResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void reqeuestReplaceReason(ClientListener listener) {
        replaceCall = client_att.reqeuestReplaceReason();
        replaceCall.enqueue(new Callback<ReplaceReason>() {
            @Override
            public void onResponse(Call<ReplaceReason> call, Response<ReplaceReason> response) {
                listener.replaceResponse(response.body());
            }
            @Override
            public void onFailure(Call<ReplaceReason> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void reqeuestDisconnection(ClientListener listener) {
        discCall = client_att.reqeuestDisconnection();
        discCall.enqueue(new Callback<DisconnectionResponse>() {
            @Override
            public void onResponse(Call<DisconnectionResponse> call, Response<DisconnectionResponse> response) {
                listener.disconnectionResponse(response.body());
            }
            @Override
            public void onFailure(Call<DisconnectionResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void requestRemovalActivityResponse(ClientListener listener) {
        removeActivityCall = client_att.requestRemovalActivityResponse();
        removeActivityCall.enqueue(new Callback<RemovalActivityResponse>() {
            @Override
            public void onResponse(Call<RemovalActivityResponse> call, Response<RemovalActivityResponse> response) {
                listener.removalActivityResponse(response.body());
            }
            @Override
            public void onFailure(Call<RemovalActivityResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestRemovalReason(ClientListener listener) {
        removalResponseCall = client_att.reqeuestRemovalReason();
        removalResponseCall.enqueue(new Callback<RemovalResponse>() {
            @Override
            public void onResponse(Call<RemovalResponse> call, Response<RemovalResponse> response) {
                listener.removalResponse(response.body());
            }
            @Override
            public void onFailure(Call<RemovalResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void reqeuestDamageReason(ClientListener listener) {
        damageResponseCall = client_att.reqeuestDamageReason();
        damageResponseCall.enqueue(new Callback<DamageResponse>() {
            @Override
            public void onResponse(Call<DamageResponse> call, Response<DamageResponse> response) {
                listener.damageResponse(response.body());
            }
            @Override
            public void onFailure(Call<DamageResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void reqeuestCollectedItemList(ClientListener listener) {
        collectedItemsCall = client_att.reqeuestCollectedItemList();
        collectedItemsCall.enqueue(new Callback<CollectedItemsResponse>() {
            @Override
            public void onResponse(Call<CollectedItemsResponse> call, Response<CollectedItemsResponse> response) {
                listener.collectItemResponse(response.body());
            }
            @Override
            public void onFailure(Call<CollectedItemsResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void reqeuestSimOperatorResponse(ClientListener listener) {
        simOperatorCall = client_att.reqeuestSimOperatorResponse();
        simOperatorCall.enqueue(new Callback<SimOperatorResponse>() {
            @Override
            public void onResponse(Call<SimOperatorResponse> call, Response<SimOperatorResponse> response) {
                listener.simOperatorResponse(response.body());
            }
            @Override
            public void onFailure(Call<SimOperatorResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestSimReplaceResponse(ClientListener listener) {
        simReplaceCall = client_att.reqeuestSimReplaceResponse();
        simReplaceCall.enqueue(new Callback<SimReplaceResponse>() {
            @Override
            public void onResponse(Call<SimReplaceResponse> call, Response<SimReplaceResponse> response) {
                listener.simReplaceReason(response.body());
            }
            @Override
            public void onFailure(Call<SimReplaceResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void notAvailActivityResponse(ClientListener listener) {
        notAvailActivityCall = client_att.notAvailActivityResponse();
        notAvailActivityCall.enqueue(new Callback<NotAvailActivityResponse>() {
            @Override
            public void onResponse(Call<NotAvailActivityResponse> call, Response<NotAvailActivityResponse> response) {
                listener.notAvailActivity(response.body());
            }
            @Override
            public void onFailure(Call<NotAvailActivityResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void requestVehNotAvailReasonResponse(ClientListener listener) {
        notAvailReasonCall = client_att.requestVehNotAvailReasonResponse();
        notAvailReasonCall.enqueue(new Callback<VehNotAvailReasonResponse>() {
            @Override
            public void onResponse(Call<VehNotAvailReasonResponse> call, Response<VehNotAvailReasonResponse> response) {
                listener.vehicleNotAvailReason(response.body());
            }
            @Override
            public void onFailure(Call<VehNotAvailReasonResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestVtsDetails(String vts_id,String server,String dbname,ClientListener listener) {
        vtsResponseCall = client_att.reqeuestVtsDetail(vts_id,server,dbname);
        vtsResponseCall.enqueue(new Callback<VTSResponse>() {
            @Override
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                listener.vtsResponses(response.body());
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void reqeuestVtsDetail(String vts_id,String server,String dbname,ClientListener listener) {
        vtsResponseCall = client_att.reqeuestVtsDetail(vts_id,server,dbname);
        vtsResponseCall.enqueue(new Callback<VTSResponse>() {
            @Override
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                listener.vtsResponse(response.body());
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestPMethod(ClientListener listener) {
        pMethodCall = client_att.reqeuestPMethod();
        pMethodCall.enqueue(new Callback<PaymentMethodResponse>() {
            @Override
            public void onResponse(Call<PaymentMethodResponse> call, Response<PaymentMethodResponse> response) {
                listener.pMethod(response.body());
            }
            @Override
            public void onFailure(Call<PaymentMethodResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void postInstallationsData(RequestBody technician_id,
                                      RequestBody activity_date,
                                      RequestBody activity_time,
                                      RequestBody customer,
                                      RequestBody customer_location,
                                      RequestBody is_demo,
                                      RequestBody activity_type,
                                      RequestBody vts_type,
                                      RequestBody device_type,
                                      RequestBody old_device_id,
                                      RequestBody new_device_id,
                                      RequestBody old_serial_no,
                                      RequestBody new_serial_no,
                                      RequestBody reg_no,
                                      RequestBody veh_type,
                                      RequestBody is_drs,
                                      RequestBody old_drs,
                                      RequestBody new_drs,
                                      RequestBody drs_direction,
                                      RequestBody mgt_set,
                                      RequestBody ignition_sensor,
                                      RequestBody fuel_sensor,
                                      RequestBody door_sensor,
                                      RequestBody panic_button,
                                      RequestBody cut_off,
                                      RequestBody replacement_reason,
                                      RequestBody removal_type,
                                      RequestBody removal_reason,
                                      RequestBody disconnection_reason,
                                      RequestBody missing_type,
                                      RequestBody missing_reason,
                                      RequestBody not_available_activity,
                                      RequestBody not_available_reason,
                                      RequestBody collection_date,
                                      RequestBody payment_method,
                                      RequestBody amount,
                                      RequestBody payment_type,
                                      RequestBody contact_person,
                                      RequestBody contact_no,
                                      RequestBody sim_provider,
                                      RequestBody old_sim_no,
                                      RequestBody new_sim_no,
                                      RequestBody sim_reason,
                                      RequestBody veh_condition,
                                      RequestBody tech_remarks,
                                      RequestBody collected_items,
                                      RequestBody faults_checked,
                                      RequestBody fuel_reading,
                                      RequestBody lid_status,
                                      RequestBody trans_receiver,
                                      RequestBody temp_sensor,
                                      RequestBody tilt_sensor,
                                      RequestBody fuel_status,
                                      RequestBody panic_status,
                                      RequestBody sensor_veh_no,
                                      RequestBody sensor_old_veh_no,
                                      RequestBody remove_type,
                                      RequestBody drs_status,
                                      RequestBody replacetype,
                                      RequestBody device_working_status,
                                      RequestBody sensor_working_status,
                                      RequestBody main_client_id,
                                      RequestBody cust_type,
                                      MultipartBody.Part image,
                                      ClientListener listener) {
        updateDataCall = client_att.postInstallationsData(technician_id,activity_date,activity_time,customer,customer_location,is_demo,activity_type,vts_type,device_type,old_device_id,new_device_id,old_serial_no,new_serial_no,reg_no,veh_type,is_drs,old_drs,new_drs,drs_direction,mgt_set,ignition_sensor,fuel_sensor,door_sensor,panic_button,cut_off,replacement_reason,removal_type,removal_reason,disconnection_reason,missing_type,missing_reason,not_available_activity,not_available_reason,collection_date,payment_method,amount,
                payment_type,contact_person,contact_no,sim_provider,old_sim_no,new_sim_no,sim_reason,veh_condition,tech_remarks,collected_items,faults_checked,fuel_reading,lid_status,trans_receiver,temp_sensor,tilt_sensor,fuel_status,panic_status,sensor_veh_no,sensor_old_veh_no,remove_type,drs_status,replacetype,device_working_status,sensor_working_status,main_client_id,cust_type,image);
        updateDataCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.updateDataResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

//    public void getVehforUM(String client_id,String loc_id,ClientListener listener) {
//        updateDataCall = client_att.get_veh_for_um(client_id,loc_id);
//        updateDataCall.enqueue(new Callback<MainResponse>() {
//            @Override
//            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
//                listener.updateDataResponse(response.body());
//            }
//            @Override
//            public void onFailure(Call<MainResponse> call, Throwable t) {
//
//                try {
//                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
//                    View snackbarView = snackbar.getView();
//                    snackbarView.setBackgroundColor(Color.RED);
//                    TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
//                    textView.setTextColor(Color.WHITE);
//                    snackbar.show();
//                } catch (Exception e) {
//                }
//            }
//        });
//    }

    public void get_veh_for_pm(String client_id,String loc_id,ClientListener listener) {
        updateDataCall = client_att.get_veh_for_pm(client_id,loc_id);
        updateDataCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.updateDataResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {

                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestAccVtsDetails(String dbname,String server,String reg_no,ClientListener listener) {
        updateDataCall = client_att.get_serial_no(dbname,server,reg_no);
        updateDataCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.vtsAccResponses(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void requestLiveDeviceCount(String user_name,String status, String customer, ClientListener listener) {
        liveDeviceCountCall = client_att.get_live_device_count_details(user_name,status,customer);
        liveDeviceCountCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.updateDataResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    Snackbar snackbar = Snackbar.make(v, "Server Response Timeout, Try Again!", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = snackbarView.findViewById(R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
}

