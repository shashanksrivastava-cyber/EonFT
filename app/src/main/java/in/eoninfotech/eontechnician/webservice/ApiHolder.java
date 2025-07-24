package in.eoninfotech.eontechnician.webservice;


import java.util.List;

import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.responses.AttendanceResponse;
import in.eoninfotech.eontechnician.responses.CallSheetListResponse;
import in.eoninfotech.eontechnician.responses.CallSheetResponse;
import in.eoninfotech.eontechnician.responses.ClientDataResponse;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.CollectionReportResponse;
import in.eoninfotech.eontechnician.responses.DRSResponse;
import in.eoninfotech.eontechnician.responses.DashBoardResponse;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.FaultyDevices;
import in.eoninfotech.eontechnician.responses.IncentiveResponse;
import in.eoninfotech.eontechnician.responses.InstInstructionResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.LogResponse;
import in.eoninfotech.eontechnician.responses.LoginResponse;
import in.eoninfotech.eontechnician.responses.MonthListResponse;
import in.eoninfotech.eontechnician.responses.MyPojo;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.StockResponse;
import in.eoninfotech.eontechnician.responses.TechResponse;
import in.eoninfotech.eontechnician.responses.TechnicianMonthResponse;
import in.eoninfotech.eontechnician.responses.UnderMaintenanceResponse;
import in.eoninfotech.eontechnician.responses.UpdateDataResponse;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;
import in.eoninfotech.eontechnician.responses.YearListResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/***************************************************************************/
// Copyright EON Infotech Ltd., published work, created 2017.          //
// This computer program includes Confidential, Proprietary information  //
// and is a trade secret of EON Infotech Ltd. All use, disclosure and/or //
// reproduction is prohibited unless authorised in writing by an         //
// authorised officer of EON Infotech Ltd. All rights reserved.          //

/**************************************************************************/
public interface ApiHolder {

    @FormUrlEncoded
    @POST("bill-detail.php")
    Call<UpdateDataResponse> view_bill(@Field("order_no") String order_no);

    @Multipart
    @POST("call-sheet.php")
    Call<UpdateDataResponse> call_sheet(@Part MultipartBody.Part image,
                                        @Part("tech_name") RequestBody tech_name,
                                        @Part("date") RequestBody date,
                                        @Part("remarks") RequestBody remark);

    @Multipart
    @POST("view-call-sheets.php")
    Call<CallSheetResponse> callsheet_list(@Part("tech_name") RequestBody tech_name,
                                           @Part("month") RequestBody month,
                                           @Part("year") RequestBody year);

    @Multipart
    @POST("month-call-sheets.php")
    Call<CallSheetListResponse> callsheetlist(@Part("tech_name") RequestBody tech_name,
                                              @Part("month") RequestBody month,
                                              @Part("year") RequestBody year);

    @FormUrlEncoded
    @POST("change-password.php")
    Call<UpdateDataResponse> change_pwd(@Field("user_name") String tech_name,
                                        @Field("old_password") String old_pwd,
                                        @Field("new_password") String new_pwd);

    @Multipart
    @POST("device_old.php")
    Call<UpdateDataResponse> uploadFile(
            @Part MultipartBody.Part image,
            @Part("tech_name") RequestBody tech_name,
            @Part("vts_old") RequestBody vts_old,
            @Part("vts_new") RequestBody vts_new,
            @Part("drs_new") RequestBody drs_new,
            @Part("drs_old") RequestBody drs_old,
            @Part("client_id") RequestBody clint_id,
            @Part("reason_replacement") RequestBody reason_replace,
            @Part("remarks") RequestBody remarks);

    @FormUrlEncoded
    @POST("device_other.php")
    Call<UpdateDataResponse> device_other(
            @Field("tech_name") String tech_name,
            @Field("vts_old") String vts_old,
            @Field("change_value") String change_value,
            @Field("remarks") String remarks,
            @Field("client_id") String client_id,
            @Field("tel_support") String tel_support);

    @FormUrlEncoded
    @POST("eon_details.php")
    Call<UpdateDataResponse> critical_sites(@Field("tech_name") String tech_name,
                                            @Field("key") String key);

