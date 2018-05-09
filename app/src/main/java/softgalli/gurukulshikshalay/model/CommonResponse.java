package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    String data;
}
