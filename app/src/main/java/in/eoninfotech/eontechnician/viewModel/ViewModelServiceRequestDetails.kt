//package `in`.eoninfotech.eontechnician.viewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import `in`.eoninfotech.eontechnician.repository.ServiceRequestDetailRepository
//import `in`.eoninfotech.eontechnician.responses.MainResponse
//import `in`.eoninfotech.eontechnician.responses.ServiceRequestDetailResponse
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ViewModelServiceRequestDetails @Inject constructor(
//    private val repository: ServiceRequestDetailRepository
//) : ViewModel() {
//
//    private val _serviceDetails = MutableLiveData<List<ServiceRequestDetailResponse>>()
//    val serviceDetails: LiveData<List<ServiceRequestDetailResponse>> = _serviceDetails
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String> = _errorMessage
//
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
//
//
//    fun getServiceRequestDetails(
//        reqNo: String?,
//        activityType: String?,
//        username: String?,
//        zone: String?
//    ) {
//
//        viewModelScope.launch {
//
//            _loading.value = true
//
//            val result = repository.getServiceRequestDetails(
//                reqNo,
//                activityType,
//                username,
//                zone
//            )
//
//            result.onSuccess { response ->
//
//                if (response.type == 1) {
//
//                    response.data?.let {
//
//                       _serviceDetails.value= response.serviceRequestDetailResponses
//
//                    } ?: run {
//
//                        _errorMessage.value = "No detail data available"
//                    }
//
//                } else {
//
//                    _errorMessage.value = response.message ?: "No data found"
//                }
//
//            }.onFailure { error ->
//
//                _errorMessage.value = error.message ?: "Something went wrong"
//
//            }
//
//            _loading.value = false
//        }
//    }
//}

package `in`.eoninfotech.eontechnician.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.eoninfotech.eontechnician.repository.ServiceRequestDetailRepository
import `in`.eoninfotech.eontechnician.responses.ServiceRequestDetailResponse
import `in`.eoninfotech.eontechnician.view.ServiceRequestDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelServiceRequestDetails @Inject constructor(
    private val repository: ServiceRequestDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServiceRequestDetailUiState>(ServiceRequestDetailUiState.Idle)
    val uiState: StateFlow<ServiceRequestDetailUiState> = _uiState

    fun getServiceRequestDetails(
        reqNo: String?,
        activityType: String?,
        username: String?,
        zone: String?
    ) {
        viewModelScope.launch {

            _uiState.value = ServiceRequestDetailUiState.Loading

            val result = repository.getServiceRequestDetails(
                reqNo,
                activityType,
                username,
                zone
            )

            result.onSuccess { response ->

                if (response.type == 1) {

                    val data = response.serviceRequestDetailResponses

                    if (!data.isNullOrEmpty()) {
                        _uiState.value = ServiceRequestDetailUiState.Success(data)
                    } else {
                        _uiState.value = ServiceRequestDetailUiState.Error("No detail data available")
                    }

                } else {
                    // ✅ Shows actual API failure message
                    _uiState.value = ServiceRequestDetailUiState.Error(
                        response.msg ?: "No data found"
                    )
                }

            }.onFailure { error ->
                _uiState.value = ServiceRequestDetailUiState.Error(
                    error.message ?: "Something went wrong"
                )
            }
        }
    }
}