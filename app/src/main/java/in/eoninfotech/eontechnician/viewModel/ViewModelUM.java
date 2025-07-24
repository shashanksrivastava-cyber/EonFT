package in.eoninfotech.eontechnician.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import in.eoninfotech.eontechnician.repository.UnderMaintRepository;
import in.eoninfotech.eontechnician.responses.MainResponse;

public class ViewModelUM extends AndroidViewModel {

    private final UnderMaintRepository underMaintRepository;

    private MutableLiveData<MainResponse> umDetails;

    private MutableLiveData<MainResponse> addVehicleUM;
    public ViewModelUM(@NonNull Application application) {
        super(application);
        this.underMaintRepository = new UnderMaintRepository(application);
    }

    public MutableLiveData<MainResponse> getUmRepository(String mainClientId, String clientId, String locId,String status) {
        umDetails = getUMDetails(mainClientId,clientId,locId,status);
        return umDetails;
    }

    private MutableLiveData<MainResponse> getUMDetails(String mainClientId, String clientId, String locId,String status) {
        return underMaintRepository.getUMDetails(mainClientId,clientId,locId,status);
    }

    public MutableLiveData<MainResponse> addInUM(String mainClientId, String clientId, String locId,String status,String reg_no,String activity_type, String activity_time, String activity_date,String device_serial,String vts_type,String contact_person, String contact_no,String tech_remarks,String technician_id) {
        addVehicleUM = getAddVehicleUM(mainClientId,clientId,locId,status,reg_no,activity_type,activity_time,activity_date,device_serial,vts_type,contact_person,contact_no,tech_remarks,technician_id);
        return addVehicleUM;
    }

    public MutableLiveData<MainResponse> getAddVehicleUM(String mainClientId, String clientId, String locId, String status, String reg_no, String activity_type, String activity_time, String activity_date, String device_serial, String vts_type, String contact_person, String contact_no, String tech_remarks, String technician_id) {
        return underMaintRepository.getAddVehicleUM(mainClientId,clientId,locId,status,reg_no,activity_type,activity_time,activity_date,device_serial,vts_type,contact_person,contact_no,tech_remarks,technician_id);
    }

}
