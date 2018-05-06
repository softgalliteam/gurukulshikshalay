package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class TopperListDataModel {

    public String getToper_id() {
        return toper_id;
    }

    public String getName() {
        return name;
    }

    public String getPassing_year() {
        return passing_year;
    }

    public String getPassing_number() {
        return passing_number;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getStatus() {
        return status;
    }

    public String getClas() {
        return clas;
    }

    @SerializedName("toper_id")
    String toper_id;
    @SerializedName("name")
    String name;
    @SerializedName("passing_year")
    String passing_year;
    @SerializedName("passing_number")
    String passing_number;
    @SerializedName("user_image")
    String user_image;
    @SerializedName("status")
    String status;
    @SerializedName("class")
    String clas;

}
