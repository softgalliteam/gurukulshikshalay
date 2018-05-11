package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class RequestedLeaveDataModel {
    public String getLeave_application_id() {
        return leave_application_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("leave_application_id")
    String leave_application_id;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("from_date")
    String from_date;
    @SerializedName("to_date")
    String to_date;
    @SerializedName("teacher_id")
    String teacher_id;
    @SerializedName("description")
    String description;
    @SerializedName("status")
    String status;
}
