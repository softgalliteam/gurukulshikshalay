package softgalli.gurukulshikshalay.retrofit;


import softgalli.gurukulshikshalay.model.AlumniModel;
import softgalli.gurukulshikshalay.model.CommonResponse;
import softgalli.gurukulshikshalay.model.EventsAndNoticeLisrModel;
import softgalli.gurukulshikshalay.model.FeedBackModel;
import softgalli.gurukulshikshalay.model.GalleryModel;
import softgalli.gurukulshikshalay.model.InsertAttendanceModel;
import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.model.RequestedLeaveModel;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.model.StudentListByClassModel;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.model.TopperLisrModel;
import softgalli.gurukulshikshalay.model.UserDetailsModel;

/**
 * Created by Shankar on 1/27/2018.
 */

public interface ServiceMethods {

    void addteacher(String teacher_id, String name, String qualification, String mobile_number, String alternate_number, String email_id,
                    String classteacher_for, String joining_date, String address, DownlodableCallback<StuTeaModel> callback);

    void addstudent(String user_id, String roll_no, String name, String email, String mobile, String clas, String sec,
                    String admission_date, String residential_address, DownlodableCallback<StuTeaModel> callback);

    void teacherList(DownlodableCallback<TeacherListModel> callback);

    void galleryList(DownlodableCallback<GalleryModel> callback);

    void topperlist(DownlodableCallback<TopperLisrModel> callback);

    void feedback(String name, String mobile, String message, String rating, String date, DownlodableCallback<FeedBackModel> callback);


    void userLogin(String loginAs, String mobile, String message, DownlodableCallback<UserDetailsModel> callback);

    void requestLeave(String user_id, String from_date, String to_date, String teacher_id, String description, DownlodableCallback<CommonResponse> callback);

    void requestedLeaveList(String login_as, String user_id, DownlodableCallback<RequestedLeaveModel> callback);

    void updateRequestedLeave(String status, String user_id, String teacherId, String teacherComment, DownlodableCallback<CommonResponse> callback);

    void getStudentsListByClassWise(String clas, String sec, DownlodableCallback<StudentListByClassModel> callback);

    void updateTeacher(String teacher_id, String name, String mobile_number, String alternate_number, String email_id, String address, String qualification, DownlodableCallback<CommonResponse> callback);

    void alumniList(DownlodableCallback<AlumniModel> callback);

    void getStudentsAttendance(String className, String sec, String studentId, String date, DownlodableCallback<StudentListByClassModel> callback);

    void attendance(InsertAttendanceModel insertAttendanceModel, DownlodableCallback<CommonResponse> callback);

    void deleteStudent(String studentRegId, DownlodableCallback<CommonResponse> callback);

    void deleteTeacher(String teacherRegId, DownlodableCallback<CommonResponse> callback);

    void getEventsOrNoticeList(boolean isToGetEventsList, DownlodableCallback<EventsAndNoticeLisrModel> callback);
}
