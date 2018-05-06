package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class FeedBackModel {
    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    String data;

}
