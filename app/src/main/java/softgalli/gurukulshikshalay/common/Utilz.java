package softgalli.gurukulshikshalay.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import softgalli.gurukulshikshalay.BuildConfig;
import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.activity.ApplyLeaveActivity;
import softgalli.gurukulshikshalay.activity.LoginScreenActivity;
import softgalli.gurukulshikshalay.activity.SeeAttendenceActivity;
import softgalli.gurukulshikshalay.activity.SeeLeaveListActivity;
import softgalli.gurukulshikshalay.activity.TakeAttendenceActivity;
import softgalli.gurukulshikshalay.preference.MyPreference;
import softgalli.gurukulshikshalay.retrofit.ApiUrl;


public class Utilz {
    private static String TAG = Utilz.class.getSimpleName();
    static ProgressDialog dialog;


    public static String getRandomName() {

        final String AB = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 8; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();

    }

    public static boolean isInternetConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    public static boolean isValidEmail1(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }


    public static int getDateFromString(String dateStr) {
        int date = 0;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parsedDate = DATE_FORMAT.parse(dateStr);
            date = parsedDate.getDate();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static void showDailog(Context c, String msg) {
        dialog = new ProgressDialog(c);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    public static void closeDialog() {
        if (dialog != null)
            dialog.cancel();
    }

    public static String getCurrentDate(Context askQuestion) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getCurrentTime(Context askQuestion) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        return currentDateTimeString;
    }

    public static String getCurrentDateInDigit(Context timeTableActivity) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String todaysday(Context timeTableActivity) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    private static Calendar c;
    private static List<String> output;

    public static List<String> getCalendar() {
        c = Calendar.getInstance();
        output = new ArrayList<String>();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        //Get current Day of week and Apply suitable offset to bring the new calendar
        //back to the appropriate Monday, i.e. this week or next
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                c.add(Calendar.DATE, -1);
                break;

            case Calendar.TUESDAY:
                c.add(Calendar.DATE, -2);
                break;

            case Calendar.WEDNESDAY:
                c.add(Calendar.DATE, -3);
                break;

            case Calendar.THURSDAY:
                c.add(Calendar.DATE, -4);
                break;

            case Calendar.FRIDAY:
                c.add(Calendar.DATE, -5);
                break;

            case Calendar.SATURDAY:
                c.add(Calendar.DATE, -6);
                break;
        }

        //Add the Monday to the output
        // output.add(c.getTime().toString());
        for (int x = 1; x <= 6; x++) {
            //Add the remaining days to the output
            c.add(Calendar.DATE, 1);
            output.add(df.format(c.getTime()));
        }
        return output;
    }


