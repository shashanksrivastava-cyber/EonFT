package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.DashboardRepository;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelDeviceDashboard extends AndroidViewModel {

    private final DashboardRepository repository;

    private MutableLiveData<MainResponse> dashboardCounts = new MutableLiveData<>();

    public ViewModelDeviceDashboard(@NonNull Application application) {
        super(application);
        repository = new DashboardRepository(application);
    }
    public MutableLiveData<MainResponse> getDashboardCountRepository(String username) {
        dashboardCounts = getDashboardCount(username);
        return dashboardCounts;
    }

    private MutableLiveData<MainResponse> getDashboardCount(String username) {
        return repository.getDashboardCount(username);
    }

}
