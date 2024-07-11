package in.eoninfotech.eontechnician.callbacks;

import in.eoninfotech.eontechnician.responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.responses.ClientResponse;
import in.eoninfotech.eontechnician.responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.responses.FaultResponse;
import in.eoninfotech.eontechnician.responses.MainResponse;
import in.eoninfotech.eontechnician.responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.responses.RemovalResponse;
import in.eoninfotech.eontechnician.responses.ReplaceReason;
import in.eoninfotech.eontechnician.responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.responses.VTSResponse;
import in.eoninfotech.eontechnician.responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.responses.WorkTypeResponse;

/**
 * Created by root on 11/1/19.
 */

public interface ClientListener extends ErrorCallBacks {

    void clientResponse(ClientResponse response);

    void locationResponse (ClientLocationResponse response);

    void workTypeResponse(WorkTypeResponse response);

    void vehicleTypeResponse(VehicleTypeResponse response);

    void faultListResponse(FaultResponse response);

    void replaceResponse(ReplaceReason response);

    void disconnectionResponse(DisconnectionResponse response);

    void removalActivityResponse(RemovalActivityResponse response);

    void removalResponse(RemovalResponse response);

    void damageResponse(RemovalResponse response);

    void collectItemResponse(CollectedItemsResponse response);

    void simOperatorResponse(SimOperatorResponse response);

    void simReplaceReason(SimReplaceResponse response);

    void notAvailActivity(NotAvailActivityResponse response);

    void vehicleNotAvailReason(VehNotAvailReasonResponse response);

    void vtsResponses (VTSResponse response);

    void vtsResponse(VTSResponse response);

    void pMethod(PaymentMethodResponse response);

    void updateDataResponse(MainResponse response);
    void mainClientResponse(MainResponse response);
    void vtsAccResponses(MainResponse response);


}
