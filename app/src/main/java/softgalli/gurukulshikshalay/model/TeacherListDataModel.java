package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

public class TeacherListDataModel {

    public String getId() {
        return id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getName() {
        return name;
    }

    public String getQualification() {
        return qualification;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getAlternate_number() {
        return alternate_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getSubject() {
        return subject;
    }

    public String getClassteacher_for() {
        return classteacher_for;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getWhat_teach() {
        return what_teach;
    }

    public String getDesignation() {
        return designation;
    }

    @SerializedName("id")
    String id;
    @SerializedName("teacher_id")
    String teacher_id;
    @SerializedName("name")
    String name;
    @SerializedName("qualification")
    String qualification;
    @SerializedName("mobile_number")
    String mobile_number;
    @SerializedName("alternate_number")
    String alternate_number;
    @SerializedName("email_id")
    String email_id;
    @SerializedName("subject")
    String subject;
    @SerializedName("classteacher_for")
    String classteacher_for;
    @SerializedName("joining_date")
    String joining_date;
    @SerializedName("address")
    String address;
    @SerializedName("image")
    String image;
    @SerializedName("status")
    String status;
    @SerializedName("facebook_id")
    String facebook_id;
    @SerializedName("what_teach")
    String what_teach;
    @SerializedName("designation")
    String designation;
}
