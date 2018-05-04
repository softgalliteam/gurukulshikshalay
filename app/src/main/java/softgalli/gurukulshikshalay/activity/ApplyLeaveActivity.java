package softgalli.gurukulshikshalay.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.ButterKnife;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.retrofit.RetrofitDataProvider;


/**
 * Created by Welcome on 2/1/2018.
 */

public class ApplyLeaveActivity extends AppCompatActivity {
    private EditText tvFirstName, tvLastName, tvreason, tvFromDate, tvToDate;
    private Spinner spinner;

    private RetrofitDataProvider retrofitDataProvider;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_leave_activity);
        ButterKnife.bind(this);
        retrofitDataProvider = new RetrofitDataProvider(this);
       // getCurrentDate();
        //initWidgit();


    }
}
