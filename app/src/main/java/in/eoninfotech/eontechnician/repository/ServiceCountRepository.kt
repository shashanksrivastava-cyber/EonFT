package `in`.eoninfotech.eontechnician.repository

import `in`.eoninfotech.eontechnician.responses.ServiceCountResponse
import `in`.eoninfotech.eontechnician.webservice.NewApiholder
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ServiceCountRepository @Inject constructor(
    private val apiService: NewApiholder
) {

    /**
     * Fetch Service Request Count from API
     */
    suspend fun getServiceRequest(
        username: String?,
        zone: String?
    ): Response<ServiceCountResponse> {

        return apiService.getServiceRequest(username, zone)

    }
}