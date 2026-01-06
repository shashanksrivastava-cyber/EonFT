package `in`.eoninfotech.eontechnician.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.eoninfotech.eontechnician.repository.YearRepository
import `in`.eoninfotech.eontechnician.repository.YearViewModel

class YearViewModelFactory(private val repository: YearRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YearViewModel::class.java)) {
            return YearViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}