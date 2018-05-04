package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shankar on 3/18/2018.
 */

public class GalleryModel {
    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<GalleryDataModel> getData() {
        return data;
    }

    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    ArrayList<GalleryDataModel> data;

}
