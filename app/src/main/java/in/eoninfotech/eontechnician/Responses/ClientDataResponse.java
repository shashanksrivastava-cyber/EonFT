package in.eoninfotech.eontechnician.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by root on 7/1/19.
 */

public class ClientDataResponse {

        private StockClientDataResponse data;

        private String type;

        public StockClientDataResponse getData () {
            return data;
        }

        public void setData (StockClientDataResponse data) {
            this.data = data;
        }

        public String getType () {
            return type;
        }

        public void setType (String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "ClassPojo [data = "+data+", type = "+type+"]";
        }

}