    @FormUrlEncoded
    @POST("incentive.php")
    Call<UpdateDataResponse> incentive(@Field("tech_name") String tech_name,
                                       @Field("key") String key);

    @FormUrlEncoded
    @POST("list-incentives.php")
    Call<IncentiveResponse> incentive_list(@Field("key") String key);

    @FormUrlEncoded
    @POST("mark-attendance.php")
    Call<AttResponse> log_attendance(
            @Field("tech_name") String username,
            @Field("time") String time,
            @Field("date") String date,
            @Field("latitude") String lati,
            @Field("longitude") String longi,
            @Field("address") String address,
            @Field("log_status") String log_,
            @Field("version") String version,
            @Field("remarks") String remarks,
            @Field("client_id") String client_id,
            @Field("client_loc") String client_loc);

    @FormUrlEncoded
    @POST("stock-report.php")
    Call<StockResponse> stock_report(@Field("tech_name") String tech_name,
                                     @Field("date") String date);

    @FormUrlEncoded
    @POST("update_installation.php")
    Call<UpdateDataResponse> update_requirement(@Field("sale_id") String sale_id,
                                                @Field("sales_person_name") String username,
                                                @Field("date") String date,
                                                @Field("order_no") String order_no,
                                                @Field("install_type") String install_type,
                                                @Field("duration") String duration,
                                                @Field("cust_type") String cust_type,
                                                @Field("cust_name") String cust_name,
                                                @Field("cust_street_name") String cust_street_name,
                                                @Field("cust_city") String cust_city,
                                                @Field("cust_office_number") String cust_office_number,
                                                @Field("cust_district") String cust_district,
                                                @Field("cust_state") String cust_state,
                                                @Field("cust_pincode") String cust_pincode,
                                                @Field("cust_p_name") String cust_p_name,
                                                @Field("cust_p_number") String cust_p_number,
                                                @Field("cust_p_id") String cust_p_id,
                                                @Field("cust_s_name") String cust_s_name,
                                                @Field("cust_s_number") String cust_s_number,
                                                @Field("cust_s_id") String cust_s_id,
                                                @Field("install_street_name") String install_street_name,
                                                @Field("install_city") String install_city,
                                                @Field("install_office_number") String install_office_number,
                                                @Field("install_district") String install_district,
                                                @Field("install_state") String install_state,
                                                @Field("install_pincode") String install_pincode,
                                                @Field("vehicle_type") String vehicle_type,
                                                @Field("battery_voltage") String battery_voltage,
                                                @Field("vts_quantity") String vts_quantity,
                                                @Field("accessory") String accessory,
                                                @Field("drs_quantity") String drs_quantity,
                                                @Field("fuel_sensor_quantity") String fuel_sensor_quantity,
                                                @Field("door_sensor_quantity") String door_sensor_quantity,
                                                @Field("priority_reason") String priority_rsn,
                                                @Field("remarks") String remarks,
                                                @Field("ignition") String ignition,
                                                @Field("high_count") String high_count,
                                                @Field("normal_count") String normal_count,
                                                @Field("low_count") String low_count);

    @FormUrlEncoded
    @POST("view_tech_plan.php")
    Call<UpdateDataResponse> view_next_plan(@Field("tech_name") String tech_name,
                                            @Field("key") String key);

