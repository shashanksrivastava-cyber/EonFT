
package `in`.eoninfotech.eontechnician.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.eoninfotech.eontechnician.repository.ServiceCountRequestRepository
import `in`.eoninfotech.eontechnician.responses.ServiceCountDetailResponse
import `in`.eoninfotech.eontechnician.view.ServiceRequestUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ServiceRequestViewModel @Inject constructor(
    private val repository: ServiceCountRequestRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ServiceRequestUiState>(ServiceRequestUiState.Idle)

    val uiState: StateFlow<ServiceRequestUiState> = _uiState

    private var offset = 0
    private val limit = 10
    private var isLastPage = false
    private var isLoading = false
    private var currentList = mutableListOf<ServiceCountDetailResponse>()

    fun fetchServiceRequestDetails(
        username: String?,
        zone: String?,
        activityType: String?,
        mainCustomer: String?,
        subCustomer: String?,
        location: String?,
        date: String?,
        loadMore: Boolean = false
    ) {

        if (loadMore && (isLoading || isLastPage)) return

        viewModelScope.launch {

            isLoading = true

            if (!loadMore) {
                offset = 0
                isLastPage = false
                currentList.clear()
                _uiState.value = ServiceRequestUiState.Loading
            }

            try {

                val result = withContext(Dispatchers.IO) {
                    repository.getServiceRequestCountDetails(
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
                }

                result.fold(

                    onSuccess = { response ->

                        if (response.type == 1) {

                            val newList = response.serviceCountDetailResponses ?: emptyList()

                            if (newList.isEmpty()) {

                                isLastPage = true

                                if (currentList.isEmpty()) {
                                    _uiState.value = ServiceRequestUiState.Empty("No data found")
                                } else {
                                    // Last page reached, keep showing existing data
                                    _uiState.value = ServiceRequestUiState.Success(currentList.toList())
                                }

                            } else {

                                currentList.addAll(newList)
                                offset += limit
                                _uiState.value = ServiceRequestUiState.Success(currentList.toList())

                            }

                        } else {

                            if (currentList.isEmpty()) {
                                _uiState.value = ServiceRequestUiState.Empty(
                                    response.msg ?: "No data found"
                                )
                            } else {
                                _uiState.value = ServiceRequestUiState.Success(currentList.toList())
                            }

                            isLastPage = true
                        }

                    },

                    onFailure = {
                        _uiState.value = ServiceRequestUiState.Error(
                            it.message ?: "Something went wrong"
                        )
                    }

                )

            } catch (e: Exception) {
                _uiState.value = ServiceRequestUiState.Error(
                    e.message ?: "Unexpected error"
                )
            }

            isLoading = false
        }
    }
}