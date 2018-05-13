package softgalli.gurukulshikshalay.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

public class RetrofitDataProvider extends AppCompatActivity implements ServiceMethods {
    Context context;
    private ApiRetrofitService createRetrofitService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiRetrofitService.class);
    }

    public RetrofitDataProvider(Context context) {
        this.context = context;
    }


    @Override
    public void notification(String school_id, final DownlodableCallback<NotificationModel> callback) {
        createRetrofitService().otpLogin(school_id).enqueue(
                new Callback<NotificationModel>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationModel> call, @NonNull final Response<NotificationModel> response) {
                        if (response.isSuccessful()) {
                            NotificationModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);

                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void addteacher(String teacher_id, String name, String qualification, String mobile_number, String alternate_number, String email_id, String classteacher_for, String joining_date, String address, final DownlodableCallback<StuTeaModel> callback) {
        createRetrofitService().addTeacher(teacher_id, name, qualification, mobile_number, alternate_number, email_id, classteacher_for, joining_date, address).enqueue(
                new Callback<StuTeaModel>() {
                    @Override
                    public void onResponse(@NonNull Call<StuTeaModel> call, @NonNull final Response<StuTeaModel> response) {
                        if (response.isSuccessful()) {
                            StuTeaModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StuTeaModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }

    @Override
    public void addstudent(String regestration_id, String name, String email, String mobile, String clas, String sec, String admission_date, String residential_address, final DownlodableCallback<StuTeaModel> callback) {
        createRetrofitService().addStudent(regestration_id, name, email, mobile, clas, sec, admission_date, residential_address).enqueue(
                new Callback<StuTeaModel>() {
                    @Override
                    public void onResponse(@NonNull Call<StuTeaModel> call, @NonNull final Response<StuTeaModel> response) {
                        if (response.isSuccessful()) {
                            StuTeaModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StuTeaModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }

    @Override
    public void galleryList(final DownlodableCallback<GalleryModel> callback) {
        createRetrofitService().listGallery().enqueue(
                new Callback<GalleryModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GalleryModel> call, @NonNull final Response<GalleryModel> response) {
                        if (response.isSuccessful()) {
                            GalleryModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);

                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GalleryModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }

    @Override
    public void topperlist(final DownlodableCallback<TopperLisrModel> callback) {
        createRetrofitService().topperList().enqueue(
                new Callback<TopperLisrModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TopperLisrModel> call, @NonNull final Response<TopperLisrModel> response) {
                        if (response.isSuccessful()) {
                            TopperLisrModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);

                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TopperLisrModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }

    @Override
    public void alumniList(final DownlodableCallback<AlumniModel> callback) {
        createRetrofitService().alumni().enqueue(
                new Callback<AlumniModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AlumniModel> call, @NonNull final Response<AlumniModel> response) {
                        if (response.isSuccessful()) {
                            AlumniModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);

                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AlumniModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }

    @Override
    public void teacherList(final DownlodableCallback<TeacherListModel> callback) {
        createRetrofitService().teacherList().enqueue(
                new Callback<TeacherListModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TeacherListModel> call, @NonNull final Response<TeacherListModel> response) {
                        if (response.isSuccessful()) {
                            TeacherListModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TeacherListModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());

                    }
                }
        );
    }

    @Override
    public void feedback(String name, String mobile, String message, String rating, String date, final DownlodableCallback<FeedBackModel> callback) {
        createRetrofitService().sendFeedback(name, mobile, message, rating, date).enqueue(
                new Callback<FeedBackModel>() {
                    @Override
                    public void onResponse(@NonNull Call<FeedBackModel> call, @NonNull final Response<FeedBackModel> response) {
                        if (response.isSuccessful()) {
                            FeedBackModel mobileRegisterPojo = response.body();
                            callback.onSuccess(mobileRegisterPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FeedBackModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void userLogin(String loginAs, String mobile, String message, final DownlodableCallback<UserDetailsModel> callback) {
        createRetrofitService().userLogin(loginAs, mobile, message).enqueue(
                new Callback<UserDetailsModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDetailsModel> call, @NonNull final Response<UserDetailsModel> response) {
                        if (response.isSuccessful()) {
                            UserDetailsModel teacherListDataModelPojo = response.body();
                            callback.onSuccess(teacherListDataModelPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserDetailsModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void updateTeacher(String teacher_id, String name, String mobile_number, String alternate_number, String email_id, String address, String qualification, final DownlodableCallback<CommonResponse> callback) {
        createRetrofitService().updateTeacher(teacher_id, name, mobile_number, alternate_number, email_id, address, qualification).enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponse> call, @NonNull final Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            CommonResponse teacherListDataModelPojo = response.body();
                            callback.onSuccess(teacherListDataModelPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void updateStudent(String user_id, String name, String email, String mobile, String residential_address, final DownlodableCallback<CommonResponse> callback) {
        createRetrofitService().updateStudent(user_id, name, email, mobile, residential_address).enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponse> call, @NonNull final Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            CommonResponse teacherListDataModelPojo = response.body();
                            callback.onSuccess(teacherListDataModelPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void requestLeave(String user_id, String from_date, String to_date, String teacher_id, String description, final DownlodableCallback<CommonResponse> callback) {
        createRetrofitService().requestLeave(user_id, from_date, to_date, teacher_id, description).enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponse> call, @NonNull final Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            CommonResponse teacherListDataModelPojo = response.body();
                            callback.onSuccess(teacherListDataModelPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void requestedLeaveList(String loginedAs,String userId, final DownlodableCallback<RequestedLeaveModel> callback) {
        createRetrofitService().requestedLeaveList(loginedAs,userId).enqueue(
                new Callback<RequestedLeaveModel>() {
                    @Override
                    public void onResponse(@NonNull Call<RequestedLeaveModel> call, @NonNull final Response<RequestedLeaveModel> response) {
                        if (response.isSuccessful()) {
                            RequestedLeaveModel teacherListDataModelPojo = response.body();
                            callback.onSuccess(teacherListDataModelPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RequestedLeaveModel> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }

    @Override
    public void updateRequestedLeave(String status, String user_id, String teacherComment, final DownlodableCallback<CommonResponse> callback) {
        createRetrofitService().updateLeave(status, user_id, teacherComment).enqueue(
                new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonResponse> call, @NonNull final Response<CommonResponse> response) {
                        if (response.isSuccessful()) {
                            CommonResponse teacherListDataModelPojo = response.body();
                            callback.onSuccess(teacherListDataModelPojo);
                        } else {
                            if (response.code() == 401) {
                                callback.onUnauthorized(response.code());
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                        Log.d("Result", "t" + t.getMessage());
                        callback.onFailure(t.getMessage());
                    }
                }
        );
    }


}