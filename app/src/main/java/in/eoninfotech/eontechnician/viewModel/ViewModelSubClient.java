package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.MainClientRepository;
import in.eoninfotech.eontechnician.repository.SubClientRepository;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelSubClient extends AndroidViewModel {

    private final SubClientRepository repository;

    private MutableLiveData<ClientResponse> subClientDetails = new MutableLiveData<>();
    public ViewModelSubClient(@NonNull Application application) {
        super(application);
        this.repository = new SubClientRepository(application);
    }

    public MutableLiveData<ClientResponse> getSubClientRepository(String c_id) {
        subClientDetails = getSubClient(c_id);
        return subClientDetails;
    }

    private MutableLiveData<ClientResponse> getSubClient(String c_id) {
        return repository.getSubClient(c_id);
    }
}