    @FormUrlEncoded
    @POST("vts_installation.php")
    Call<UpdateDataResponse> vts_requirement(@Field("sales_person_name") String username,
                                             @Field("date") String date,
                                             @Field("order_no") String order_no,
                                             @Field("install_type") String install_type,
                                             @Field("duration") String duration,
                                             @Field("cust_type") String cust_type,
                                             @Field("cust_name") String cust_name,
                                             @Field("cust_street_name") String cust_street_name,
                                             @Field("cust_city") String cust_city,
                                             @Field("cust_office_number") String cust_office_number,
                                             @Field("cust_district") String cust_district,
                                             @Field("cust_state") String cust_state,
                                             @Field("cust_pincode") String cust_pincode,
                                             @Field("cust_p_name") String cust_p_name,
                                             @Field("cust_p_number") String cust_p_number,
                                             @Field("cust_p_id") String cust_p_id,
                                             @Field("cust_s_name") String cust_s_name,
                                             @Field("cust_s_number") String cust_s_number,
                                             @Field("cust_s_id") String cust_s_id,
                                             @Field("install_street_name") String install_street_name,
                                             @Field("install_city") String install_city,
                                             @Field("install_office_number") String install_office_number,
                                             @Field("install_district") String install_district,
                                             @Field("install_state") String install_state,
                                             @Field("install_pincode") String install_pincode,
                                             @Field("vehicle_type") String vehicle_type,
                                             @Field("battery_voltage") String battery_voltage,
                                             @Field("vts_quantity") String vts_quantity,
                                             @Field("accessory") String accessory,
                                             @Field("drs_quantity") String drs_quantity,
                                             @Field("fuel_sensor_quantity") String fuel_sensor_quantity,
                                             @Field("door_sensor_quantity") String door_sensor_quantity,
                                             @Field("priority_reason") String priority_rsn,
                                             @Field("remarks") String remarks,
                                             @Field("ignition") String ignition,
                                             @Field("high_count") String high_count,
                                             @Field("normal_count") String normal_count,
                                             @Field("low_count") String low_count);

    @GET("activities-list.php")
    Call<WorkTypeResponse> reqeuestworkType();

//    @GET("activities-list-test.php")
//    Call<WorkTypeResponse> reqeuestworkType();

    @GET("vehicle-type.php")
    Call<VehicleTypeResponse> reqeuestvehicleType();

//    @FormUrlEncoded
//    @POST("vehicle-type.php")
//    Call<VehicleTypeResponse> reqeuestvehicleType(
//            @Field("cust_id") String cust_id,
//            @Field("cust_name") String cust_name);

    @GET("main_client_list.php")
    Call<MainResponse> reqeuestMainClientList();
    @FormUrlEncoded
    @POST("clients-list.php")
    Call<ClientResponse> reqeuestClientList(
            @Field("c_id") String c_id);

//    @FormUrlEncoded
//    @POST("clients-list-test.php")
//    Call<ClientResponse> reqeuestClientList(
//            @Field("c_id") String c_id);

    @GET("stock-clients.php")
    Call<ClientResponse> reqeuestStockClientList();

    @GET("eon-technicians.php")
    Call<TechResponse> requestTechList();

    @GET("fault-list.php")
    Call<FaultResponse> reqeuestFaultList();

    @GET("collected-items.php")
    Call<CollectedItemsResponse> reqeuestCollectedItemList();

    @GET("replacement-reasons.php")
    Call<ReplaceReason> reqeuestReplaceReason();

    @GET("removal-reasons.php")
    Call<RemovalResponse> reqeuestRemovalReason();

    @GET("damage-reasons.php")
    Call<RemovalResponse> reqeuestDamageReason();

    @GET("disconnection-reasons.php")
    Call<DisconnectionResponse> reqeuestDisconnection();

    @GET("payment-methods.php")
    Call<PaymentMethodResponse> reqeuestPMethod();

//    @FormUrlEncoded
//    @POST("client-locations.php")
//    Call<ClientLocationResponse> reqeuestClientLocation(
//            @Field("customer") String customer,
//            @Field("server") String server,
//            @Field("dbname") String dbname);

    @FormUrlEncoded
    @POST("client-locations1.php")
    Call<ClientLocationResponse> reqeuestClientLocation(
            @Field("customer") String customer,
            @Field("server") String server,
            @Field("dbname") String dbname);

    @FormUrlEncoded
    @POST("vts-detail.php")
    Call<VTSResponse> reqeuestVtsDetail(
            @Field("vid") String id,
            @Field("server") String server,
            @Field("dbname") String dbname);

    @FormUrlEncoded
    @POST("check-drs.php")
    Call<DRSResponse> reqeuestDrsDetail(
            @Field("id") String id);

