package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InsertAttendanceModel {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public ArrayList<AttendanceStatusModel> getAttendance() {
        return attendance;
    }

    public void setAttendance(ArrayList<AttendanceStatusModel> attendance) {
        this.attendance = attendance;
    }

    @SerializedName("date")
    String date;
    @SerializedName("class")
    String clas;
    @SerializedName("sec")
    String sec;
    @SerializedName("teacher_id")
    String teacher_id;
    @SerializedName("attendance")
    ArrayList<AttendanceStatusModel> attendance;
}
