package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

public class StudentListDataModel extends RealmObject implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("fatherName")
    private String fatherName;
    @SerializedName("gender")
    private String gender;
    @SerializedName("isSelected")
    private boolean isSelected;
    @SerializedName("age")
    private int age;

    public int getRollNo() {
        return roll_no;
    }

    @SerializedName("roll_no")
    private int roll_no;
    @SerializedName("id")
    private String id;

    public String getPassword() {
        return password;
    }

    @SerializedName("password")
    private String password;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("parents_id")
    private String parents_id;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("class")
    private String className;
    @SerializedName("sec")
    private String sec;
    @SerializedName("admission_date")
    private String admission_date;

    @SerializedName("student_id")
    private String student_id;
    @SerializedName("teacher_id")
    private String teacher_id;

    @SerializedName("residential_address")
    private String residential_address;
    @SerializedName("permanent_address")
    private String permanent_address;
    @SerializedName("profile_pic")
    private String profile_pic;


    public String getStudent_id() {
        return student_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getId() {
        return id;
    }

    public String getParentsId() {
        return parents_id;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getClassName() {
        return className;
    }

    public String getSec() {
        return sec;
    }

    public String getAdmissionDate() {
        return admission_date;
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

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public StudentListDataModel() {
    }

    public StudentListDataModel(String studentId, String studentName, String fatherName, String gender, int age) {

        this.name = studentName;
        this.user_id = studentId;
        this.fatherName = fatherName;
        this.gender = gender;
        this.age = age;
        isSelected = false;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getStudentName() {
        return name;
    }

    public void setStudentName(String studentName) {
        this.name = studentName;
    }

    public String getStudentId() {
        return user_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
