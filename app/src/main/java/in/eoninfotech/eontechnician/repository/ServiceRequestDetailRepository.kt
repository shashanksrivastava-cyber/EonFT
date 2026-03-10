package `in`.eoninfotech.eontechnician.repository

import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.webservice.NewApiholder
import javax.inject.Inject

class ServiceRequestDetailRepository @Inject constructor(
    private val apiService: NewApiholder
){

    suspend fun getServiceRequestDetails(
        req_no : String?,
        activity_type : String?,
        username: String?,
        zone: String?
    ) : Result<MainResponse> {

        return try {

            val response = apiService.getServiceRequestDetails(
                req_no,
                activity_type,
                username,
                zone,
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