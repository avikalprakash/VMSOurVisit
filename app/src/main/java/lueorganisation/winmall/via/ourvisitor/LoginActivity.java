package lueorganisation.winmall.via.ourvisitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.UserSessionManager;

public class LoginActivity extends AppCompatActivity {
     Button signup, signinBtn;
     EditText emailText, passwordText;
     CheckBox remember_me;
     ProgressDialog pDialog;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup = findViewById(R.id.signup);
        signinBtn = findViewById(R.id.signinBtn);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        remember_me = findViewById(R.id.remember_me);
        session = new UserSessionManager(getApplicationContext());
        remember_me.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim();
                String pass = passwordText.getText().toString().trim();
                if (!isValidEmail(email)){
                    emailText.requestFocus();
                    emailText.setError("Invalid Email Id");
                }else if (TextUtils.isEmpty(email)) {
                    emailText.requestFocus();
                    emailText.setError("This Field Is Mandatory");
                }else if (TextUtils.isEmpty(pass)) {
                    passwordText.requestFocus();
                    passwordText.setError("This Field Is Mandatory");
                }else {
                    Login();

                }

            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void Login() {
        String email = emailText.getText().toString().trim();
        String pass = passwordText.getText().toString().trim();

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("loading...");
        pDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("email", email);
        postParam.put("password", pass);


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Urls.LOGIN, new JSONObject(postParam),
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
                                String Access_Token = jobj.getString("Access-Token");
                             //   session.createUserLoginSession(Access_Token);
                                DeleteCache.deleteCache(getApplicationContext());
                                SaveAccessToken.getInstance(getApplicationContext()).saveuserId(Access_Token);
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), BranchEventSelection.class);
                                i.putExtra("Access-Token", Access_Token);
                                startActivity(i);
                                finish();

                            }

                            else
                            {
                                String msg = jobj.getString("msg");
                             //   DeleteCache.deleteCache(getApplicationContext());
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(jsonObjReq);

    }


}
