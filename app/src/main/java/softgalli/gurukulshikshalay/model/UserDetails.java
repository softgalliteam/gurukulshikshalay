package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDetails implements Serializable{
    public String getProfile_pic() {
        return profile_pic;
    }

    public String getName() {
        return name;
    }

    @SerializedName("profile_pic")
    String profile_pic;
    @SerializedName("name")
    String name;

}
