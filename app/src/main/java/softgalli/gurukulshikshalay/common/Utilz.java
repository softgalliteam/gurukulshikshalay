package softgalli.gurukulshikshalay.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.activity.LoginScreenActivity;
import softgalli.gurukulshikshalay.preference.MyPreference;


public class Utilz {
    private static String TAG = Utilz.class.getSimpleName();
    static ProgressDialog dialog;


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
            builder.setTitle("No Internet Connection");
            builder.setMessage("No Internet connection. Please check your network settings.");
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

}

