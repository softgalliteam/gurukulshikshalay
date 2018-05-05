package softgalli.gurukulshikshalay.preference;

import android.content.SharedPreferences;

import softgalli.gurukulshikshalay.AppController;

/**
 * Created by Welcome on 1/12/2018.
 */

public class MyPreference {

    public static void setSignupSkipped(boolean isSignupSkipped) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("isSignupSkipped", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSignupSkipped1", isSignupSkipped);
        editor.commit();
    }

    public static boolean isSignupSkipped() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("isSignupSkipped", 0);
        return pref.getBoolean("isSignupSkipped1", false);
    }

    public static void setFirstTimeLaunch(boolean isLogin) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("FirstTimeLaunch1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("FirstTimeLaunch", isLogin);
        editor.commit();
    }

    public static boolean isFirstTimeLaunch() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("FirstTimeLaunch1", 0);
        return pref.getBoolean("FirstTimeLaunch", true);
    }

    public static void setPreLoad(boolean isLogin) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("PreLoad1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("PreLoad", isLogin);
        editor.commit();
    }

    public static boolean isPreLoaded() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("PreLoad1", 0);
        return pref.getBoolean("PreLoad", false);
    }

    public static void setLogin(boolean isLogin) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("Login1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("Login", isLogin);
        editor.commit();
    }

    public static boolean isLogined() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("Login1", 0);
        return pref.getBoolean("Login", false);
    }

    public static void setUserName(String name) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("user_name1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_name", name);
        editor.commit();
    }

    public static String getUserName() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("user_name1", 0);
        return pref.getString("user_name", "");
    }

    public static void setProfilPicUrl(String name) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("User_Image", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_Image1", name);
        editor.commit();
    }

    public static String getProfilPicUrl() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("User_Image", 0);
        return pref.getString("User_Image", "");
    }

    public static void setEmailId(String email) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("EmailId", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email_id", email);
        editor.commit();
    }

    public static String getEmailId() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("EmailId", 0);
        return pref.getString("email_id", "");
    }

    public static void setUserMobileNo(String mobileNo) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("UserMobileNo1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("UserMobileNo", mobileNo);
        editor.commit();
    }

    public static String getUserMobileNo() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("UserMobileNo1", 0);
        return pref.getString("UserMobileNo", "");
    }
}
