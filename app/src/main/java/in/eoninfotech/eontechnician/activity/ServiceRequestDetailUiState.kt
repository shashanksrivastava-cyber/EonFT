package `in`.eoninfotech.eontechnician.view

import `in`.eoninfotech.eontechnician.responses.ServiceRequestDetailResponse

sealed class ServiceRequestDetailUiState {
    object Idle : ServiceRequestDetailUiState()
    object Loading : ServiceRequestDetailUiState()
    data class Success(val data: List<ServiceRequestDetailResponse>) : ServiceRequestDetailUiState()
    data class Error(val message: String) : ServiceRequestDetailUiState()
}