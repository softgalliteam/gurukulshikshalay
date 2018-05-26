package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceStatusModel {
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("student_id")
    String student_id;
    @SerializedName("status")
    String status;

    public AttendanceStatusModel(String student_id, String status){
        this.student_id = student_id;
        this.status = status;
    }
}
