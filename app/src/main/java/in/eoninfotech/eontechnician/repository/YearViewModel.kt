package `in`.eoninfotech.eontechnician.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class YearViewModel (private val repository: YearRepository) : ViewModel(){

    private val _yearList = MutableLiveData<List<String>>()
    val yearList: LiveData<List<String>> = _yearList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadYears(selectedYear: Int) {
        viewModelScope.launch {
            val result = repository.fetchYears()
            result.onSuccess {
                _yearList.value = it
            }.onFailure {
                _error.value = "Try Again - Connection timeout"
            }
        }
    }
}