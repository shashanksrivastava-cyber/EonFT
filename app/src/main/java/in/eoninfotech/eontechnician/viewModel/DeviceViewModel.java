package in.eoninfotech.eontechnician.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.SrNoDeviceList;

public class DeviceViewModel extends ViewModel {

    private final MutableLiveData<SrNoUiModel> srNoUiData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public LiveData<SrNoUiModel> getSrNoUiData() {
        return srNoUiData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    // 🔥 CALL THIS FROM FRAGMENT INSTEAD OF CONTROLLER
    public void onDeviceResponse(MainResponse response) {

        loading.setValue(false);

        SrNoUiModel uiData = new SrNoUiModel();
        uiData.newSrList = new ArrayList<>();
        uiData.oldSrList = new ArrayList<>();

        // Default items
        uiData.newSrList.add("SELECT SR.NO.");
        uiData.oldSrList.add("SELECT SR.NO.");

        try {
            if (response != null &&
                    response.getSrno_device_list() != null &&
                    !response.getSrno_device_list().isEmpty()) {

                List<SrNoDeviceList> newList =
                        response.getSrno_device_list().get(0).getNew_sr_no();

                List<SrNoDeviceList> oldList =
                        response.getSrno_device_list().get(0).getOld_sr_no();

                if (newList != null) {
                    for (SrNoDeviceList sr : newList) {
                        uiData.newSrList.add(
                                sr.getPcb_sr_no() + sr.getCust_type()
                        );
                    }
                }

                if (oldList != null) {
                    for (SrNoDeviceList sr : oldList) {
                        uiData.oldSrList.add(sr.getPcb_sr_no());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        srNoUiData.setValue(uiData);
    }

    public void setLoading(boolean isLoading) {
        loading.setValue(isLoading);
    }
}
