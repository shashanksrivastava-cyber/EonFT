package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import in.eoninfotech.eontechnician.repository.MainClientRepository;
import in.eoninfotech.eontechnician.responses.MainClientList;
import in.eoninfotech.eontechnician.responses.MainResponse;


public class ViewModelMainClient extends AndroidViewModel {

    private final MainClientRepository repository;
    private LiveData<MainResponse> mainClientDetails;

    public ViewModelMainClient(@NonNull Application application) {
        super(application);
        this.repository = new MainClientRepository(application);
    }

    public LiveData<MainResponse> getMainClientRepository() {
        if (mainClientDetails == null) {
            mainClientDetails = repository.getMainClient();
        }
        return mainClientDetails;
    }

}