    @Multipart
    @POST("technicians-work.php")
    Call<MainResponse> postInstallationsData(@Part("work_type") RequestBody worktype,
                                             @Part("tech_name") RequestBody techName,
                                             @Part("client_id") RequestBody clientId,
                                             @Part("client_loc_id") RequestBody c_loc_id,
                                             @Part("reg_no") RequestBody regNo,
                                             @Part("veh_type") RequestBody vehType,
                                             @Part("device_type") RequestBody devType,
                                             @Part("old_vts_id") RequestBody oldVtsId,
                                             @Part("new_vts_id") RequestBody newVtsId,
                                             @Part("is_drs") RequestBody isDrs,
                                             @Part("new_drs_id") RequestBody newDrsId,
                                             @Part("old_drs_id") RequestBody oldDrsId,
                                             @Part("drs_direction") RequestBody drsDirection,
                                             @Part("reason_replacement") RequestBody replaceReason,
                                             @Part("removal_reason") RequestBody removalReason,
                                             @Part("items_collected") RequestBody itemsCollected,
                                             @Part("others") RequestBody others,
                                             @Part("tel_support") RequestBody telSupport,
                                             @Part("activity_date") RequestBody date,
                                             @Part("activity_time") RequestBody time,
                                             @Part("remarks") RequestBody remarks,
                                             @Part("disconnection_reason") RequestBody dis_reason,
                                             @Part("ignition_sensor") RequestBody ignition_sensor,
                                             @Part("fuel_sensor") RequestBody fuel_sensor,
                                             @Part("door_sensor") RequestBody door_sensor,
                                             @Part("veh_condition") RequestBody veh_condition,
                                             @Part("mgt_set") RequestBody mgt_set,
                                             @Part("sim_provider") RequestBody sim_provider,
                                             @Part("old_sim_no") RequestBody old_sim_no,
                                             @Part("new_sim_no") RequestBody new_sim_no,
                                             @Part("sim_reason") RequestBody sim_reason,
                                             @Part("not_available_reason") RequestBody not_available_reason,
                                             @Part("not_available_activity") RequestBody not_available_activity,
                                             @Part("is_demo") RequestBody is_demo,
                                             @Part("missing_type") RequestBody missing_type,
                                             @Part("collection_amount") RequestBody collection_amount,
                                             @Part("collection_date") RequestBody collection_date,
                                             @Part("collection_type") RequestBody collection_type,
                                             @Part MultipartBody.Part collection_image,
                                             @Part("missing_reason") RequestBody missing_reason,
                                             @Part("removal_type") RequestBody removal_type,
                                             @Part("cut_off") RequestBody cut_off,
                                             @Part("serial_no") RequestBody serial_no,
                                             @Part("contact_person") RequestBody contact_person,
                                             @Part("contact_no") RequestBody contact_no,
                                             @Part("payment_type") RequestBody payment_type,
                                             @Part("old_serial_no") RequestBody old_serial_no,
                                             @Part("vts_type") RequestBody vts_type);

