package lueorganisation.winmall.via.ourvisitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.model.Branch;
import lueorganisation.winmall.via.ourvisitor.model.City;
import lueorganisation.winmall.via.ourvisitor.model.Country;
import lueorganisation.winmall.via.ourvisitor.model.Event;
import lueorganisation.winmall.via.ourvisitor.model.State;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;
import lueorganisation.winmall.via.ourvisitor.utils.SaveLatitudeLongitude;

public class FinalStepRegister extends AppCompatActivity implements View.OnClickListener {
    Button male, female, submit;
    ImageView close;
    String gender="";
    EditText visitorText,  addressText, cityEvent;
    Spinner countryEvent, stateEvent;
    TextView genderText;
    String name, mobile, email, profile_pic, card_pic, visitor_type;
    private ArrayList<Country> countryArrayList;
    private ArrayList<State> stateArrayList;
    private ArrayList<City> cityArrayList;
    int countryId;
    int stateId;
    int cityId;
    String country="";
    String state="";
    String city="";
    String visitor_purpose="";
    String address="";

    ProgressDialog pDialog;
    String BranchID="";
    String EventID="";
    String VisitType_Branch_event="";
    String imageProfile;
    String imageCard;
    Context context;
    boolean isNew;
    String oldId="";
    int getID;
    int getStateID;
    boolean y=false;
    boolean xy=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_step_register);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        submit = findViewById(R.id.submit);
        close = findViewById(R.id.close);
        visitorText = findViewById(R.id.visitor_purpose);
        countryEvent = findViewById(R.id.countryEvent);
        stateEvent = findViewById(R.id.stateEvent);
        cityEvent = findViewById(R.id.cityEvent);
        addressText = findViewById(R.id.address);
        genderText = findViewById(R.id.genderText);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        submit.setOnClickListener(this);
        close.setOnClickListener(this);
        context = this;
        name = getIntent().getStringExtra("name");
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("email");
        oldId = getIntent().getStringExtra("old_id");
        imageProfile = getIntent().getStringExtra("profile_pic");
        imageCard = getIntent().getStringExtra("card_pic");
        visitor_type = getIntent().getStringExtra("visitor_type");

        gender = getIntent().getStringExtra("gender");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        country = getIntent().getStringExtra("country");
        visitor_purpose = getIntent().getStringExtra("visitor_purpose");
        isNew = getIntent().getBooleanExtra("isNew", isNew);


        if (!gender.equals("")){
            if (gender.equals("Male")){
                male.setBackgroundResource(R.drawable.btn_bg_yellow);
                female.setBackgroundResource(R.drawable.btn_bg_white);


                //set text color
                male.setTextColor(getResources().getColor(R.color.white));
                female.setTextColor(getResources().getColor(R.color.black));
            }else if (gender.equals("Female")){
                //set background color
                female.setBackgroundResource(R.drawable.btn_bg_yellow);
                male.setBackgroundResource(R.drawable.btn_bg_white);


                //set text color
                female.setTextColor(getResources().getColor(R.color.white));
                male.setTextColor(getResources().getColor(R.color.black));
            }
        }
        if (!address.equals("")){
            addressText.setText(address);
        }
        if (!city.equals("")){
            cityEvent.setText(city);
        }
        if (!visitor_purpose.equals("")){
            visitorText.setText(visitor_purpose);
        }






       try {
           BranchID = SaveBranchEvent.getInstance(FinalStepRegister.this).getBranch();
           EventID = SaveBranchEvent.getInstance(FinalStepRegister.this).getEvent();
           VisitType_Branch_event = SaveBranchEvent.getInstance(FinalStepRegister.this).getType();
       }catch (Exception e){}





        countryEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    countryId = countryArrayList.get(i).getId();
                    country = countryArrayList.get(i).getName();
                    loadStateList();
                }catch (Exception e){}
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        stateEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    stateId = stateArrayList.get(i).getId();
                    state = stateArrayList.get(i).getName();
                  //  loadCityList();
                }catch (Exception e){}
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

     /*   citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    cityId = cityArrayList.get(i).getId();
                    city = cityArrayList.get(i).getName();

                }catch (Exception e){}
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        loadCountryList();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.male:
                gender = "Male";
                //set background color
                male.setBackgroundResource(R.drawable.btn_bg_yellow);
                female.setBackgroundResource(R.drawable.btn_bg_white);


                //set text color
                male.setTextColor(getResources().getColor(R.color.white));
                female.setTextColor(getResources().getColor(R.color.black));


                //go to next step
                break;

            case R.id.female:
                gender = "Female";
                //set background color
                female.setBackgroundResource(R.drawable.btn_bg_yellow);
                male.setBackgroundResource(R.drawable.btn_bg_white);


                //set text color
                female.setTextColor(getResources().getColor(R.color.white));
                male.setTextColor(getResources().getColor(R.color.black));

                break;

            case R.id.submit:

                String visitor = visitorText.getText().toString().trim();
                String city = cityEvent.getText().toString().trim();
                String address = addressText.getText().toString().trim();
               if (TextUtils.isEmpty(visitor)) {
                   visitorText.requestFocus();
                   visitorText.setError("This Field Is Mandatory");
                }else if (TextUtils.isEmpty(address)) {
                    addressText.requestFocus();
                    addressText.setError("This Field Is Mandatory");
                }else if (TextUtils.isEmpty(city)) {
                   cityEvent.requestFocus();
                   cityEvent.setError("This Field Is Mandatory");
               }
                else if(gender.isEmpty()){
                   genderText.requestFocus();
                   genderText.setError("please select gender");
                   Toast.makeText(this, "please select gender", Toast.LENGTH_SHORT).show();
               }else if (country.equals("Country")){
                   Toast.makeText(this, "please select country", Toast.LENGTH_SHORT).show();
               }
               else if (state.equals("State")){
                   Toast.makeText(this, "please select State", Toast.LENGTH_SHORT).show();
               }
               else if (city.equals("City")){
                   Toast.makeText(this, "please select City", Toast.LENGTH_SHORT).show();
               }
               else {
                   VisitorRegister();

                }

                break;

            case R.id.close:
                finish();
                break;
        }
    }

    private void loadCountryList() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(FinalStepRegister.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(FinalStepRegister.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        countryArrayList=new ArrayList<>();





        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.COUNTRY_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion

                        progressDialog.dismiss();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            boolean check = obj.getBoolean("success");

                            if (check){


                                JSONArray jsonArray = null;

                                jsonArray = obj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject catObj = jsonArray.getJSONObject(i);
                                    int id = catObj.getInt("id");
                                    String name = catObj.getString("name");
                                    if (id >= 0 && name != null) {
                                        Country country = new Country(id, name);
                                        countryArrayList.add(country);
                                        populateCountrySpinner();
                                    }


                                }
                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                }

        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");
                params.put("Access-Token", access_token);

                return params;
            }
        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(FinalStepRegister.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }

    private void populateCountrySpinner() {
        ArrayList<String> lables = new ArrayList<String>();
        //  branchSpinner.setText("");
        for (int i = 0; i < countryArrayList.size(); i++) {
            lables.add(countryArrayList.get(i).getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.branchlist_item, lables);
        countryEvent.setAdapter(spinnerAdapter);

        if(lables != null) {
            countryEvent.setEnabled(true);
            //for testing
            if (isNew) {
                try {
                    String locale;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        locale = context.getResources().getConfiguration().getLocales().get(0).getCountry();
                    } else {
                        locale = context.getResources().getConfiguration().locale.getCountry();
                    }
                    Locale loc = new Locale("",locale);
                    String countryName =  loc.getDisplayCountry();
                    if(lables.size()>0) {
                        int i;

                        for (i= 0; i < countryArrayList.size(); i++) {
                            if (countryName.equals(countryArrayList.get(i).getName())){
                                getID = countryArrayList.get(i).getId();
                            //    Toast.makeText(context, String.valueOf(getID), Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        countryEvent.setSelection(getID);
                                    }
                                }, 100);


                                y=true;
                                break;
                            }
                            if (y){
                                break;
                            }

                        }
                    }

                }catch (Exception e){}
            }else {
                try {
                    if(lables.size()>0) {
                        int i;
                        for (i= 0; i < countryArrayList.size(); i++) {
                            String c = countryArrayList.get(i).getName();
                            if (country.equals(countryArrayList.get(i).getName())){
                                getID = countryArrayList.get(i).getId();
                                //    Toast.makeText(context, String.valueOf(getID), Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        countryEvent.setSelection(getID);
                                    }
                                }, 100);


                                y=true;
                                break;
                            }
                            if (y){
                                break;
                            }

                        }
                    }

                }catch (Exception e){}
            }

            //till here
        }
        else {
            countryEvent.setEnabled(false);
        }
    }

    private void populateStateSpinner() {
        ArrayList<String> lables = new ArrayList<String>();
        //  branchSpinner.setText("");
        for (int i = 0; i < stateArrayList.size(); i++) {
            lables.add(stateArrayList.get(i).getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.branchlist_item, lables);
        stateEvent.setAdapter(spinnerAdapter);
        if(lables != null) {
            stateEvent.setEnabled(true);
           if (!isNew){
                try {
                    if(lables.size()>0) {
                        int j;
                        String s;
                        for (j= 0; j < stateArrayList.size(); j++) {
                            s = stateArrayList.get(j).getName();
                           // Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                            Log.d("state", s);

                            if (state.equals(stateArrayList.get(j).getName())){
                                getStateID = stateArrayList.get(j).getId();
                                //    Toast.makeText(context, String.valueOf(getID), Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        stateEvent.setSelection(getStateID);
                                    }
                                }, 100);


                                xy=true;
                                break;
                            }
                            if (xy){
                                break;
                            }
                        }
                    }

                }catch (Exception e){}
            }
        }
        else {
            stateEvent.setEnabled(false);
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(bmp == null){

        }else {
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

/*    private void populateCitySpinner() {
        List<String> lables = new ArrayList<String>();
        //  branchSpinner.setText("");
        for (int i = 0; i < cityArrayList.size(); i++) {
            lables.add(cityArrayList.get(i).getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.branchlist_item, lables);
        citySpinner.setAdapter(spinnerAdapter);
        if(lables != null) {
            citySpinner.setEnabled(true);
        }
        else {
            citySpinner.setEnabled(false);
        }
    }*/


    private void loadStateList() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(FinalStepRegister.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(FinalStepRegister.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        stateArrayList=new ArrayList<>();





        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.STATE_LIST+countryId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        String url = Urls.STATE_LIST+countryId;
                        Log.d("url", url);
                        progressDialog.dismiss();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            boolean check = obj.getBoolean("success");

                            if (check){


                                JSONArray jsonArray = null;

                                jsonArray = obj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject catObj = jsonArray.getJSONObject(i);
                                    int id = catObj.getInt("id");
                                    String name = catObj.getString("name");
                                    if (id >= 0 && name != null) {
                                        State event = new State(id, name);
                                        stateArrayList.add(event);
                                        populateStateSpinner();
                                    }


                                }
                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                }

        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");
                params.put("Access-Token", access_token);

                return params;
            }
        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(FinalStepRegister.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }



  /*  private void loadCityList() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(FinalStepRegister.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(FinalStepRegister.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        cityArrayList=new ArrayList<>();





        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.CITY_LIST+stateId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        String url = Urls.CITY_LIST+stateId;
                        Log.d("url", url);
                        progressDialog.dismiss();

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            boolean check = obj.getBoolean("success");

                            if (check){


                                JSONArray jsonArray = null;

                                jsonArray = obj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject catObj = jsonArray.getJSONObject(i);
                                    int id = catObj.getInt("id");
                                    String name = catObj.getString("name");
                                    if (id >= 0 && name != null) {
                                        City city = new City(id, name);
                                        cityArrayList.add(city);
                                       // populateCitySpinner();
                                    }


                                }
                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                }

        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");
                params.put("Access-Token", access_token);

                return params;
            }
        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(FinalStepRegister.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }*/


    public void VisitorRegister() {
        String visitor_for="";
        final String access_token = SaveAccessToken.getInstance(FinalStepRegister.this).getUserId();
        String visitor = visitorText.getText().toString().trim();
        String address = addressText.getText().toString().trim();
        String city = cityEvent.getText().toString().trim();

        if (VisitType_Branch_event.equals("1")){
            visitor_for= "Branch";
        }else if (VisitType_Branch_event.equals("2")){
            visitor_for= "Event";
        }
        pDialog = new ProgressDialog(FinalStepRegister.this);
        pDialog.setMessage("loading...");
        pDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("v_name", name);
        postParam.put("visitor_for", visitor_for);
        postParam.put("v_email", email);
        postParam.put("v_mobile", mobile);
        postParam.put("v_gender", gender);
        postParam.put("v_country", country);
        postParam.put("v_state", state);
        postParam.put("v_city", city);
        postParam.put("v_address", address);
        postParam.put("zip_code", "");
        postParam.put("v_v_purpose", visitor);
        postParam.put("v_type", visitor_type);
        postParam.put("v_event_id", EventID);
        postParam.put("v_old_id", oldId);
        postParam.put("is_new", String.valueOf(isNew));
        postParam.put("v_org_id", BranchID);
        postParam.put("base64_id_proof", imageCard);
        postParam.put("base64_image", imageProfile);



        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Urls.VISITOR_REGISTER, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jobj) {
                        pDialog.dismiss();
                        Log.d("tag", jobj.toString());

                        try {

                            boolean check = jobj.getBoolean("success");

                            if (check)
                            {
                                String msg = jobj.getString("msg");

                                Toast.makeText(FinalStepRegister.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            else
                            {
                                String msg = jobj.getString("msg");
                                Toast.makeText(FinalStepRegister.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d("tag", "Error: " + error.getMessage());
                //  hideProgressDialog();
            }
        })

        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Access-Token", access_token);
                return headers;
            }
        };

        jsonObjReq.setTag("tag");
        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(FinalStepRegister.this);
        queue.add(jsonObjReq);

    }
}
