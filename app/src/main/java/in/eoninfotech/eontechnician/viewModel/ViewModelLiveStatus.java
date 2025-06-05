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

    public MutableLiveData<MainResponse> getLiveStatusRepository(String server,String dbname,String dist_id,String depo_id,String status,String type,String veh_serial_no) {
        liveStatusDetails = getLiveStatusTest(server,dbname,dist_id,depo_id,status,type,veh_serial_no);
        return liveStatusDetails;
    }

    public MutableLiveData<MainResponse> getLiveStatusRepository(String veh_no,String server,String dbname) {
        liveStatusDetails = getDrumLiveStatus(veh_no,server,dbname);
        return liveStatusDetails;
    }

    private MutableLiveData<MainResponse> getLiveStatus(String server,String dbname,String dist_id,String depo_id,String status,String type) {
        return repository.getLiveStatus(server,dbname,dist_id,depo_id,status,type);
    }

    private MutableLiveData<MainResponse> getLiveStatusTest(String server,String dbname,String dist_id,String depo_id,String status,String type,String veh_serial_no) {
        return repository.getLiveStatusReport(server,dbname,dist_id,depo_id,status,type,veh_serial_no);
    }

    private MutableLiveData<MainResponse> getDrumLiveStatus(String veh_no,String server,String dbname) {
        return repository.getLiveDrumReport(veh_no,server,dbname);
    }
}
