package `in`.eoninfotech.eontechnician.view

import `in`.eoninfotech.eontechnician.responses.ServiceCountDetailResponse

sealed class ServiceRequestUiState {

    object Idle : ServiceRequestUiState()

    object Loading : ServiceRequestUiState()

    data class Success(
        val data: List<ServiceCountDetailResponse>
    ) : ServiceRequestUiState()

    data class Empty(
        val message: String
    ) : ServiceRequestUiState()

    data class Error(
        val message: String
    ) : ServiceRequestUiState()
}