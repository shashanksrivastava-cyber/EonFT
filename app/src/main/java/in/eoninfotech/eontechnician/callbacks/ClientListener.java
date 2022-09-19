package in.eoninfotech.eontechnician.callbacks;

import in.eoninfotech.eontechnician.Responses.ClientLocationResponse;
import in.eoninfotech.eontechnician.Responses.ClientResponse;
import in.eoninfotech.eontechnician.Responses.CollectedItemsResponse;
import in.eoninfotech.eontechnician.Responses.DisconnectionResponse;
import in.eoninfotech.eontechnician.Responses.FaultResponse;
import in.eoninfotech.eontechnician.Responses.MainResponse;
import in.eoninfotech.eontechnician.Responses.NotAvailActivityResponse;
import in.eoninfotech.eontechnician.Responses.PaymentMethodResponse;
import in.eoninfotech.eontechnician.Responses.RemovalActivityResponse;
import in.eoninfotech.eontechnician.Responses.RemovalResponse;
import in.eoninfotech.eontechnician.Responses.ReplaceReason;
import in.eoninfotech.eontechnician.Responses.SimOperatorResponse;
import in.eoninfotech.eontechnician.Responses.SimReplaceResponse;
import in.eoninfotech.eontechnician.Responses.VTSResponse;
import in.eoninfotech.eontechnician.Responses.VehNotAvailReasonResponse;
import in.eoninfotech.eontechnician.Responses.VehicleTypeResponse;
import in.eoninfotech.eontechnician.Responses.WorkTypeResponse;

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


}