    @Multipart
    @POST("technicians-work.php")
    Call<MainResponse> postInstallationsData(@Part("technician_id") RequestBody technician_id,
                                             @Part("activity_date") RequestBody activity_date,
                                             @Part("activity_time") RequestBody activity_time,
                                             @Part("customer") RequestBody customer,
                                             @Part("customer_location") RequestBody customer_location,
                                             @Part("is_demo") RequestBody is_demo,
                                             @Part("activity_type") RequestBody activity_type,
                                             @Part("vts_type") RequestBody vts_type,
                                             @Part("device_type") RequestBody device_type,
                                             @Part("old_device_id") RequestBody old_device_id,
                                             @Part("new_device_id") RequestBody new_device_id,
                                             @Part("old_serial_no") RequestBody old_serial_no,
                                             @Part("new_serial_no") RequestBody new_serial_no,
                                             @Part("reg_no") RequestBody reg_no,
                                             @Part("veh_type") RequestBody veh_type,
                                             @Part("is_drs") RequestBody is_drs,
                                             @Part("old_drs") RequestBody old_drs,
                                             @Part("new_drs") RequestBody new_drs,
                                             @Part("drs_direction") RequestBody drs_direction,
                                             @Part("mgt_set") RequestBody mgt_set,
                                             @Part("ignition_sensor") RequestBody ignition_sensor,
                                             @Part("fuel_sensor") RequestBody fuel_sensor,
                                             @Part("door_sensor") RequestBody door_sensor,
                                             @Part("panic_button") RequestBody panic_button,
                                             @Part("cut_off") RequestBody cut_off,
                                             @Part("replacement_reason") RequestBody replacement_reason,
                                             @Part("removal_type") RequestBody removal_type,
                                             @Part("removal_reason") RequestBody removal_reason,
                                             @Part("disconnection_reason") RequestBody disconnection_reason,
                                             @Part("missing_type") RequestBody missing_type,
                                             @Part("missing_reason") RequestBody missing_reason,
                                             @Part("not_available_activity") RequestBody not_available_activity,
                                             @Part("not_available_reason") RequestBody not_available_reason,
                                             @Part("collection_date") RequestBody collection_date,
                                             @Part("payment_method") RequestBody payment_method,
                                             @Part("amount") RequestBody amount,
                                             @Part("payment_type") RequestBody payment_type,
                                             @Part("contact_person") RequestBody contact_person,
                                             @Part("contact_no") RequestBody contact_no,
                                             @Part("sim_provider") RequestBody sim_provider,
                                             @Part("old_sim_no") RequestBody old_sim_no,
                                             @Part("new_sim_no") RequestBody new_sim_no,
                                             @Part("sim_reason") RequestBody sim_reason,
                                             @Part("veh_condition") RequestBody veh_condition,
                                             @Part("tech_remarks") RequestBody tech_remarks,
                                             @Part("collected_items") RequestBody collected_items,
                                             @Part("faults_checked") RequestBody faults_checked,
                                             @Part("fuel_reading") RequestBody fuelreading,
                                             @Part("lid_status") RequestBody lid_status,
                                             @Part("trans_receiver") RequestBody trans_receiver,
                                             @Part("temp_sensor") RequestBody temp_sensor,
                                             @Part("tilt_sensor") RequestBody tilt_sensor,
                                             @Part("fuel_status") RequestBody fuel_status,
                                             @Part("panic_status") RequestBody panic_status,
                                             @Part("sensor_veh_no") RequestBody sensor_veh_no,
                                             @Part("sensor_old_veh_no") RequestBody sensor_old_veh_no,
                                             @Part("remove_type") RequestBody remove_type,
                                             @Part("drs_status") RequestBody drs_status,
                                             @Part("replace_type") RequestBody replace_type,
                                             @Part("device_working_status") RequestBody device_working_status,
                                             @Part("sensor_working_status") RequestBody sensor_working_status,
                                             @Part("main_client_id") RequestBody main_client_id,
                                             @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("faulty-vts.php")
    Call<UpdateDataResponse> faulty_vts(@Field("technician") String technician);

    @FormUrlEncoded
    @POST("view-activities.php")
    Call<ActivityResponse> view_activities(@Field("date") String date,
                                           @Field("technician_id") String technician_id);

    @GET("tech-of-month.php")
    Call<TechnicianMonthResponse> requestTechnicianoftheMonth();

    @FormUrlEncoded
    @POST("tech-dashboard.php")
    Call<DashBoardResponse> dashBoardResponse(@Field("zone") String zone);

    @FormUrlEncoded
    @POST("under-maintenance.php")
    Call<UnderMaintenanceResponse> underMainResponse(@Field("zone") String zone);

    @FormUrlEncoded
    @POST("under_maintenance_working.php")
    Call<UnderMaintenanceResponse> underMainWorkingResponse(@Field("zone") String zone);

    @FormUrlEncoded
    @POST("faulty_fuel.php")
    Call<FaultyDevices> faulty_fuel_response(@Field("zone") String zone);

    @FormUrlEncoded
    @POST("devices-detail.php")
    Call<FaultyDevices> faultyDevicesResponse(@Field("zone") String zone);

    @FormUrlEncoded
    @POST("faulty-drs-details.php")
    Call<FaultyDevices> faultyDRSResponse(@Field("zone") String zone);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> loginResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("imei_no") String imei_no,
            @Field("version_name") String version_name,
            @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("log-status.php")
    Call<LogResponse> logResponse(
            @Field("tech_name") String tech_name,
            @Field("date") String date);

    @GET("sim-replacement-reasons.php")
    Call<SimReplaceResponse> reqeuestSimReplaceResponse();

    @GET("sim-operators.php")
    Call<SimOperatorResponse> reqeuestSimOperatorResponse();

    @GET("not-available-reasons.php")
    Call<VehNotAvailReasonResponse> requestVehNotAvailReasonResponse();

    @GET("removal-activities.php")
    Call<RemovalActivityResponse> requestRemovalActivityResponse();

    @GET("not-available-activites.php")
    Call<NotAvailActivityResponse> notAvailActivityResponse();

    @GET("months-list.php")
    Call<MonthListResponse> monthResponse();

    @GET("years-list.php")
    Call<YearListResponse> yearResponse();

    @FormUrlEncoded
    @POST("check-faults.php")
    Call<MyPojo> readUserData(@Field("tech") String tech_name);

    @FormUrlEncoded
    @POST("pvt-clients.php")
    Call<MyPojo> readPvtData(@Field("tech") String tech_name);

    @FormUrlEncoded
    @POST("fault-messages.php")
    Call<InstInstructionResponse> instructionResponse(
            @Field("srv") String server,
            @Field("db") String database,
            @Field("cust_id") String cust_id,
            @Field("loc_id") String loc_id);

    @FormUrlEncoded
    @POST("stock-update.php")
    Call<MainResponse> stockResponse(
            @Field("client_id") String client_id,
            @Field("vts_wrk") String vts_wrk,
            @Field("vts_falt") String vts_falt,
            @Field("mtrs_7") String mtrs_7,
            @Field("mtrs_2") String mtrs_2,
            @Field("drs") String drs,
            @Field("mgt_set") String mgt_set,
            @Field("tech_name") String tech_name,
            @Field("y_cable") String y_cable,
            @Field("remarks") String remarks,
            @Field("vts_w_id") String vts_w_id,
            @Field("vts_f_id") String vts_f_id,
            @Field("drs_id") String drs_id,
            @Field("drs_f_id") String drs_f_id,
            @Field("drs_w_id") String drs_w_id);

    @FormUrlEncoded
    @POST("stock-client-data.php")
    Call<ClientDataResponse> reqeuestClientDataResponse(
            @Field("tech_name") String tech_name,
            @Field("client_id") String client_id);

    @FormUrlEncoded
    @POST("collection-report.php")
    Call<CollectionReportResponse> requestCollectionReportResponse(
            @Field("tech_name") String tech_name,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date);

    @FormUrlEncoded
    @POST("incentives.php")
    Call<IncentiveResponse> incentiveResponse(
            @Field("tech_id") String tech_id,
            @Field("tech_name") String tech_name,
            @Field("zone") String zone,
            @Field("month") String month,
            @Field("year") String year);

    @FormUrlEncoded
    @POST("atd-summary.php")
    Call<AttendanceResponse> attendanceResponse(
            @Field("tech_name") String tech_name,
            @Field("month") String month,
            @Field("year") String year);

    @FormUrlEncoded
    @POST("locations.php")
    Call<LocationsResponse> locationResponse(@Field("tech_name") String tech_name,
                                             @Field("location") String location,
                                             @Field("imei_no") String imei_no,
                                             @Field("latitude") String latitude,
                                             @Field("longitude") String longitude,
                                             @Field("mac_address") String mac_address);

    @FormUrlEncoded
    @POST("tracking-details.php")
    Call<TrackingResponse> trackingResponse(
            @Field("username") String username,
            @Field("imei_no") String imei_no);

    @FormUrlEncoded
    @POST("messages.php")
    Call<MessageResponse> messageResponse(
            @Field("tech_name") String tech_name,
            @Field("date") String date,
            @Field("status") String status,
            @Field("msg_type") String msg_type);

    @FormUrlEncoded
    @POST("update-message-status.php")
    Call<MainResponse> updateResponse(
            @Field("message_id") String message_id);

    @GET("device-type.php")
        // response to be changed
    Call<MainResponse> getDeviceTypes();

    @GET("vts-type.php")
    Call<VTSTypeResponse> getVTSTypes();

    @GET("getFaq.php")
    Call<FaqResponse> getFaqData();

    @FormUrlEncoded
    @POST("get_vehicle_for_um.php")
    Call<UmVehicleResponse> get_veh_for_um(@Field("client_id") String client_id,
                                      @Field("loc_id") String loc_id);

    @GET("get_veh_for_pm.php")
    Call<MainResponse> get_veh_for_pm(@Field("client_id") String client_id,
                                      @Field("loc_id") String loc_id);

    @Multipart
    @POST("submit_bill.php")
    Call<MainResponse> submit_bill(@Part("user_id") RequestBody user_id,
                                   @Part("from_date") RequestBody from_date,
                                   @Part("to_date") RequestBody to_date,
                                   @Part("amount") RequestBody amount,
                                   @Part("remarks") RequestBody remarks,
                                   @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("bill_details.php")
    Call<BillResponse> get_bill_details(@Field("user_id") String user_id,
                                   @Field("from_date") String from_date,
                                   @Field("to_date") String to_date,
                                   @Field("status") String status);

//    @FormUrlEncoded
//    @POST("bill_details.php")
//    Observable<List<BillResponse>> get_bill_details(@Field("user_id") String user_id,
//                                                    @Field("from_date") String from_date,
//                                                    @Field("to_date") String to_date,
//                                                    @Field("status") String status);

    @FormUrlEncoded
    @POST("cancelled_bill.php")
    Call<BillResponse> cancel_bill(@Field("bill_no") String bill_no,
                                   @Field("remarks") String remarks);

    @GET("get_dispatch_type_list.php")
    Call<MainResponse> get_dispatch_type_list();

    @FormUrlEncoded
    @POST("get_dispatch_material_status.php")
    Call<MainResponse> get_dispatch_material_status(
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("status") String status,
            @Field("tech_id") String tech_id);

    @FormUrlEncoded
    @POST("get_dispatch_detailed_list.php")
    Call<MainResponse> get_dispatched_device(
            @Field("dispatch_id") String dispatch_id,
            @Field("transit_through") String transit_through,
            @Field("tech_id") String tech_id,
            @Field("main_id") String main_id,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("receive_dispatch_material.php")
    Call<MainResponse> receive_dispatched_material(
            @Field("dispatch_id") String dispatch_id,
            @Field("challan_id") String challan_id,
            @Field("tech_id") String tech_id,
            @Field("items_collected") String items_collected,
            @Field("accessories_collected") String accessories_collected,
            @Field("remarks") String remarks);

    @GET("get_item_list.php")
    Call<MainResponse> get_item_list();

    @FormUrlEncoded
    @POST("return_material.php")
    Call<MainResponse> return_material(
            @Field("tech_id") String tech_id,
            @Field("pcb_sr_no") String pcb_sr_no,
            @Field("item_qty") String item_qty,
            @Field("transit_type") String transit_type,
            @Field("transit_name") String transit_name,
            @Field("transit_through") String transit_through,
            @Field("remarks") String remarks,
            @Field("other_tech_id") String other_tech_id);

    @FormUrlEncoded
    @POST("get_removed_device_list.php")
    Call<MainResponse> removed_device_list(
            @Field("tech_id") String tech_id);

    @FormUrlEncoded
    @POST("get_activity_serial_no.php")
    Call<MainResponse> get_serial_no(
            @Field("tech_id") String tech_id,
            @Field("customer") String customer,
            @Field("sub_cust") String sub_cust,
            @Field("activity_type") String activity_type,
            @Field("dbname") String dbname,
            @Field("server") String server);

    @FormUrlEncoded
    @POST("get_live_status_report.php")
    Call<MainResponse> get_live_status(
            @Field("server") String server,
            @Field("dbname") String dbname,
            @Field("dist_id") String dist_id,
            @Field("depo_id") String depo_id,
            @Field("status") String status,
            @Field("type") String type);


    @FormUrlEncoded
    @POST("get_live_status_report_eon.php")
    Call<MainResponse> get_live_status_report(
            @Field("server") String server,
            @Field("dbname") String dbname,
            @Field("dist_id") String dist_id,
            @Field("depo_id") String depo_id,
            @Field("status") String status,
            @Field("type") String type,
            @Field("veh_serial_no") String veh_serial_no);

    @FormUrlEncoded
    @POST("get_return_material_status.php")
    Call<MainResponse> get_return_material_status(
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("status") String status,
            @Field("tech_id") String tech_id);

    @FormUrlEncoded
    @POST("get_tech_returned_device_details.php")
    Call<MainResponse> get_tech_returned_device_details(
            @Field("id_no") String id_no);

    @FormUrlEncoded
    @POST("get_device_count_details.php")
    Call<MainResponse> get_device_count_details(
            @Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("get_serial_no.php")
    Call<MainResponse> get_serial_no(
            @Field("dbname") String dbname,
            @Field("server") String server,
            @Field("regno") String regno);

    @FormUrlEncoded
    @POST("get_live_device_count.php")
    Call<MainResponse> get_live_device_count(
            @Field("user_name") String user_name);

    @FormUrlEncoded
    @POST("get_live_device_count_details.php")
    Call<MainResponse> get_live_device_count_details(
            @Field("user_name") String user_name,
            @Field("status") String status,
            @Field("customer") String customer);

    @FormUrlEncoded
    @POST("change_device_status.php")
    Call<MainResponse> get_change_device_status(
            @Field("user_name") String user_name,
            @Field("status") String status,
            @Field("pcb_sr_no") String pcb_sr_no);

    @FormUrlEncoded
    @POST("submit_additional_devices.php")
    Call<MainResponse> submit_additional_devices(
            @Field("user_name") String user_name,
            @Field("device_data") String device_data,
            @Field("acc_data") String acc_data,
            @Field("remarks") String remarks);

    @FormUrlEncoded
    @POST("get_tech_requirement_details.php")
    Call<CallSheetListResponse> get_tech_requirement_details(
            @Field("month") String month,
            @Field("year") String year,
            @Field("username") String username);

    @FormUrlEncoded
    @POST("get_tech_requirement_detailed_data.php")
    Call<MainResponse> get_tech_requirement_detailed_data(
            @Field("id_no") String id_no,
            @Field("username") String username);

    @FormUrlEncoded
    @POST("kd_cust_tech_device_list.php")
    Call<MainResponse> get_cust_tech_device(
            @Field("tech_id") String tech_id,
            @Field("customer") String customer);

    @FormUrlEncoded
    @POST("get_live_drum_report.php")
    Call<MainResponse> get_live_drum_report(
            @Field("veh_no") String veh_no,
            @Field("server") String server,
            @Field("dbname") String dbname);

    @FormUrlEncoded
    @POST("get_um_vehicle.php")
    Call<MainResponse> get_um_vehicle(
            @Field("main_client_id") String main_client_id,
            @Field("client_id") String client_id,
            @Field("loc_id") String loc_id,
            @Field("status") String status);   // 0 for non in under maintenance and 1 is under maintenance

    @FormUrlEncoded
    @POST("update_vehicle_um.php")
    Call<MainResponse> add_vehicle_um(
            @Field("main_client_id") String main_client_id,
            @Field("client_id") String client_id,
            @Field("loc_id") String loc_id,
            @Field("status") String status,             // 0 for non in under maintenance and 1 is under maintenance
            @Field("reg_no") String reg_no,
            @Field("activity_type") String activity_type,
            @Field("activity_time") String activity_time,
            @Field("activity_date") String activity_date,
            @Field("device_serial") String device_serial,
            @Field("vts_type") String vts_type,
            @Field("contact_person") String contact_person,
            @Field("contact_no") String contact_no,
            @Field("tech_remarks") String tech_remarks,
            @Field("technician_id") String technician_id);

}
