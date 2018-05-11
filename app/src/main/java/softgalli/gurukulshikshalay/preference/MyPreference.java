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

    public static void setLoginedAs(String name) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("LoginedAs1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LoginedAs", name);
        editor.commit();
    }

    public static String getLoginedAs() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("LoginedAs1", 0);
        return pref.getString("LoginedAs", "");
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

    public static void setUserId(int userId) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("UserId1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("UserId", userId);
        editor.commit();
    }

    public static int getUserId() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("UserId1", 0);
        return pref.getInt("UserId", 0);
    }
}
