package in.eoninfotech.eontechnician.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 9/3/19.
 */

public class YearDetail {

    @SerializedName("year")
    String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
