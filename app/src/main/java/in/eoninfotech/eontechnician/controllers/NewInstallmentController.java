package in.eoninfotech.eontechnician.controllers;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.Responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.Responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.callbacks.ClientListener;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
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
    Call<RemovalResponse>damageResponseCall;
    Call<CollectedItemsResponse>collectedItemsCall;
    Call<SimOperatorResponse>simOperatorCall;
    Call<SimReplaceResponse>simReplaceCall;
    Call<NotAvailActivityResponse>notAvailActivityCall;
    Call<VehNotAvailReasonResponse>notAvailReasonCall;
    Call<VTSResponse>vtsResponseCall;
    Call<PaymentMethodResponse>pMethodCall;
    Call<MainResponse>updateDataCall;

    public void reqeuestClientList(ClientListener listener) {

        clientCall = client_att.reqeuestClientList();
        clientCall.enqueue(new Callback<ClientResponse>() {
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                listener.clientResponse(response.body());
            }
            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void reqeuestClientLocation(String clientId, ClientListener listener) {

        locCall = client_att.reqeuestClientLocation(clientId);
        locCall.enqueue(new Callback<ClientLocationResponse>() {
            @Override
            public void onResponse(Call<ClientLocationResponse> call, Response<ClientLocationResponse> response) {
                listener.locationResponse(response.body());
            }
            @Override
            public void onFailure(Call<ClientLocationResponse> call, Throwable t) {

                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                    // Toast.makeText(getActivity(), "Server Response Timeout, Try Again!", Toast.LENGTH_LONG).show();
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestDamageReason(ClientListener listener) {
        damageResponseCall = client_att.reqeuestDamageReason();
        damageResponseCall.enqueue(new Callback<RemovalResponse>() {
            @Override
            public void onResponse(Call<RemovalResponse> call, Response<RemovalResponse> response) {
                listener.damageResponse(response.body());
            }
            @Override
            public void onFailure(Call<RemovalResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void reqeuestVtsDetails(String vts_id,ClientListener listener) {
        vtsResponseCall = client_att.reqeuestVtsDetail(vts_id);
        vtsResponseCall.enqueue(new Callback<VTSResponse>() {
            @Override
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                listener.vtsResponses(response.body());
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void reqeuestVtsDetail(String vts_id,ClientListener listener) {
        vtsResponseCall = client_att.reqeuestVtsDetail(vts_id);
        vtsResponseCall.enqueue(new Callback<VTSResponse>() {
            @Override
            public void onResponse(Call<VTSResponse> call, Response<VTSResponse> response) {
                listener.vtsResponse(response.body());
            }
            @Override
            public void onFailure(Call<VTSResponse> call, Throwable t) {

                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
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
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

    public void postInstallationData(String worktype,
                                     String techName,
                                     String clientId,
                                     String c_loc_id,
                                     String regNo,
                                     String vehType,
                                     String devType,
                                     String oldVtsId,
                                     String newVtsId,
                                     String isDrs,
                                     String newDrsId,
                                     String oldDrsId,
                                     String drsDirection,
                                     String replaceReason,
                                     String removalReason,
                                     String itemsCollected,
                                     String others,
                                     String telSupport,
                                     String date,
                                     String time,
                                     String remarks,
                                     String dis_reason,
                                     String ignition_sensor,
                                     String fuel_sensor,
                                     String door_sensor,
                                     String veh_condition,
                                     String mgt_set,
                                     String sim_provider,
                                     String old_sim_no,
                                     String new_sim_no,
                                     String sim_reason,
                                     String not_available_reason,
                                     String not_available_activity,
                                     String is_demo,
                                     String missing_type,
                                     String collection_amount,
                                     String collection_date,
                                     String collection_type,
                                     String collectionImage,
                                     String missing_reason,
                                     String removal_type,
                                     String cut_off,
                                     String serial_no,
                                     String contact_person,
                                     String contact_no,
                                     String payment_type,
                                     String old_serial_no,
                                     String vts_device,
                                     ClientListener listener) {
        updateDataCall = client_att.postInstallationData(worktype,techName,clientId,c_loc_id,
                regNo,vehType,devType,oldVtsId,newVtsId,isDrs,newDrsId,oldDrsId,drsDirection,replaceReason,removalReason,itemsCollected,others, telSupport,date,time,remarks,dis_reason,ignition_sensor,fuel_sensor,door_sensor,
                veh_condition,mgt_set,sim_provider,old_sim_no,new_sim_no,sim_reason,not_available_reason,not_available_activity,is_demo,missing_type,collection_amount,collection_date,collection_type,collectionImage,
                missing_reason,removal_type,cut_off,serial_no,contact_person,contact_no,payment_type, old_serial_no, vts_device);
        updateDataCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.updateDataResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }
    public void postInstallationsData(RequestBody worktype,
                                      RequestBody techName,
                                      RequestBody clientId,
                                      RequestBody c_loc_id,
                                      RequestBody regNo,
                                      RequestBody vehType,
                                      RequestBody devType,
                                      RequestBody oldVtsId,
                                      RequestBody newVtsId,
                                      RequestBody isDrs,
                                      RequestBody newDrsId,
                                      RequestBody oldDrsId,
                                      RequestBody drsDirection,
                                      RequestBody replaceReason,
                                      RequestBody removalReason,
                                      RequestBody itemsCollected,
                                      RequestBody others,
                                      RequestBody telSupport,
                                      RequestBody date,
                                      RequestBody time,
                                      RequestBody remarks,
                                      RequestBody dis_reason,
                                      RequestBody ignition_sensor,
                                      RequestBody fuel_sensor,
                                      RequestBody door_sensor,
                                      RequestBody veh_condition,
                                      RequestBody mgt_set,
                                      RequestBody sim_provider,
                                      RequestBody old_sim_no,
                                      RequestBody new_sim_no,
                                      RequestBody sim_reason,
                                      RequestBody not_available_reason,
                                      RequestBody not_available_activity,
                                      RequestBody is_demo,
                                      RequestBody missing_type,
                                      RequestBody collection_amount,
                                      RequestBody collection_date,
                                      RequestBody collection_type,
                                      MultipartBody.Part collection_image,
                                      RequestBody missing_reason,
                                      RequestBody removal_type,
                                      RequestBody cut_off,
                                      RequestBody serial_no,
                                      RequestBody contact_person,
                                      RequestBody contact_no,
                                      RequestBody payment_type,
                                      RequestBody old_serial_no,
                                      RequestBody vts_device,
                                      ClientListener listener) {
        updateDataCall = client_att.postInstallationsData(worktype,techName,clientId,c_loc_id,regNo,vehType,devType,oldVtsId,newVtsId, isDrs,newDrsId,oldDrsId,drsDirection, replaceReason,removalReason, itemsCollected,others, telSupport,date,time,remarks,
                dis_reason,ignition_sensor,fuel_sensor,door_sensor, veh_condition,mgt_set,sim_provider,old_sim_no,new_sim_no, sim_reason,not_available_reason,not_available_activity,is_demo,missing_type,collection_amount,collection_date,collection_type,collection_image,
                missing_reason,removal_type,cut_off,serial_no,contact_person,contact_no,payment_type, old_serial_no, vts_device);
        updateDataCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                listener.updateDataResponse(response.body());
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                try {
                    TSnackbar snackbar = TSnackbar.make(v, "Server Response Timeout, Try Again!", TSnackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
                    TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                } catch (Exception e) {
                }
            }
        });
    }

}

