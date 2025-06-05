package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.SubClientRepository;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelSerialNo extends AndroidViewModel {

    private final SerialNoRepository repository;

    private MutableLiveData<MainResponse> serialNoDetails = new MutableLiveData<>();
    public ViewModelSerialNo(@NonNull Application application) {
        super(application);
        this.repository = new SerialNoRepository(application);
    }

    public MutableLiveData<MainResponse> getSerialNoDetails(String tech_id,String customer) {
        serialNoDetails = getSerialNo(tech_id,customer);
        return serialNoDetails;
    }

    private MutableLiveData<MainResponse> getSerialNo(String tech_id,String customer) {
        return repository.getSerialNo(tech_id,customer);
    }
}
