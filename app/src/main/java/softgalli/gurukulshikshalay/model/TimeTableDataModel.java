package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class TimeTableDataModel {
    public String getId() {
        return id;
    }

    public String getClas() {
        return clas;
    }

    public String getSec() {
        return sec;
    }

    public String getFrom_time() {
        return from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public String getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public TimeTableTeacherDetails getTeacher_name() {
        return teacher_name;
    }

    @SerializedName("id")
    String id;
    @SerializedName("class")
    String clas;
    @SerializedName("sec")
    String sec;
    @SerializedName("from_time")
    String from_time;
    @SerializedName("to_time")
    String to_time;
    @SerializedName("date")
    String date;
    @SerializedName("subject")
    String subject;
    @SerializedName("teacherdetails")
    TimeTableTeacherDetails teacher_name;

}
