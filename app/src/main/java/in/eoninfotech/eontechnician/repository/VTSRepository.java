package in.eoninfotech.eontechnician.repository;

import java.util.ArrayList;
import java.util.List;

import in.eoninfotech.eontechnician.responses.DeviceTypeOtherAis;
import in.eoninfotech.eontechnician.webservice.ApiHolder;
import in.eoninfotech.eontechnician.webservice.ServiceConnectionNewURL;
import in.eoninfotech.eontechnician.webservice.VTSTypeResponse;
import retrofit2.Call;
import retrofit2.Response;

public class VTSRepository {

    ArrayList<DeviceTypeOtherAis> deviceTypeOtherAis_arr = new ArrayList<>();

    public interface Callback {
        void onSuccess(List<String> deviceTypes);
        void onError(String message);
    }

    public void getDeviceTypes(String version, Callback callback) {

        ApiHolder api = ServiceConnectionNewURL.getClient(version)
                .create(ApiHolder.class);

        api.getVTSTypes().enqueue(new retrofit2.Callback<VTSTypeResponse>() {
            @Override
            public void onResponse(Call<VTSTypeResponse> call,
                                   Response<VTSTypeResponse> response) {
                try {
                    if (response.body() != null &&
                            "1".equals(response.body().getType())) {

                        List<String> list = new ArrayList<>();
                        list.add(" SELECT VTS TYPE");

                        for (DeviceTypeOtherAis item : response.body().getDeviceTypesArr()) {
                            list.add(item.getName());
                        }

                        callback.onSuccess(list);
                    } else {
                        callback.onError("No data");
                    }
                } catch (Exception e) {
                    callback.onError("Parsing error");
                }
            }

            @Override
            public void onFailure(Call<VTSTypeResponse> call, Throwable t) {
                callback.onError("Network error");
            }
        });
    }
}
