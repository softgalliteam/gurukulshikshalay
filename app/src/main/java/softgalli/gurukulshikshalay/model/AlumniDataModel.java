package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class AlumniDataModel {

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("id")
    String id;
    @SerializedName("image")
    String image;
    @SerializedName("status")
    String status;
}
