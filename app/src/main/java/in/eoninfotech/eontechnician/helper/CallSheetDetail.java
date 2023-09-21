
package in.eoninfotech.eontechnician.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 13/10/17.
 */
public class CallSheetDetail {

    @SerializedName("month_id")
    private String month_id;
    @SerializedName("month")
    private String month;
    @SerializedName("year")
    private String year;
    @SerializedName("sheets_count")
    private String sheets_count;
    @SerializedName("image")
    private String image;

    public String getMonth_id() {
        return month_id;
    }

    public void setMonth_id(String month_id) {
        this.month_id = month_id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSheets_count() {
        return sheets_count;
    }

    public void setSheets_count(String sheets_count) {
        this.sheets_count = sheets_count;
    }
}
