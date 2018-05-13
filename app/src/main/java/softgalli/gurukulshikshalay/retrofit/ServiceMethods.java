package softgalli.gurukulshikshalay.retrofit;


import softgalli.gurukulshikshalay.model.AlumniModel;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.FeedBackModel;
import softgalli.gurukulshikshalay.model.GalleryModel;
import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.model.RequestedLeaveModel;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.model.UserDetailsDataModel;
import softgalli.gurukulshikshalay.model.TopperLisrModel;
import softgalli.gurukulshikshalay.model.UserDetailsModel;

/**
 * Created by Shankar on 1/27/2018.
 */

public interface ServiceMethods {
    void notification(String school_id, DownlodableCallback<NotificationModel> callback);
    void addteacher(String teacher_id, String name, String qualification, String mobile_number, String alternate_number, String email_id,
                    String classteacher_for, String joining_date, String address, DownlodableCallback<StuTeaModel> callback);
    void addstudent(String regestration_id, String name, String email, String mobile, String clas, String sec,
                    String admission_date, String residential_address, DownlodableCallback<StuTeaModel> callback);
    void teacherList(DownlodableCallback<TeacherListModel> callback);
    void galleryList(DownlodableCallback<GalleryModel> callback);
    void topperlist(DownlodableCallback<TopperLisrModel> callback);
    void alumniList(DownlodableCallback<AlumniModel> callback);
    void feedback(String name, String mobile, String message, String rating,String date, DownlodableCallback<FeedBackModel> callback);


    void userLogin(String loginAs, String mobile, String message, DownlodableCallback<UserDetailsModel> callback);
    void updateTeacher(String teacher_id, String name, String mobile_number, String alternate_number, String email_id, String address, String qualification, DownlodableCallback<CommonResponse> callback);
    void updateStudent(String user_id, String name, String email, String mobile, String residential_address, DownlodableCallback<CommonResponse> callback);
    void requestLeave(String user_id, String from_date, String to_date, String teacher_id, String description, DownlodableCallback<CommonResponse> callback);
    void requestedLeaveList(String login_as,String user_id, DownlodableCallback<RequestedLeaveModel> callback);
    void updateRequestedLeave(String status, String user_id, String teacherComment, DownlodableCallback<CommonResponse> callback);
}
