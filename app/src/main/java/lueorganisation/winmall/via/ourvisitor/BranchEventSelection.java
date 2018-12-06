package lueorganisation.winmall.via.ourvisitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.model.Branch;
import lueorganisation.winmall.via.ourvisitor.model.Event;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;
import lueorganisation.winmall.via.ourvisitor.utils.UserSessionManager;

public class BranchEventSelection extends AppCompatActivity {
    Button submitBtn;
    Spinner branchSpinner, eventSpinner;
    private ArrayList<Branch> branchList;
    List<Event> eventList;
    int branchId;
    int eventId;
    private static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_EVENT = "event";
    private RadioButton radioBranch, radioEvent;
    private RadioGroup radioGroup;
    String Access_Token="";
    UserSessionManager session;
   // private Button btnType;
    RelativeLayout branchLayout, eventLayout, Branch_Event_Select;
    String type;
    String radioValue="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_event_selection);
        submitBtn = findViewById(R.id.submitBtn);
        branchSpinner = findViewById(R.id.spinnerBranch);
        eventSpinner = findViewById(R.id.spinnerEvent);
      //  btnType = findViewById(R.id.btnType);
        branchLayout = findViewById(R.id.branchLayout);
        eventLayout = findViewById(R.id.eventLayout);
        Branch_Event_Select = findViewById(R.id.Branch_Event_Select);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        radioBranch = (RadioButton)findViewById(R.id.radioBranch);
        radioEvent = (RadioButton)findViewById(R.id.radioEvent);
        session = new UserSessionManager(getApplicationContext());
        Access_Token= getIntent().getStringExtra("Access-Token");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioBranch:
                        // do operations specific to this selection

                        try {
                            if (radioBranch==null || radioBranch.getText().equals("")) {
                                Toast.makeText(BranchEventSelection.this, "Please select your type", Toast.LENGTH_SHORT).show();
                            }
                            else if (radioBranch.getText().equals("Branch")){
                                radioValue="Branch";
                                branchLayout.setVisibility(View.VISIBLE);
                                submitBtn.setVisibility(View.VISIBLE);
                                eventLayout.setVisibility(View.GONE);

                            }

                        }catch (Exception e){}
                        break;
                    case R.id.radioEvent:
                        try {
                            if (radioEvent==null || radioEvent.getText().equals("")) {
                                Toast.makeText(BranchEventSelection.this, "Please select your type", Toast.LENGTH_SHORT).show();
                            }
                            else if (radioEvent.getText().equals("Event")){
                                radioValue="Event";
                                branchLayout.setVisibility(View.GONE);
                                eventLayout.setVisibility(View.VISIBLE);
                                submitBtn.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){}
                        break;

                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String branch = branchSpinner.getSelectedItem().toString();
                String event = eventSpinner.getSelectedItem().toString();
                if (radioValue.equals("Branch")){
                    type = "1";
                if (branchId==0){
                    Toast.makeText(BranchEventSelection.this, "Please select any Branch", Toast.LENGTH_SHORT).show();
                }else {
                    String br = String.valueOf(branchId);
                    SaveBranchEvent.getInstance(getApplicationContext()).saveBranchEvent(type, br, "");
                    session.createUserLoginSession(Access_Token);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                }else if (radioValue.equals("Event")) {
                    type = "2";
                    if (eventId==0){
                    Toast.makeText(BranchEventSelection.this, "Please select any Event", Toast.LENGTH_SHORT).show();
                }else {
                        String ev = String.valueOf(eventId);
                        SaveBranchEvent.getInstance(getApplicationContext()).saveBranchEvent(type, "", ev);
                        session.createUserLoginSession(Access_Token);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }


            }
        });

        loadBranchList();
        loadEventList();


        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    branchId = branchList.get(i).getOrg_id();

                }catch (Exception e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    eventId = eventList.get(i).getEvent_id();
                    Log.d("event_id", String.valueOf(eventId));
                }catch (Exception e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadBranchList() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(BranchEventSelection.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(BranchEventSelection.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        branchList=new ArrayList<>();





        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_BRANCH_NAME,
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
                                    int id = catObj.getInt("org_id");
                                    String branchName = catObj.getString("name");
                                    if (id >= 0 && branchName != null) {
                                        Branch branch = new Branch(id, branchName);
                                        branchList.add(branch);
                                        populateBranchSpinner();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BranchEventSelection.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }

    private void populateBranchSpinner() {
        List<String> lables = new ArrayList<String>();
      //  branchSpinner.setText("");
        for (int i = 0; i < branchList.size(); i++) {
            lables.add(branchList.get(i).getBranch());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.branchlist_item, lables);
        branchSpinner.setAdapter(spinnerAdapter);
        if(lables != null) {
            branchSpinner.setEnabled(true);
        }
        else {
            branchSpinner.setEnabled(false);
        }
    }

    private void populateEventSpinner() {
        List<String> lables = new ArrayList<String>();
        //  branchSpinner.setText("");
        for (int i = 0; i < eventList.size(); i++) {
            lables.add(eventList.get(i).getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.branchlist_item, lables);
        eventSpinner.setAdapter(spinnerAdapter);
        if(lables != null) {
            eventSpinner.setEnabled(true);
        }
        else {
            eventSpinner.setEnabled(false);
        }
    }


    private void loadEventList() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(BranchEventSelection.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(BranchEventSelection.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        eventList=new ArrayList<>();





        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_EVENT_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        String url = Urls.GET_EVENT_NAME+branchId;
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
                                    int id = catObj.getInt("event_id");
                                    String branchName = catObj.getString("name");
                                    if (id >= 0 && branchName != null) {
                                        Event event = new Event(id, branchName);
                                        eventList.add(event);
                                        populateEventSpinner();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BranchEventSelection.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }
}
