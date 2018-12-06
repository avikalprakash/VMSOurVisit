package lueorganisation.winmall.via.ourvisitor.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lueorganisation.winmall.via.ourvisitor.DeleteCache;
import lueorganisation.winmall.via.ourvisitor.MainActivity;
import lueorganisation.winmall.via.ourvisitor.R;
import lueorganisation.winmall.via.ourvisitor.SignUpActivity;
import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;


public class EditProfileFragment extends Fragment {

    TextView locationText;
    TextView emailText;
    TextView mobileText;
    TextView datetimeText;
    TextView orgName;
    Button changePassword, closeAccount;
    CircleImageView circleLogo;
    ProgressDialog pDialog;
    boolean value=false;
    String old_pass="";
    String new_pass="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        locationText = (TextView)view.findViewById(R.id.address);
        emailText = (TextView)view.findViewById(R.id.email);
        mobileText = (TextView)view.findViewById(R.id.mobile);
        datetimeText = (TextView)view.findViewById(R.id.datetime);
        orgName = (TextView) view.findViewById(R.id.org_name);
        changePassword = (Button)view.findViewById(R.id.change_password);
        circleLogo = (CircleImageView)view.findViewById(R.id.logo);
        Drawable dr1 = getResources().getDrawable(R.drawable.location);
        Bitmap bitmap1 = ((BitmapDrawable) dr1).getBitmap();
        Drawable d1= new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1, 28, 40, true));
        locationText.setCompoundDrawablesWithIntrinsicBounds(d1,null,null,null);

        Drawable dr2 = getResources().getDrawable(R.drawable.email);
        Bitmap bitmap2 = ((BitmapDrawable) dr2).getBitmap();
        Drawable d2= new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 30, 30, true));
        emailText.setCompoundDrawablesWithIntrinsicBounds(d2,null,null,null);

        Drawable dr3 = getResources().getDrawable(R.drawable.mobile);
        Bitmap bitmap3 = ((BitmapDrawable) dr3).getBitmap();
        Drawable d3= new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 28, 40, true));
        mobileText.setCompoundDrawablesWithIntrinsicBounds(d3,null,null,null);

        loadProfileDetails();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });



        return view;
    }

    public void changePassword() {
        final Dialog dialog = new Dialog(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.alert_password_change, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(subView);
        final EditText previousPass = (EditText)dialog.findViewById(R.id.previous_password);
        final EditText newPass = (EditText)dialog.findViewById(R.id.passwordNew);
        final EditText confirmPass = (EditText)dialog.findViewById(R.id.passwordConfirm);
        final ImageView cancle = (ImageView)dialog.findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        Button save = (Button) dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prePass = previousPass.getText().toString().trim();
                String newP = newPass.getText().toString().trim();
                String conPass = confirmPass.getText().toString().trim();
                   if (prePass.equals("")){
                       previousPass.setError("Please Enter previous password");
                   }else if (newP.equals("")){
                       newPass.setError("Please Enter New password");
                   }else if (conPass.equals("")){
                    newPass.setError("Please Enter Confirm  password");
                }else if (!newP.equals(conPass)){
                    confirmPass.setError("Please confirm correct password");
                }else{
                       changePasswordAPI(prePass, conPass);
                       if (value) {
                           dialog.dismiss();
                       }
                }

            }
        });

        dialog.show();
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
                                String opr_id = jsonObject.getString("opr_id");
                                String image = jsonObject.getString("src");
                                String image_org = jsonObject.getString("src_org");
                                String org_name = jsonObject.getString("org_name");
                                String reg_date = jsonObject.getString("reg_date");


                                orgName.setText(name);
                                emailText.setText("  "+email);
                                mobileText.setText("  "+phone);
                                datetimeText.setText("Member Since "+reg_date);
                                Glide.with(getActivity()).load(image_org)
                                        .into(circleLogo);



                             //   DeleteCache.deleteCache(getContext());

                            }else {

                                String name = obj.getString("msg");
                                Toast.makeText(getActivity(), "Your token has beed expire", Toast.LENGTH_SHORT).show();

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


    public void changePasswordAPI(String prePass, String newPass) {
        final String access_token = SaveAccessToken.getInstance(getActivity()).getUserId();


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("loading...");
        pDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("old_pass", prePass);
        postParam.put("new_pass", newPass);


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Urls.CHANGE_PASSWORD, new JSONObject(postParam),
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
                                   Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                   value=true;
                            }

                            else
                            {
                                String msg = jobj.getString("msg");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                value=true;
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
                headers.put("Access-Token", access_token);
                return headers;
            }
        };

        jsonObjReq.setTag("tag");
        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjReq);

    }
}
