package softgalli.gurukulshikshalay.retrofit;


import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.model.StuTeaModel;

/**
 * Created by Shankar on 1/27/2018.
 */

public interface ServiceMethods {
    void notification(String school_id, DownlodableCallback<NotificationModel> callback);
    void addteacher(String teacher_id, String name, String qualification, String mobile_number, String alternate_number, String email_id,
                    String classteacher_for, String joining_date, String address, DownlodableCallback<StuTeaModel> callback);
    void addstudent(String regestration_id, String name, String email, String mobile, String clas, String sec,
                    String admission_date, String residential_address, DownlodableCallback<StuTeaModel> callback);
}
