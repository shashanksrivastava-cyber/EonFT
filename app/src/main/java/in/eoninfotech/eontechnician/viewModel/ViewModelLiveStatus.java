package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.LiveStatusRepository;
import in.eoninfotech.eontechnician.repository.MainClientRepository;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelLiveStatus extends AndroidViewModel {

    private final LiveStatusRepository repository;

    private MutableLiveData<MainResponse> liveStatusDetails = new MutableLiveData<>();

    public ViewModelLiveStatus(@NonNull Application application) {
        super(application);
        this.repository = new LiveStatusRepository(application);
    }

    public MutableLiveData<MainResponse> getLiveStatusRepository(String server,String dbname,String dist_id,String depo_id,String status,String type) {
        liveStatusDetails = getLiveStatus(server,dbname,dist_id,depo_id,status,type);
        return liveStatusDetails;
    }

    private MutableLiveData<MainResponse> getLiveStatus(String server,String dbname,String dist_id,String depo_id,String status,String type) {
        return repository.getLiveStatus(server,dbname,dist_id,depo_id,status,type);
    }
}
