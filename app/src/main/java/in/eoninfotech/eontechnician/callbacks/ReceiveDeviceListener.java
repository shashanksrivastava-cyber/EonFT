package in.eoninfotech.eontechnician.callbacks;

import in.eoninfotech.eontechnician.responses.MainResponse;

public interface ReceiveDeviceListener extends ErrorCallBacks {

    void receiveDeviceResponse(MainResponse response);
    void receiveDispatchMaterial(MainResponse response);

     void returnDeviceresponse (MainResponse response);

     void dispatchFromTechResponse(MainResponse response);



}
