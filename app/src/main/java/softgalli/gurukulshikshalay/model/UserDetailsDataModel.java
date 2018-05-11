package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class UserDetailsDataModel implements Serializable{
    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsDataModel(String user_id, String name, String email, String mobile, String joining_date, String profile_pic, String status, String qualification, String alternate_number, String classteacher_for, String address, String facebook_id, String what_teach, String designation) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.joining_date = joining_date;
        this.profile_pic = profile_pic;
        this.status = status;
        this.qualification = qualification;
        this.alternate_number = alternate_number;
        this.classteacher_for = classteacher_for;
        this.address = address;
        this.facebook_id = facebook_id;
        this.what_teach = what_teach;
        this.designation = designation;
    }

    public String getClas() {
        return clas;
    }

    public String getSec() {
        return sec;
    }

    public String getJoining_date() {
        return joining_date;
    }

    public String getResidential_address() {
        return residential_address;
    }

    public String getPermanent_address() {
        return permanent_address;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getStatus() {
        return status;
    }

    public String getQualification() {
        return qualification;
    }

    public String getAlternate_number() {
        return alternate_number;
    }

    public String getSubject() {
        return subject;
    }

    public String getClassteacher_for() {
        return classteacher_for;
    }

    public String getAddress() {
        return address;
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

    public String getMobile() {
        return mobile;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("name")
    private String name;
    @SerializedName("email_id")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("class")
    private String clas;
    @SerializedName("sec")
    private String sec;
    @SerializedName("joining_date")
    private String joining_date;
    @SerializedName("residential_address")
    private String residential_address;
    @SerializedName("permanent_address")
    private String permanent_address;
    @SerializedName("image")
    private String profile_pic;
    @SerializedName("status")
    private String status;
    @SerializedName("qualification")
    private String qualification;
    @SerializedName("alternate_number")
    private String alternate_number;
    @SerializedName("subject")
    private String subject;
    @SerializedName("classteacher_for")
    private String classteacher_for;
    @SerializedName("address")
    private String address;
    @SerializedName("facebook_id")
    private String facebook_id;
    @SerializedName("what_teach")
    private String what_teach;
    @SerializedName("designation")
    private String designation;

    public String getFatherName() {
        return fatherName;
    }

    @SerializedName("father_name")
    private String fatherName;


}
