/*package com.example.sam.blutoothsocketreceiver.Utilities;
import android.app.Application;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.sam.blutoothsocketreceiver.QrDisplay;

import org.json.JSONObject;

public class AssignmentSharedPrefs extends Application{

    public static AssignmentSharedPrefs INSTANCE;

    public void onCreate(){
        super.onCreate();
        INSTANCE = AssignmentSharedPrefs.this;
        Log.e("INSTANCE REF", INSTANCE.toString());
    }

    public final static SharedPreferences getSp(){
        Log.e("INSTANCE REF", INSTANCE.toString());
        return INSTANCE.getSharedPreferences(QrDisplay.SHARED_PREF, Activity.MODE_PRIVATE);
    }

    public static String getSp(String key, String defaultValue){
        return AssignmentSharedPrefs.getSp().getString(key, defaultValue);
    }

    public static int getSp(String key, int defaultValue){
        return AssignmentSharedPrefs.getSp().getInt(key, defaultValue);
    }

    public static JSONObject getJSONSp(String key){
        return TeamAssignment.toJSONObject(AssignmentSharedPrefs.getSp(key, ""));
    }

    public static void setSp(String key, String value) {
         AssignmentSharedPrefs.getSp().edit().putString(key, value).apply();
    }

    public static void setSp(String key, int value) {
        AssignmentSharedPrefs.getSp().edit().putInt(key, value).apply();
    }
}
*/
