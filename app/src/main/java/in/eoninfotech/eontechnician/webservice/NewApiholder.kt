package `in`.eoninfotech.eontechnician.webservice

import `in`.eoninfotech.eontechnician.responses.YearListResponse
import retrofit2.Response
import retrofit2.http.GET

interface NewApiholder {
    @GET("years-list.php")
    suspend fun getYearList(): Response<YearListResponse>
}
