package lueorganisation.winmall.via.ourvisitor;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lueorganisation.winmall.via.ourvisitor.Fragment.EditProfileFragment;
import lueorganisation.winmall.via.ourvisitor.Fragment.HelpFragment;
import lueorganisation.winmall.via.ourvisitor.Fragment.HomeFragment;
import lueorganisation.winmall.via.ourvisitor.Fragment.ProfileFragment;
import lueorganisation.winmall.via.ourvisitor.Fragment.TermsOfUseFragment;
import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.AbsRuntimePermission;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;
import lueorganisation.winmall.via.ourvisitor.utils.SaveLatitudeLongitude;
import lueorganisation.winmall.via.ourvisitor.utils.UserSessionManager;

public class MainActivity extends AbsRuntimePermission {

    BottomNavigationView bn;
    UserSessionManager session;
    private static final int REQUEST_PERMISSION = 10;

    String branchID="";
    String eventID="";
    String typeID = "";

    LocationManager mlocManager;
    AlertDialog.Builder alert;
    String lat = "";
    String lang = "";
    Timer timer1;
    TimerTask timerTask;
    int count = 0;
    Context context;
    Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alert = new AlertDialog.Builder(this);
        context = this;
        bn = (BottomNavigationView) findViewById(R.id.bnhomemain);
        session = new UserSessionManager(getApplicationContext());

        if (session.checkLogin()) {
            finish();
        }
        try {
            typeID = SaveBranchEvent.getInstance(MainActivity.this).getType();
        }catch (Exception e){}




        HomeFragment fhome = new HomeFragment();
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.containermain, fhome);
        trans.commit();

        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navinstruction:

                        HomeFragment fhome = new HomeFragment();
                        android.support.v4.app.FragmentTransaction trans1 = getSupportFragmentManager().beginTransaction();
                        trans1.replace(R.id.containermain, fhome);
                        trans1.commit();

                        break;
                    case R.id.profile:
                        EditProfileFragment meFragment= new EditProfileFragment();
                        FragmentTransaction transaction5 = getSupportFragmentManager().beginTransaction();
                        transaction5.replace(R.id.containermain, meFragment);
                        transaction5.addToBackStack(null);
                        transaction5.commit();

                        break;
                    case R.id.help:
                        HelpFragment helpFragment= new HelpFragment();
                        FragmentTransaction transaction6 = getSupportFragmentManager().beginTransaction();
                        transaction6.replace(R.id.containermain, helpFragment);
                        transaction6.addToBackStack(null);
                        transaction6.commit();
                        break;


                /*    case R.id.terms_condition:

                        TermsOfUseFragment termsOfUseFragment= new TermsOfUseFragment();
                        FragmentTransaction transaction8 = getSupportFragmentManager().beginTransaction();
                        transaction8.replace(R.id.containermain, termsOfUseFragment);
                        transaction8.addToBackStack(null);
                        transaction8.commit();

                        break;*/

                    case R.id.logout:
                        proceedMessage();

                        break;

                }
                return true;
            }
        });


        requestAppPermissions(new String[]{

                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                 //       Manifest.permission.ACCESS_FINE_LOCATION,
                 //       Manifest.permission.RECORD_AUDIO,
                //        Manifest.permission.ACCESS_COARSE_LOCATION
                },

                R.string.msg, REQUEST_PERMISSION);

    //    getGpsLocation();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void proceedMessage() {
        final Dialog dialog = new Dialog(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.alert_logout, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(subView);
        // dialog.setTitle("Title...");
        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);
        //final MyEditText otp=(MyEditText)dialog.findViewById(R.id.otp);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SaveBranchEvent.clearBranchData();
                }catch (Exception e){}
                session.logoutUser();
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
