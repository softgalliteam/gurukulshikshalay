package softgalli.gurukulshikshalay.common;


import android.content.Context;
import android.content.SharedPreferences;

import softgalli.gurukulshikshalay.AppController;

public class ClsGeneral {
    public static void setPreferences(String key, String value) {
        SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences(
                "WED_APP", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences(
                "WED_APP", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolPreferences(String key) {
        SharedPreferences prefs = AppController.getInstance().getSharedPreferences("WED_APP",
                Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public static String getStrPreferences(String key) {
        SharedPreferences prefs = AppController.getInstance().getSharedPreferences("WED_APP",
                Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static int getIntPreferences(String key) {
        SharedPreferences prefs = AppController.getInstance().getSharedPreferences("WED_APP",
                Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }


}
