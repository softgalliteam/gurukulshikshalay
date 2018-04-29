package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class TimeTableTeacherDetails {
    public String getName() {
        return name;
    }

    @SerializedName("name")
    String name;
}
