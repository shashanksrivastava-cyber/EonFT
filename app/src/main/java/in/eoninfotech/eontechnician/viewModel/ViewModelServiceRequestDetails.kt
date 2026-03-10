package `in`.eoninfotech.eontechnician.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.eoninfotech.eontechnician.repository.ServiceRequestDetailRepository
import `in`.eoninfotech.eontechnician.responses.MainResponse
import `in`.eoninfotech.eontechnician.responses.ServiceRequestDetailResponse
import kotlinx.coroutines.launch
import javax.inject.Inject


//@HiltViewModel
//class ViewModelServiceRequestDetails @Inject constructor(
//    private val repository: ServiceRequestDetailRepository
//) : ViewModel() {
//
//    private val _serviceRequestDetails = MutableLiveData<MainResponse>()
//    val serviceRequestDetails: LiveData<MainResponse> = _serviceRequestDetails
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
//                if (response.type.equals("1")) {
//
//                    _serviceRequestDetails.value = response
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

@HiltViewModel
class ViewModelServiceRequestDetails @Inject constructor(
    private val repository: ServiceRequestDetailRepository
) : ViewModel() {

    private val _serviceDetails = MutableLiveData<List<ServiceRequestDetailResponse>>()
    val serviceDetails: LiveData<List<ServiceRequestDetailResponse>> = _serviceDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun getServiceRequestDetails(
        reqNo: String?,
        activityType: String?,
        username: String?,
        zone: String?
    ) {

        viewModelScope.launch {

            _loading.value = true

            val result = repository.getServiceRequestDetails(
                reqNo,
                activityType,
                username,
                zone
            )

            result.onSuccess { response ->

                if (response.type == 1) {

                    response.data?.let {

                       _serviceDetails.value= response.serviceRequestDetailResponses

                    } ?: run {

                        _errorMessage.value = "No detail data available"
                    }

                } else {

                    _errorMessage.value = response.message ?: "No data found"
                }

            }.onFailure { error ->

                _errorMessage.value = error.message ?: "Something went wrong"

            }

            _loading.value = false
        }
    }
}