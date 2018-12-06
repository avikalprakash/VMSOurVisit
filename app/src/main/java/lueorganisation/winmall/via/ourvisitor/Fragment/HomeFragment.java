package lueorganisation.winmall.via.ourvisitor.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lueorganisation.winmall.via.ourvisitor.BranchEventSelection;
import lueorganisation.winmall.via.ourvisitor.FirstStepRegister;
import lueorganisation.winmall.via.ourvisitor.LoginActivity;
import lueorganisation.winmall.via.ourvisitor.MainActivity;
import lueorganisation.winmall.via.ourvisitor.R;
import lueorganisation.winmall.via.ourvisitor.TotalVisitorActivity;
import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button registerVisitors;
    LinearLayout visitorLayout;
    ProgressDialog pDialog;
    TextView todayTotalVisitor, totalVisitor;
    String setType="";
    String BranchID="";
    String EventID="";
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        registerVisitors = view.findViewById(R.id.registerVisitors);
        visitorLayout = view.findViewById(R.id.visitorLayout);
        totalVisitor = view.findViewById(R.id.totalVisitor);
        todayTotalVisitor = view.findViewById(R.id.todayTotalVisitor);
        visitorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getActivity(), TotalVisitorActivity.class);
              startActivity(intent);
            }
        });
        registerVisitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getActivity(), FirstStepRegister.class));
            }
        });

        loadVisitors();

        return view;
    }




    private void loadVisitors() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(getActivity()).getUserId();
        String VisitType_Branch_event = SaveBranchEvent.getInstance(getActivity()).getType();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if (VisitType_Branch_event.equals("1")){
            BranchID = SaveBranchEvent.getInstance(getActivity()).getBranch();
            setType = "branch_id="+BranchID;
        }else if (VisitType_Branch_event.equals("2")) {

            EventID = SaveBranchEvent.getInstance(getActivity()).getEvent();
            setType = "event_id="+EventID;
        }

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.DASHBOARD_COUNTER+setType,
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
                                JSONObject jsonObject = obj.getJSONObject("result");
                                String tVisitor = jsonObject.getString("total_visitor");
                                String ttVisitor = jsonObject.getString("today_visitor");



                                totalVisitor.setText(tVisitor);
                                todayTotalVisitor.setText(ttVisitor);



                            }else {

                                String name = obj.getString("msg");


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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
