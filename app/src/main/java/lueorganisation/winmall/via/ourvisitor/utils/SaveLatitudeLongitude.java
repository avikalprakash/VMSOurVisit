package lueorganisation.winmall.via.ourvisitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Fujitsu on 14/06/2017.
 */

public class SaveLatitudeLongitude {
    private static final String SHARED_PREF_NAME = "saveuserid";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";



    private static SaveLatitudeLongitude mInstance;
    private static Context mCtx;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    private SaveLatitudeLongitude(Context context) {
        mCtx = context;
    }

    public static synchronized SaveLatitudeLongitude getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new SaveLatitudeLongitude(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveLatitudeLongitude(String latitude, String longitude)
    {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(TAG_LATITUDE, latitude);
        editor.putString(TAG_LONGITUDE, longitude);
        editor.apply();
      //  Toast.makeText(mCtx, latitude+", "+longitude, Toast.LENGTH_LONG).show();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getLatitude()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_LATITUDE, null);
    }


    public String getLongitude()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_LONGITUDE, null);
    }

    public static void clearBranchData(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        Log.d("data_is_clear", "yes");

    }

}
