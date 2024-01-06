package in.eoninfotech.eontechnician.callbacks;

import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.DispatchDeviceDetails;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.ReceiveDeviceResponse;

public interface ReceiveDeviceListener extends ErrorCallBacks {

    void receiveDeviceResponse(MainResponse response);
    void receiveDispatchMaterial(MainResponse response);

     void returnDeviceresponse (MainResponse response);

     void dispatchFromTechResponse(MainResponse response);



}
