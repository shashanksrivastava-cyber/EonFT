package `in`.eoninfotech.eontechnician.repository

import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.webservice.NewApiholder
import javax.inject.Inject

class ServiceCountRequestRepository @Inject constructor(
    private val apiService: NewApiholder
){
    suspend fun getServiceRequestCountDetails(
        username : String?,
        zone : String?,
        activityType: String?,
        mainCustomer: String?,
        subCustomer: String?,
        location: String?,
        date: String?,
        offset: Int?,
        limit: Int?
    ): Result<MainResponse> {

        return try {

            val response = apiService.getServiceRequestCountDetails(
                username,
                zone,
                activityType,
                mainCustomer,
                subCustomer,
                location,
                date,
                offset,
                limit
            )

            if (response.isSuccessful && response.body() != null) {

                Result.success(response.body()!!)

            } else {

                Result.failure(Exception("API Error"))

            }

        } catch (e: Exception) {

            Result.failure(e)

        }
    }
}