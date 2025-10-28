package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.AddDashboardRepository;
import in.eoninfotech.eontechnician.responses.DashBoardResponse;

public class ViewModelAddDashboard extends AndroidViewModel {

    private final AddDashboardRepository repository;

    private MutableLiveData<DashBoardResponse> addDetails = new MutableLiveData<>();

    public ViewModelAddDashboard(@NonNull Application application) {
        super(application);
        this.repository = new AddDashboardRepository(application);
    }

    public MutableLiveData<DashBoardResponse> getAddCountsRepository(String zone) {
        addDetails = getAddCounts(zone);
        return addDetails;
    }

    private MutableLiveData<DashBoardResponse> getAddCounts(String zone) {
        return repository.getAddCounts(zone);
    }


}


