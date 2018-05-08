package softgalli.gurukulshikshalay.retrofit;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import softgalli.gurukulshikshalay.model.AlumniModel;
import softgalli.gurukulshikshalay.model.FeedBackModel;
import softgalli.gurukulshikshalay.model.GalleryModel;
import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.model.StuTeaModel;
import softgalli.gurukulshikshalay.model.TeacherListModel;
import softgalli.gurukulshikshalay.model.UserDetailsDataModel;
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
    Call<StuTeaModel> addStudent(@Field("regestration_id") String regestration_id, @Field("name") String name, @Field("email") String email
            , @Field("mobile") String mobile_number, @Field("class") String clas, @Field("sec") String classteacher_for,
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

}
