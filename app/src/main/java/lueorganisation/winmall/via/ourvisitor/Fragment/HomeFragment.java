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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lueorganisation.winmall.via.ourvisitor.BranchEventSelection;
import lueorganisation.winmall.via.ourvisitor.FirstStepRegister;
import lueorganisation.winmall.via.ourvisitor.LoginActivity;
import lueorganisation.winmall.via.ourvisitor.R;
import lueorganisation.winmall.via.ourvisitor.TotalVisitorActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button registerVisitors;
    LinearLayout visitorLayout;
    ProgressDialog pDialog;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        registerVisitors = view.findViewById(R.id.registerVisitors);
        visitorLayout = view.findViewById(R.id.visitorLayout);
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

//        GetDetails();

        return view;
    }


    public void GetDetails() {


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("loading...");
        pDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("email", "");



        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://condoassist2u.com/eatapp/API/special_promotion_detail.php", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jobj) {
                        pDialog.dismiss();
                        Log.d("tag", jobj.toString());

                        try {

                            String check = jobj.getString("error");

                            if (check.equals("false"))
                            {

                            }

                            else
                            {

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
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjReq.setTag("tag");
        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjReq);

    }
}
