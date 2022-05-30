package in.eoninfotech.eontechnician.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FaultsDetails {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    String name;

    @SerializedName("possible_reason")
    ArrayList<FaultsData> possibleReason = new ArrayList<>();

    @SerializedName("recommend_comment")
    ArrayList<FaultsData> recommendedReason = new ArrayList<>();

    public ArrayList<FaultsData> getPossibleReason() {
        return possibleReason;
    }

    public void setPossibleReason(ArrayList<FaultsData> possibleReason) {
        this.possibleReason = possibleReason;
    }

    public ArrayList<FaultsData> getRecommendedReason() {
        return recommendedReason;
    }

    public void setRecommendedReason(ArrayList<FaultsData> recommendedReason) {
        this.recommendedReason = recommendedReason;
    }
}
