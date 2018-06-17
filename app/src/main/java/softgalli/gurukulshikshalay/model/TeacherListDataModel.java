package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeacherListDataModel implements Serializable {
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getQualification() {
        return qualification;
    }

    public String getMobile() {
        return mobile;
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
    @SerializedName("user_id")
    String userId;
    @SerializedName("name")
    String name;
    @SerializedName("qualification")
    String qualification;
    @SerializedName("mobile")
    String mobile;
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

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("what_teach")

    String what_teach;
    @SerializedName("designation")
    String designation;
}