    public static void setTextType(int textStyle, TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setTypeface(tv.getTypeface(), textStyle);
        }
    }

    public static void setTextSize(int textSize, TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    /**
     * Check device have internet connection or not
     *
     * @param activity
     * @return
     */
    public static boolean isOnline(Activity activity) {
        {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = null;
            if (activity != null)
                cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                        if (ni.isConnected()) {
                            haveConnectedWifi = true;
                            Log.i("", "WIFI CONNECTION : AVAILABLE");
                        } else {
                            Log.i(TAG, "WIFI CONNECTION : NOT AVAILABLE");
                        }
                    }
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                        if (ni.isConnected()) {
                            haveConnectedMobile = true;
                            Log.i(TAG, "MOBILE INTERNET CONNECTION : AVAILABLE");
                        } else {
                            Log.i(TAG, "MOBILE INTERNET CONNECTION : NOT AVAILABLE");
                        }
                    }
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }

    }

    public static void showNoInternetConnectionDialog(final Activity mActivity) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(mActivity.getResources().getString(R.string.no_internet_connection_msg_title));
            builder.setMessage(mActivity.getResources().getString(R.string.no_internet_connection_msg));
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "Show Dialog: " + e.getMessage());
        }
    }

    public static void showMessageOnDialog(final Activity mActivity, final String title, final String message) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "Show Dialog: " + e.getMessage());
        }
    }

    public static String getCurrentDate() {
        String urrentDate = "";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        urrentDate = df.format(c);
        return urrentDate;
    }

    public static int getCurrentDateOnly() {
        Date c = Calendar.getInstance().getTime();
        return c.getDate();
    }

    public static void openDialer(final Activity mActivity, final String mobileNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobileNo));
        mActivity.startActivity(intent);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            mActivity.startActivity(intent);
            return;
        }
    }


    public static void whatsappShare(final Activity mActivity, final String mobileNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobileNo));
        mActivity.startActivity(intent);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            mActivity.startActivity(intent);
            return;
        }
    }

    @SuppressLint("NewApi")
    public static void logout(final Activity mActivity) {
        MyPreference.setLogin(false);
        MyPreference.setSignupSkipped(false);
        MyPreference.setLoginedAs("");
        MyPreference.setUserName("");
        ClsGeneral.setPreferences(AppConstants.ID, "");
        ClsGeneral.setPreferences(AppConstants.USER_ID, "");
        ClsGeneral.setPreferences(AppConstants.NAME, "");
        ClsGeneral.setPreferences(AppConstants.EMAIL, "");
        ClsGeneral.setPreferences(AppConstants.CLAS, "");
        ClsGeneral.setPreferences(AppConstants.SEC, "");
        ClsGeneral.setPreferences(AppConstants.JOINING_DATE, "");
        ClsGeneral.setPreferences(AppConstants.RESIDENTIAL_ADDRESS, "");
        ClsGeneral.setPreferences(AppConstants.PERMANENT_ADDRESS, "");
        ClsGeneral.setPreferences(AppConstants.PROFILE_PIC, "");
        ClsGeneral.setPreferences(AppConstants.STATUS, "");
        ClsGeneral.setPreferences(AppConstants.QUALIFICATION, "");
        ClsGeneral.setPreferences(AppConstants.ALTERNTE_NUMBER, "");
        ClsGeneral.setPreferences(AppConstants.PHONE_NO, "");
        ClsGeneral.setPreferences(AppConstants.WHAT_TEACH, "");
        ClsGeneral.setPreferences(AppConstants.SUBJECT, "");
        ClsGeneral.setPreferences(AppConstants.CLASS_TEACHER_FOR, "");
        ClsGeneral.setPreferences(AppConstants.ADDRESS, "");
        ClsGeneral.setPreferences(AppConstants.FACEBOOK_ID, "");
        ClsGeneral.setPreferences(AppConstants.DESIGNATION, "");
        ClsGeneral.setPreferences(AppConstants.IS_LOGINED, false);
        ClsGeneral.setPreferences(AppConstants.FATHER_NAME, "");
        ClsGeneral.setPreferences(AppConstants.LOGIN_AS, "");
        mActivity.startActivity(new Intent(mActivity, LoginScreenActivity.class));
        mActivity.finishAffinity();
    }

    public static void openBrowser(final Activity mActivity, final String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mActivity.startActivity(i);
    }

    public static void openMail(final Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mActivity.getResources().getString(R.string.principal_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        Intent mailer = Intent.createChooser(intent, null);
        mActivity.startActivity(mailer);

    }


    public static void showLoginFirstDialog(final Activity mActivity) {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.login_first_dialog);
            dialog.setTitle(null);
            dialog.setCanceledOnTouchOutside(false);

            TextView textViewWelcome = dialog.findViewById(R.id.textViewWelcome);
            textViewWelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.startActivity(new Intent(mActivity, LoginScreenActivity.class));
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLeaveMgmtDialog(final Activity mActivity) {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.leave_mgmt_dialog);
            dialog.setTitle(null);
            dialog.setCanceledOnTouchOutside(false);

            RelativeLayout takeLeavesRl = dialog.findViewById(R.id.takeLeavesRl);
            RelativeLayout seeLeavesRl = dialog.findViewById(R.id.seeLeavesRl);
            takeLeavesRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.startActivity(new Intent(mActivity, ApplyLeaveActivity.class));
                    dialog.dismiss();
                }
            });
            seeLeavesRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.startActivity(new Intent(mActivity, SeeLeaveListActivity.class));
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getClassList() {
        List<String> classList;
        classList = new ArrayList<>();

        classList.add("Select Class");
        classList.add("10");
        classList.add("9");
        classList.add("8");
        classList.add("7");
        classList.add("6");
        classList.add("5");
        classList.add("4");
        classList.add("3");
        classList.add("2");
        classList.add("1");
        classList.add("LKG");
        classList.add("UKG");
        classList.add("NURSERY");
        return classList;
    }

    public static List<String> getSectionList() {
        List<String> sectionList = new ArrayList<>();
        sectionList.add("Select Section");
        sectionList.add("A");
        sectionList.add("B");
        return sectionList;
    }

    public static void showAttendanceMgmtDialog(final Activity mActivity) {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.attendance_mgmt_dialog);
            dialog.setTitle(null);
            dialog.setCanceledOnTouchOutside(false);
            List<String> classList = new ArrayList<>(), sectionList = new ArrayList<>();

            classList.addAll(getClassList());

            sectionList.addAll(getSectionList());

            final Spinner classNameSpinner, sectionNameSpinner;
            classNameSpinner = dialog.findViewById(R.id.classNameSpinner);
            sectionNameSpinner = dialog.findViewById(R.id.sectionNameSpinner);

            RelativeLayout takeAttendanceRl = dialog.findViewById(R.id.takeLeavesRl);
            RelativeLayout seeAttendanceRl = dialog.findViewById(R.id.seeLeavesRl);

            ArrayAdapter<String> classAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, classList);
            ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, sectionList);
            classAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            classNameSpinner.setAdapter(classAdapter);
            sectionNameSpinner.setAdapter(sectionAdapter);

            takeAttendanceRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isValidClassAndSection(mActivity, classNameSpinner, sectionNameSpinner)) {
                        Intent mIntent = new Intent(mActivity, TakeAttendenceActivity.class);
                        mIntent.putExtra(AppConstants.CLASS_NAME, classNameSpinner.getSelectedItem().toString());
                        mIntent.putExtra(AppConstants.SECTION_NAME, sectionNameSpinner.getSelectedItem().toString());
                        mActivity.startActivity(mIntent);
                        dialog.dismiss();
                    }
                }
            });
            seeAttendanceRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isValidClassAndSection(mActivity, classNameSpinner, sectionNameSpinner)) {
                        Intent mIntent = new Intent(mActivity, SeeAttendenceActivity.class);
                        mIntent.putExtra(AppConstants.CLASS_NAME, classNameSpinner.getSelectedItem().toString());
                        mIntent.putExtra(AppConstants.SECTION_NAME, sectionNameSpinner.getSelectedItem().toString());
                        mActivity.startActivity(mIntent);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidClassAndSection(Activity mActivity, Spinner classNameSpinner, Spinner sectionNameSpinner) {
        boolean isValid = true;
        if (classNameSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Please select class", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (sectionNameSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(mActivity, "Please select section", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }


    public static void genericAPI(final Context mActivity) {
        final RequestParams requestParams = new RequestParams();
        String url = ApiUrl.BASE_URL + ApiUrl.GENERIC_API;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(mActivity, url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                Log.i("TAG", "Response : " + jsonObject);
                try {
                    String newAppVersion = "", currentAppVersion = BuildConfig.VERSION_CODE + "";
                    if (statusCode == 200 && jsonObject.length() > 0) {
                        if (jsonObject != null && jsonObject.has(AppConstants.DATA)) {
                            JSONArray jsonArrayData = jsonObject.optJSONArray(AppConstants.DATA);
                            if (jsonArrayData != null && jsonArrayData.length() > 0) {
                                JSONObject jsonObject1 = jsonArrayData.getJSONObject(0);
                                if (jsonObject1 != null && jsonObject1.has(AppConstants.VERSION))
                                    newAppVersion = jsonObject1.optString(AppConstants.VERSION);
                                Log.i(TAG, "New App version : " + newAppVersion);
                                if (!newAppVersion.equalsIgnoreCase(currentAppVersion)) {
                                    String msg = String.format(mActivity.getString(R.string.update_app_message), currentAppVersion, newAppVersion);
                                    updateAppDialog(mActivity, msg);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    //trcking user
                    Log.i(TAG, e + "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "Generic Api Failed");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG, "Generic Api Failed");
            }
        });
    }


    public static void updateAppDialog(final Context mActivity, String msg) {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.update_app_dialog);
            dialog.setTitle(null);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            TextView updateTextView = dialog.findViewById(R.id.updateTextView);
            updateTextView.setText(msg);
            TextView textViewLater = dialog.findViewById(R.id.textViewLater);
            TextView textViewUpdate = dialog.findViewById(R.id.textViewUpdate);

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Uri uri = Uri.parse(ApiUrl.PLAYSTORE_LINK);
                        Intent in = new Intent(Intent.ACTION_VIEW, uri);
                        mActivity.startActivity(in);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            textViewLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getSelectedClass(int position) {
        if (position == 1)
            return "10";
        else if (position == 2)
            return "9";
        else if (position == 3)
            return "8";
        else if (position == 4)
            return "7";
        else if (position == 5)
            return "6";
        else if (position == 6)
            return "5";
        else if (position == 7)
            return "4";
        else if (position == 8)
            return "3";
        else if (position == 9)
            return "2";
        else if (position == 10)
            return "1";
        else if (position == 11)
            return "LKG";
        else if (position == 12)
            return "UKG";
        else if (position == 13)
            return "Nursery";
        else
            return "";
    }

    public static String getSelectedSection(int position) {
        if (position == 1)
            return "A";
        else if (position == 2)
            return "B";
        else if (position == 3)
            return "C";
        else if (position == 4)
            return "D";
        else
            return "";
    }

    public static boolean isAttendanceTakenToday(String mStrDate) {
        boolean isAttendanceTakenToday = true;
        String mCurrentDate = getCurrentDate();
        if (mCurrentDate != null && mStrDate != null && mCurrentDate.equalsIgnoreCase(mStrDate)) {
            Date dt = new Date();
            int hours = dt.getHours();
            if (hours >= 1 || hours <= 12) {
                isAttendanceTakenToday = false;
            }
        }
        return isAttendanceTakenToday;
    }

    public static String getDayNameFromDate(String mStrDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-mm-yyyy");
        LocalDate localDate = formatter.parseLocalDate(mStrDate);
        int dayOfWeek = localDate.getDayOfWeek(); // Follows ISO 8601 standard, where Monday = 1, Sunday = 7.
        if (dayOfWeek == 1)
            return AppConstants.MONDAY;
        else if (dayOfWeek == 2)
            return AppConstants.TUESDAY;
        else if (dayOfWeek == 3)
            return AppConstants.WEDNESDAY;
        else if (dayOfWeek == 4)
            return AppConstants.THURSDAY;
        else if (dayOfWeek == 5)
            return AppConstants.FRIDAY;
        else if (dayOfWeek == 6)
            return AppConstants.SATURDAY;
        else
            return "Sunday";
    }

}

