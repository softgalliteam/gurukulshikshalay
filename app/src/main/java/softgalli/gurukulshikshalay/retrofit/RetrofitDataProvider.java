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
import softgalli.gurukulshikshalay.model.NotificationModel;
import softgalli.gurukulshikshalay.model.StuTeaModel;

/**
 * Created by Shankar on 1/27/2018.
 */

public class RetrofitDataProvider extends AppCompatActivity implements ServiceMethods {
    private Context context;

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

    public  RetrofitDataProvider(Context context)
    {
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

                        } else

                        {
                            if (response.code() == 401)
                            {
                                callback.onUnauthorized(response.code());
                            }
                            else {
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

                        } else

                        {
                            if (response.code() == 401)
                            {
                                callback.onUnauthorized(response.code());
                            }
                            else {
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

                        } else

                        {
                            if (response.code() == 401)
                            {
                                callback.onUnauthorized(response.code());
                            }
                            else {
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


}