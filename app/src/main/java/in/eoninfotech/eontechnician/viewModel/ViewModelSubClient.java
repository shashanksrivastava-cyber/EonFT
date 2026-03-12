package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    public LiveData<ClientResponse> getSubClientLiveData() {
        return subClientDetails;
    }

    public void fetchSubClients(String c_id) {
        repository.getSubClient(c_id).observeForever(response -> {
            subClientDetails.setValue(response);
        });
    }
}

//public class ViewModelSubClient extends AndroidViewModel {
//
//    private final SubClientRepository repository;
//    private MutableLiveData<ClientResponse> subClientDetails = new MutableLiveData<>();
//
//    public ViewModelSubClient(@NonNull Application application) {
//        super(application);
//        this.repository = new SubClientRepository(application);
//    }
//
//    // ✅ Observer attaches to this — called ONCE in observeViewModels()
//    public LiveData<ClientResponse> getSubClientLiveData() {
//        return subClientDetails;
//    }
//
//    // ✅ Called from spinner listener when user selects main client
//    public void fetchSubClients(String c_id) {
//        repository.getSubClient(c_id).observeForever(response -> {
//            subClientDetails.setValue(response);
//        });
//    }
//}
