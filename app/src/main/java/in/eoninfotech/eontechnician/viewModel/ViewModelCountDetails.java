package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.CountDetailRepository;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelCountDetails extends AndroidViewModel {

    private final CountDetailRepository repository;

    private MutableLiveData<MainResponse> countDetails = new MutableLiveData<>();

    public ViewModelCountDetails(@NonNull Application application) {
        super(application);
        repository = new CountDetailRepository(application);
    }

    public MutableLiveData<MainResponse> getCountDetailsRepository(String username,String status,String customer) {
        countDetails = getCountDetails(username,status,customer);
        return countDetails;
    }

    private MutableLiveData<MainResponse> getCountDetails(String username,String status,String customer) {
        return repository.getCountDetails(username,status,customer);
    }
}
