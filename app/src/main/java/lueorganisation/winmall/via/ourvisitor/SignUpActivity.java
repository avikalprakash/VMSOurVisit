package lueorganisation.winmall.via.ourvisitor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;

public class SignUpActivity extends AppCompatActivity {
    ImageView close;
    Button signup;
    ProgressDialog pDialog;
    EditText emailText, passwordText, mobileText, countryText, cityText, zip_codeText, organisation_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        close = findViewById(R.id.close);
        signup = findViewById(R.id.signup);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        mobileText = findViewById(R.id.mobile);
        countryText = findViewById(R.id.Country);
        organisation_name = findViewById(R.id.organisation_name);
        cityText = findViewById(R.id.City);
        zip_codeText = findViewById(R.id.zipcode);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim();
                String pass = passwordText.getText().toString().trim();
                String mobile = mobileText.getText().toString().trim();
                String orgName = organisation_name.getText().toString().trim();
               // String country = countryText.getText().toString().trim();
               // String city = cityText.getText().toString().trim();
                String zip = zip_codeText.getText().toString().trim();
                if (!isValidEmail(email)){
                    emailText.requestFocus();
                    emailText.setError("Invalid Email Id");
                }
                else if (TextUtils.isEmpty(email)) {
                    emailText.requestFocus();
                    emailText.setError("This Field Is Mandatory");
                }else if (TextUtils.isEmpty(orgName)) {
                    organisation_name.requestFocus();
                    organisation_name.setError("This Field Is Mandatory");
                }
                else if (TextUtils.isEmpty(pass)) {
                    passwordText.requestFocus();
                    passwordText.setError("This Field Is Mandatory");
                }else if (TextUtils.isEmpty(mobile)) {
                    mobileText.requestFocus();
                    mobileText.setError("This Field Is Mandatory");
                }/*else if (TextUtils.isEmpty(country)) {
                    countryText.requestFocus();
                    countryText.setError("This Field Is Mandatory");
                }else if (TextUtils.isEmpty(city)) {
                    cityText.requestFocus();
                    cityText.setError("This Field Is Mandatory");
                }*/else if (TextUtils.isEmpty(zip)) {
                    zip_codeText.requestFocus();
                    zip_codeText.setError("This Field Is Mandatory");
                }else {
                    SignUp();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void SignUp() {
        final String access_token = SaveAccessToken.getInstance(SignUpActivity.this).getUserId();
        String email = emailText.getText().toString().trim();
        String pass = passwordText.getText().toString().trim();
        String mobile = mobileText.getText().toString().trim();
        String org_name = organisation_name.getText().toString().trim();
        String zip = zip_codeText.getText().toString().trim();
        pDialog = new ProgressDialog(SignUpActivity.this);
        pDialog.setMessage("loading...");
        pDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("org_name", org_name);
        postParam.put("mobile", mobile);
        postParam.put("email", email);
        postParam.put("password", pass);
        postParam.put("zip_code", zip);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Urls.SIGN_UP, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jobj) {
                        pDialog.dismiss();
                        Log.d("tag", jobj.toString());

                        try {

                            boolean check = jobj.getBoolean("success");

                            if (check)
                            {

                             //   Toast.makeText(SignUpActivity.this, "Sign Up successfully\n Please Check your email for Login details", Toast.LENGTH_SHORT).show();
                             //  finish();

                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage("Sign Up successfully\n Please Check your email for Login details")
                                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();

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
                headers.put("Access-Token", access_token);
                return headers;
            }
        };

        jsonObjReq.setTag("tag");
        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
        queue.add(jsonObjReq);

    }

}
