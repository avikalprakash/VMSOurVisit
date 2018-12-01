package lueorganisation.winmall.via.ourvisitor.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import lueorganisation.winmall.via.ourvisitor.LoginActivity;

/**
 * Created by Fujitsu on 14/06/2017.
 */

public class SaveBranchEvent {
    private static final String SHARED_PREF_NAME = "saveuserid";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_EVENT = "event";

    private static final String TAG_TYPE = "type";

    private static SaveBranchEvent mInstance;
    private static Context mCtx;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    private SaveBranchEvent(Context context) {
        mCtx = context;
    }

    public static synchronized SaveBranchEvent getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new SaveBranchEvent(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveBranchEvent(String type, String branch, String event)
    {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(TAG_TYPE, type);
        editor.putString(TAG_BRANCH, branch);
        editor.putString(TAG_EVENT, event);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getBranch()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_BRANCH, null);
    }

    public String getType()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TYPE, null);
    }

    public String getEvent()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_EVENT, null);
    }

    public static void clearBranchData(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        Log.d("data_is_clear", "yes");

    }

}
