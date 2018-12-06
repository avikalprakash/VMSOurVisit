package lueorganisation.winmall.via.ourvisitor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.adapter.TotalVisitorListAdaptor;
import lueorganisation.winmall.via.ourvisitor.local.CustomAdapter;
import lueorganisation.winmall.via.ourvisitor.local.DataModel;
import lueorganisation.winmall.via.ourvisitor.model.Branch;
import lueorganisation.winmall.via.ourvisitor.model.VisitorListPojo;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;

public class TotalVisitorActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    ListView visitorList;
    ImageView close;
    private static CustomAdapter adapter;
    private ArrayList<VisitorListPojo> visitorListPojos;
    String setType="";
    String BranchID="";
    String EventID="";
    TextView no_recodrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_visitor);
        visitorList = findViewById(R.id.visitorList);
        close = findViewById(R.id.close);
        no_recodrs = findViewById(R.id.no_recodrs);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close current screen
                finish();
            }
        });


        visitorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = visitorListPojos.get(i).getName();
                String visitor_type = visitorListPojos.get(i).getVisit_type();
                String city = visitorListPojos.get(i).getCity();
                String state = visitorListPojos.get(i).getState();
                String country = visitorListPojos.get(i).getCountry();
                String datetime = visitorListPojos.get(i).getDatetime();
                String email = visitorListPojos.get(i).getEmail();
                String mobile =  visitorListPojos.get(i).getMobile();
                String visit_purpose = visitorListPojos.get(i).getVisit_purpose();
                String profilePic = visitorListPojos.get(i).getImage();
                String cardPic = visitorListPojos.get(i).getId_proof();

                AlertDialog( name, visitor_type, city, state, country, datetime, email, mobile, visit_purpose, profilePic,
                        cardPic);
            }
        });

        loadBranchList();
    }

    public void AlertDialog(String name,String visitor_type, String city, String state, String country, String datetime,
                            String email, String mobile, String visit_purpose, String profilePic, String cardPic
                            ){
        LayoutInflater inflater = LayoutInflater.from(TotalVisitorActivity.this);
        View subView = inflater.inflate(R.layout.visitors_detail_alert, null);
        final ImageView close = (ImageView) subView.findViewById(R.id.close);
        final TextView emailText = (TextView)subView.findViewById(R.id.emailText);
        final TextView mobileText = (TextView)subView.findViewById(R.id.mobileText);
        final TextView locationText = (TextView)subView.findViewById(R.id.locationText);
        final TextView visit_purposeText = (TextView)subView.findViewById(R.id.visit_purposeText);
        final CircleImageView profilePicture = (CircleImageView)subView.findViewById(R.id.profilePic);
        final TextView nameText = (TextView)subView.findViewById(R.id.name);
        final TextView typeText = (TextView)subView.findViewById(R.id.type);
        final TextView addressText = (TextView)subView.findViewById(R.id.address);
        final TextView dateText = (TextView)subView.findViewById(R.id.date);
        final ImageView cardImage = (ImageView)subView.findViewById(R.id.cardImage);



        Drawable dr = getResources().getDrawable(R.drawable.email);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));
        emailText.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);


        Drawable dr2 = getResources().getDrawable(R.drawable.mobile);
        Bitmap bitmap2 = ((BitmapDrawable) dr2).getBitmap();
        Drawable d2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 28, 40, true));
        mobileText.setCompoundDrawablesWithIntrinsicBounds(d2,null,null,null);


        Drawable dr3 = getResources().getDrawable(R.drawable.location);
        Bitmap bitmap3 = ((BitmapDrawable) dr3).getBitmap();
        Drawable d3 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 30, 30, true));
        locationText.setCompoundDrawablesWithIntrinsicBounds(d3,null,null,null);


        Drawable dr4 = getResources().getDrawable(R.drawable.visitor_purpose);
        Bitmap bitmap4 = ((BitmapDrawable) dr4).getBitmap();
        Drawable d4 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap4, 30, 30, true));
        visit_purposeText.setCompoundDrawablesWithIntrinsicBounds(d4,null,null,null);

        emailText.setText("  "+email);
        mobileText.setText("  "+mobile);
        addressText.setText(" "+city+", "+state+", "+country);
        visit_purposeText.setText("  "+visit_purpose);
        nameText.setText(name);
        typeText.setText(visitor_type);
        dateText.setText(datetime);

        Glide.with(getApplicationContext()).load(profilePic).into(profilePicture);
        Glide.with(getApplicationContext()).load(cardPic).into(cardImage);




        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(subView);
        final AlertDialog alertDialog = builder1.show();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             alertDialog.dismiss();
            }
        });


    }


    private void loadBranchList() {
        //getting the progressbar
        String VisitType_Branch_event = SaveBranchEvent.getInstance(TotalVisitorActivity.this).getType();
        final String access_token = SaveAccessToken.getInstance(TotalVisitorActivity.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(TotalVisitorActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if (VisitType_Branch_event.equals("1")){
            BranchID = SaveBranchEvent.getInstance(TotalVisitorActivity.this).getBranch();
            setType = "branch_id="+BranchID;
        }else if (VisitType_Branch_event.equals("2")) {

            EventID = SaveBranchEvent.getInstance(TotalVisitorActivity.this).getEvent();
            setType = "event_id="+EventID;
        }
        visitorListPojos=new ArrayList<>();





        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.VISITOR_LIST+setType,
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
                                    VisitorListPojo visitorListPojo = new VisitorListPojo();
                                    visitorListPojo.setVisit_id(catObj.getInt("visit_id"));
                                    visitorListPojo.setVisitor_id(catObj.getInt("visitor_id"));
                                    visitorListPojo.setName(catObj.getString("name"));
                                    visitorListPojo.setEmail(catObj.getString("email"));
                                    visitorListPojo.setMobile(catObj.getString("mobile"));
                                    visitorListPojo.setCountry(catObj.getString("country"));
                                    visitorListPojo.setCity(catObj.getString("city"));
                                    visitorListPojo.setState(catObj.getString("state"));
                                    visitorListPojo.setId_proof(catObj.getString("id_proof"));
                                    visitorListPojo.setImage(catObj.getString("image"));
                                    visitorListPojo.setVisit_purpose(catObj.getString("visit_purpose"));
                                    visitorListPojo.setVisit_type(catObj.getString("visit_type"));
                                    visitorListPojo.setTime(catObj.getString("time"));
                                    visitorListPojo.setDatetime(catObj.getString("datetime"));
                                    visitorListPojos.add(visitorListPojo);
                                }
                                TotalVisitorListAdaptor totalVisitorListAdaptor = new TotalVisitorListAdaptor(TotalVisitorActivity.this, visitorListPojos);
                                visitorList.setAdapter(totalVisitorListAdaptor);
                            }else {

                                no_recodrs.setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(TotalVisitorActivity.this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }
}
