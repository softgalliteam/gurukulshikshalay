package softgalli.gurukulshikshalay.retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

public interface ApiRetrofitService {
    @POST(ApiUrl.NOTIFICATION)
    @FormUrlEncoded
    Call<NotificationModel> otpLogin(@Field("school_id") String school_id);

    @POST(ApiUrl.ADDTEACHER)
    @FormUrlEncoded
    Call<StuTeaModel> addTeacher(@Field("teacher_id") String teacher_id, @Field("name") String name, @Field("qualification") String qualification
            , @Field("mobile_number") String mobile_number, @Field("alternate_number") String alternate_number, @Field("email_id") String email_id
            , @Field("classteacher_for") String classteacher_for, @Field("joining_date") String joining_date, @Field("address") String address);

    @POST(ApiUrl.ADDSTUDENT)
    @FormUrlEncoded
    Call<StuTeaModel> addStudent(@Field("user_id") String user_id, @Field("roll_no") String roll_no, @Field("name") String name, @Field("email") String email
            , @Field("mobile") String mobile_number, @Field("class") String clas, @Field("sec") String sec,
                                 @Field("admission_date") String admission_date, @Field("residential_address") String residential_address);

    @GET(ApiUrl.GALLERYLIST)
    Call<GalleryModel> listGallery();

    @GET(ApiUrl.TOPPERLIST)
    Call<TopperLisrModel> topperList();

    @GET(ApiUrl.ALUMNI)
    Call<AlumniModel> alumni();

    @GET(ApiUrl.TEACHERLIST)
    Call<TeacherListModel> teacherList();

    @POST(ApiUrl.FEEDBACK)
    @FormUrlEncoded
    Call<FeedBackModel> sendFeedback(@Field("name") String name, @Field("mobile") String mobile, @Field("message") String message, @Field("rating") String rating, @Field("date") String date);


    @POST(ApiUrl.LOGIN)
    @FormUrlEncoded
    Call<UserDetailsModel> userLogin(@Field("login_as") String name, @Field("user_id") String mobile, @Field("password") String message);

    @POST(ApiUrl.UPDATETEACHER)
    @FormUrlEncoded
    Call<CommonResponse> updateTeacher(@Field("teacher_id") String teacher_id, @Field("name") String name, @Field("mobile_number") String mobile_number, @Field("alternate_number") String alternate_number
            , @Field("email_id") String email_id, @Field("address") String address, @Field("qualification") String qualification);

    @POST(ApiUrl.UPDATESTUDENT)
    @FormUrlEncoded
    Call<CommonResponse> updateStudent(@Field("user_id") String user_id, @Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("residential_address") String residential_address);

    @POST(ApiUrl.APPLYLEAVE)
    @FormUrlEncoded
    Call<CommonResponse> requestLeave(@Field("user_id") String user_id, @Field("from_date") String from_date, @Field("to_date") String to_date, @Field("teacher_id") String teacher_id, @Field("description") String description);

    @POST(ApiUrl.LEAVEREQUESTLIST)
    @FormUrlEncoded
    Call<RequestedLeaveModel> requestedLeaveList(@Field("login_as") String loginedAs, @Field("user_id") String userId);

    @POST(ApiUrl.UPDATELEAVE)
    @FormUrlEncoded
    Call<CommonResponse> updateLeave(@Field("status") String status, @Field("user_id") String user_id, @Field("teacher_id") String teacherId, @Field("teacher_comment") String teacherComment);

    @POST(ApiUrl.GET_CLASS_WISE_STUDENT_LIST)
    @FormUrlEncoded
    Call<StudentListByClassModel> getStudentsListByClassWise(@Field("class") String className, @Field("sec") String sec);

    @POST(ApiUrl.GET_STUDENT_ATTENDANCE_LIST)
    @FormUrlEncoded
    Call<StudentListByClassModel> getStudentsAttendance(@Field("class") String className, @Field("sec") String sec, @Field("studentId") String studentId, @Field("date") String date);

    @POST(ApiUrl.INSER_TATTENDANCE)
    Call<CommonResponse> insertAttendance(@Body InsertAttendanceModel insertAttendanceModel);

    @FormUrlEncoded
    @POST(ApiUrl.DELETE_STUDENT)
    Call<CommonResponse> deleteStudent(@Field("user_id") String deleteStudent);

    @FormUrlEncoded
    @POST(ApiUrl.DELETE_TEACHER)
    Call<CommonResponse> deleteTeacher(@Field("user_id") String deleteTeacher);

    @FormUrlEncoded
    @POST(ApiUrl.CHANGE_PASSWORD)
    Call<CommonResponse> changePassword(@Field("user_id") String userId, @Field("login_type") String loginType,
                                        @Field("old_password") String oldPassword, @Field("new_password") String newPassword);
    @FormUrlEncoded
    @POST(ApiUrl.PUBLISH_NOTICE)
    Call<CommonResponse> publishNotice(@Field("title") String title, @Field("message") String message, @Field("date") String date, @Field("posted_by") String posted_by, @Field("status") String status);

    @FormUrlEncoded
    @POST(ApiUrl.ADD_EVENT)
    Call<CommonResponse> addEvent(@Field("title") String title, @Field("message") String message, @Field("date") String date, @Field("posted_by") String posted_by, @Field("status") String status);

    @GET(ApiUrl.GET_EVENT_LIST)
    Call<EventsAndNoticeLisrModel> getEventsList();

    @GET(ApiUrl.GET_NOTICE_LIST)
    Call<EventsAndNoticeLisrModel> getNoticeBoardList();

    @FormUrlEncoded
    @POST(ApiUrl.EDIT_DELETE_EVENT)
    Call<CommonResponse> callUpdateDeleteEvents(@Field("title") String title, @Field("message") String message, @Field("date") String date, @Field("posted_by") String posted_by,
                                                         @Field("status") String status, @Field("id") String id, @Field("isToUpdate") String isToUpdate);
    @FormUrlEncoded
    @POST(ApiUrl.EDIT_DELETE_NOTICE)
    Call<CommonResponse> callUpdateDeleteNoticeBoard(@Field("title") String title, @Field("message") String message, @Field("date") String date, @Field("posted_by") String posted_by,
                                                         @Field("status") String status, @Field("id") String id, @Field("isToUpdate") String isToUpdate);
}
