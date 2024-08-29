package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.ActivityDetailRepository;
import in.eoninfotech.eontechnician.repository.ClientLocationRepository;
import in.eoninfotech.eontechnician.responses.ActivityDetailResponse;
import in.eoninfotech.eontechnician.responses.ActivityResponse;
import in.eoninfotech.eontechnician.responses.ClientLocationResponse;

public class ViewModelActivityDetails extends AndroidViewModel {

    private final ActivityDetailRepository repository;

    private MutableLiveData<ActivityResponse> activityDetailResponse = new MutableLiveData<>();

    public ViewModelActivityDetails(@NonNull Application application) {
        super(application);
        this.repository = new ActivityDetailRepository(application);
    }

    public MutableLiveData<ActivityResponse> getActivityDetailRepository(String date,String tech_id) {
        activityDetailResponse = getActivityDetails(date,tech_id);
        return activityDetailResponse;
    }

    private MutableLiveData<ActivityResponse> getActivityDetails(String date, String tech_id) {
        return repository.getActivtyDetail(date,tech_id);
    }
}
