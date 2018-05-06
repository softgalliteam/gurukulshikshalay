package softgalli.gurukulshikshalay.retrofit;


import softgalli.gurukulshikshalay.model.AlumniModel;
import softgalli.gurukulshikshalay.model.FeedBackModel;
import softgalli.gurukulshikshalay.model.GalleryModel;
import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.model.TopperLisrModel;

/**
 * Created by Shankar on 1/27/2018.
 */

public interface ServiceMethods {
    void notification(String school_id, DownlodableCallback<NotificationModel> callback);
    void addteacher(String teacher_id, String name, String qualification, String mobile_number, String alternate_number, String email_id,
                    String classteacher_for, String joining_date, String address, DownlodableCallback<StuTeaModel> callback);
    void addstudent(String regestration_id, String name, String email, String mobile, String clas, String sec,
                    String admission_date, String residential_address, DownlodableCallback<StuTeaModel> callback);

    void galleryList(DownlodableCallback<GalleryModel> callback);
    void topperlist(DownlodableCallback<TopperLisrModel> callback);
    void alumniList(DownlodableCallback<AlumniModel> callback);
    void teacherList(DownlodableCallback<TeacherListModel> callback);
    void feedback(String name, String mobile, String message, String rating,String date, DownlodableCallback<FeedBackModel> callback);



}
