package `in`.eoninfotech.eontechnician.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.eoninfotech.eontechnician.repository.ServiceCountRepository
import `in`.eoninfotech.eontechnician.responses.ServiceCountResponse
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServiceCountViewModel @Inject constructor(
    private val repository: ServiceCountRepository
) : ViewModel() {

    private val _serviceCount = MutableLiveData<ServiceCountResponse>()
    val serviceCount: LiveData<ServiceCountResponse> = _serviceCount

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun getServiceRequest(username: String?, zone: String?) {

        viewModelScope.launch {

            _loading.value = true

            try {

                val response = repository.getServiceRequest(username, zone)

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        // API Success Case
                        if (body.type.equals("1", true)) {

                            _serviceCount.value = body

                        } else {

                            // API returned failure
                            _errorMessage.value = body.message
                        }

                    } else {

                        _errorMessage.value = "No data received from server"
                    }

                } else {

                    _errorMessage.value = "Server error: ${response.code()}"
                }

            } catch (e: Exception) {

                _errorMessage.value = e.localizedMessage ?: "Something went wrong"

            } finally {

                _loading.value = false
            }
        }
    }
}