package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.ClientLocationRepository;
import in.eoninfotech.eontechnician.repository.SubClientRepository;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;

public class ViewModelClientLocation extends AndroidViewModel {

    private final ClientLocationRepository repository;

    private MutableLiveData<ClientLocationResponse> clientLocationDetails = new MutableLiveData<>();

    public ViewModelClientLocation(@NonNull Application application) {
        super(application);
        this.repository = new ClientLocationRepository(application);
    }

    public MutableLiveData<ClientLocationResponse> getClientLocationRepository(String customer,String server,String dbname) {
        clientLocationDetails = getClientLocation(customer,server,dbname);
        return clientLocationDetails;
    }

    private MutableLiveData<ClientLocationResponse> getClientLocation(String customer,String server,String dbname) {
        return repository.getClientLocation(customer,server,dbname);
    }
}
