package `in`.eoninfotech.eontechnician.webservice

import `in`.eoninfotech.eontechnician.activity.ServiceRequestActivity
import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.responses.ServiceCountResponse
import `in`.eoninfotech.eontechnician.responses.YearListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface NewApiholder {
    @GET("years-list.php")
    suspend fun getYearList(): Response<YearListResponse>

    @FormUrlEncoded
    @POST("get_service_request_count.php")
    suspend fun getServiceRequest(
        @Field("username") username: String?,
        @Field("zone") zone: String?
    ): Response<ServiceCountResponse>

    @FormUrlEncoded
    @POST("get_service_request_count_details.php")
    suspend fun getServiceRequestCountDetails(
        @Field("username") username: String?,
        @Field("zone") zone: String?,
        @Field("activity_type") activity_type: String?,
        @Field("main_customer") main_customer: String?,
        @Field("sub_cust") sub_cust: String?,
        @Field("location") location: String?,
        @Field("date") date: String?,
        @Field("offset") offset: Int?,
        @Field("limit") limit: Int?
    ): Response<MainResponse>

    @FormUrlEncoded
    @POST("get_service_request_details.php")
    suspend fun getServiceRequestDetails(
        @Field("req_no") req_no: String?,
        @Field("activity_type") activity_type: String?,
        @Field("username") username: String?,
        @Field("zone") zone: String?,
    ): Response<MainResponse>


}
