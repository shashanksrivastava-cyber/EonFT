package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.MainClientRepository;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelMainClient extends AndroidViewModel {

    private final MainClientRepository repository;

    private MutableLiveData<MainResponse> mainClientDetails = new MutableLiveData<>();
    public ViewModelMainClient(@NonNull Application application) {
        super(application);
        this.repository = new MainClientRepository(application);
    }

    public MutableLiveData<MainResponse> getMainClientRepository() {
        mainClientDetails = getMainClient();
        return mainClientDetails;
    }

    private MutableLiveData<MainResponse> getMainClient() {
        return repository.getMainClient();
    }
}
