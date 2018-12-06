package lueorganisation.winmall.via.ourvisitor.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lueorganisation.winmall.via.ourvisitor.BranchEventSelection;
import lueorganisation.winmall.via.ourvisitor.R;
import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
   ProgressDialog pDialog;
    String photo,email,name,mobile, org_id;
    CircleImageView logo;
    Button edtpro_btn;
    TextView nme_txt, orgText, email_txt, mobile_txt;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        logo = view.findViewById(R.id.logo);
        nme_txt = view.findViewById(R.id.nme_txt);
        orgText = view.findViewById(R.id.membertype_txt);
        email_txt = view.findViewById(R.id.email_txt);
        mobile_txt = view.findViewById(R.id.mobile_txt);
        edtpro_btn = view.findViewById(R.id.edtpro_btn);
        loadProfileDetails();
        edtpro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment meFragment= new ProfileFragment();
                FragmentTransaction transaction5 = getActivity().getSupportFragmentManager().beginTransaction();
                transaction5.replace(R.id.containermain, meFragment);
                transaction5.addToBackStack(null);
                transaction5.commit();
            }
        });
        return view;
    }

    private void loadProfileDetails() {
        //getting the progressbar
        final String access_token = SaveAccessToken.getInstance(getActivity()).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.GET_PROFILE_DETAILS,
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
                              String name = jsonObject.getString("name");
                                String phone = jsonObject.getString("phone");
                                String email = jsonObject.getString("email");
                                String role = jsonObject.getString("role");
                                String org_id = jsonObject.getString("org_id");
                                String image = jsonObject.getString("src");

                                nme_txt.setText(name);
                                orgText.setText(org_id);
                                email_txt.setText(email);
                                mobile_txt.setText(phone);
                                Glide.with(getActivity()).load(image)
                                        .into(logo);



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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
