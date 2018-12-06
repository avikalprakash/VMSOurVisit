package lueorganisation.winmall.via.ourvisitor;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import id.zelory.compressor.Compressor;
import lueorganisation.winmall.via.ourvisitor.model.FileUtil;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lueorganisation.winmall.via.ourvisitor.Urls.Urls;
import lueorganisation.winmall.via.ourvisitor.utils.SaveAccessToken;
import lueorganisation.winmall.via.ourvisitor.utils.SaveBranchEvent;

public class SecondStepRegister extends AppCompatActivity {
    ImageView close;
    CircleImageView profile_pic;
    ImageView card_pic;
    Uri imageUriProfilePic;
    Uri imageUriCardPic;
    Button next;
    Uri image2;
    EditText nameText, emailText, moboileText;

    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 2;

    private int PICK_IMAGE_REQUEST_NEW = 3;
    private int CAMERA_REQUEST_NEW = 4;
    Uri image1;
    AlertDialog dialog;
    Bitmap bitmapProfilePic;
    Bitmap bitmapCardPic;
    String visitor_type;
    ByteArrayOutputStream stream;
    boolean text=true;
    String gender="";
    String address="";
    String city="";
    String state="";
    String country="";
    String visitor_purpose="";
    boolean isNew=true;
    int old_id=0;

    private File actualImagePic;
    private File actualImageCard;
    private File compressedImage;
    Bitmap  compressedImageBitmapPic;
    Bitmap  compressedImageBitmapCard;

    Bitmap bitmap;
    String imagePic="";
    String imageCard="";
    String VisitType_Branch_event="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step_register);
        profile_pic = findViewById(R.id.profile_pic);
        card_pic = findViewById(R.id.card_pic);
        close = findViewById(R.id.close);
        next = findViewById(R.id.next);
        nameText = findViewById(R.id.name);
        emailText = findViewById(R.id.email);
        moboileText = findViewById(R.id.mobile);
         stream = new ByteArrayOutputStream();
        VisitType_Branch_event = SaveBranchEvent.getInstance(SecondStepRegister.this).getType();
        try {
            visitor_type = getIntent().getStringExtra("visitor_type");
        }catch (Exception e){}

       /* emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                    String mobile = moboileText.getText().toString().trim();
                    if (!mobile.equals("")) {
                        String m = "mobile";
                        checkVisitorAvailability(mobile, m);
                    }
            }
        });

        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                    String email = emailText.getText().toString().trim();
                    if (!email.equals("")) {
                        String e = "email";
                        checkVisitorAvailability(email, e);
                    }

            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    compressedImageBitmapPic = new Compressor(SecondStepRegister.this).compressToBitmap(actualImagePic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    compressedImageBitmapCard = new Compressor(SecondStepRegister.this).compressToBitmap(actualImageCard);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageCard = getStringImage(compressedImageBitmapCard);
                imagePic = getStringImage(compressedImageBitmapPic);
                String email = emailText.getText().toString().trim();
                String name = nameText.getText().toString().trim();
                String mobile = moboileText.getText().toString().trim();


                if (!isNew) {

                    Intent i = new Intent(getApplicationContext(), FinalStepRegister.class);
                    i.putExtra("mobile", mobile);
                    i.putExtra("email", email);
                    i.putExtra("name", name);
                    i.putExtra("profile_pic", imagePic);
                    i.putExtra("card_pic", imageCard);
                    i.putExtra("visitor_type", visitor_type);
                    //for auto fill
                    i.putExtra("gender", gender);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("country", country);
                    i.putExtra("visitor_purpose", visitor_purpose);
                    i.putExtra("isNew", isNew);
                    i.putExtra("old_id", String.valueOf(old_id));
                    startActivity(i);
                } else {

                    if (VisitType_Branch_event.equals("2")){
                        if (TextUtils.isEmpty(name)) {
                            nameText.requestFocus();
                            nameText.setError("This Field Is Mandatory");
                        } else if (!isValidEmail(email)) {
                            emailText.requestFocus();
                            emailText.setError("Invalid Email Id");
                        } else if (TextUtils.isEmpty(email)) {
                            emailText.requestFocus();
                            emailText.setError("This Field Is Mandatory");
                        } else if (TextUtils.isEmpty(mobile)) {
                            moboileText.requestFocus();
                            moboileText.setError("This Field Is Mandatory");
                        }  else {

                            Intent i = new Intent(getApplicationContext(), FinalStepRegister.class);
                            i.putExtra("mobile", mobile);
                            i.putExtra("email", email);
                            i.putExtra("name", name);
                            i.putExtra("profile_pic", imagePic);
                            i.putExtra("card_pic", imageCard);
                            i.putExtra("visitor_type", visitor_type);
                            //for auto fill
                            i.putExtra("gender", gender);
                            i.putExtra("address", address);
                            i.putExtra("city", city);
                            i.putExtra("state", state);
                            i.putExtra("country", country);
                            i.putExtra("visitor_purpose", visitor_purpose);
                            i.putExtra("isNew", isNew);
                            startActivity(i);
                        }

                    }else if(VisitType_Branch_event.equals("1")){
                        if (TextUtils.isEmpty(name)) {
                            nameText.requestFocus();
                            nameText.setError("This Field Is Mandatory");
                        } else if (!isValidEmail(email)) {
                            emailText.requestFocus();
                            emailText.setError("Invalid Email Id");
                        } else if (TextUtils.isEmpty(email)) {
                            emailText.requestFocus();
                            emailText.setError("This Field Is Mandatory");
                        } else if (TextUtils.isEmpty(mobile)) {
                            moboileText.requestFocus();
                            moboileText.setError("This Field Is Mandatory");
                        } else if (TextUtils.isEmpty(imagePic)) {

                            Toast.makeText(SecondStepRegister.this, "Please select your image", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(imageCard)) {

                            Toast.makeText(SecondStepRegister.this, "Please Select your card image", Toast.LENGTH_SHORT).show();

                        } else {

                            Intent i = new Intent(getApplicationContext(), FinalStepRegister.class);
                            i.putExtra("mobile", mobile);
                            i.putExtra("email", email);
                            i.putExtra("name", name);
                            i.putExtra("profile_pic", imagePic);
                            i.putExtra("card_pic", imageCard);
                            i.putExtra("visitor_type", visitor_type);
                            //for auto fill
                            i.putExtra("gender", gender);
                            i.putExtra("address", address);
                            i.putExtra("city", city);
                            i.putExtra("state", state);
                            i.putExtra("country", country);
                            i.putExtra("visitor_purpose", visitor_purpose);
                            i.putExtra("isNew", isNew);
                            startActivity(i);
                        }
                    }
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SecondStepRegister.this);
                View mview = getLayoutInflater().inflate(R.layout.chooseimage, null);
                Button mtakephoto = (Button) mview.findViewById(R.id.imagebycamera);
                mtakephoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUriProfilePic = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriProfilePic);
                        startActivityForResult(intent, CAMERA_REQUEST);


                    }
                });

                Button mtakegallery = (Button) mview.findViewById(R.id.imagebygallery);
                mtakegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showFileChooser();

                    }
                });
                mbuilder.setView(mview);
                dialog = mbuilder.create();
                dialog.show();
            }

        });


        card_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SecondStepRegister.this);
                View mview = getLayoutInflater().inflate(R.layout.chooseimage, null);
                Button mtakephoto = (Button) mview.findViewById(R.id.imagebycamera);
                mtakephoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUriCardPic = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriCardPic);
                        startActivityForResult(intent, CAMERA_REQUEST_NEW);


                    }
                });

                Button mtakegallery = (Button) mview.findViewById(R.id.imagebygallery);
                mtakegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showFileChooser2();

                    }
                });
                mbuilder.setView(mview);
                dialog = mbuilder.create();
                dialog.show();
            }

        });





    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void showFileChooser()
    {
        try {

            if (android.os.Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);


            } else {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST);

            }
        } catch (Exception e) {

            //   Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void showFileChooser2()
    {
        try {

            if (android.os.Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST_NEW);


            } else {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST_NEW);

            }
        } catch (Exception e) {

            //   Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            Uri filePath = data.getData();
            try {
                actualImagePic = FileUtil.from(this, data.getData());
                bitmapProfilePic = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                profile_pic.setImageBitmap(bitmapProfilePic);
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST)
        {
            try {

                bitmapProfilePic = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUriProfilePic);
                actualImagePic = FileUtil.from(this, imageUriProfilePic);
                profile_pic.setImageBitmap(bitmapProfilePic);

                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST_NEW && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            Uri filePath = data.getData();
            try {
                actualImageCard = FileUtil.from(this, data.getData());
                bitmapCardPic = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                card_pic.setImageBitmap(bitmapCardPic);
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST_NEW)
        {
            try {
                bitmapCardPic = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUriCardPic);
                actualImageCard = FileUtil.from(this, imageUriCardPic);
                card_pic.setImageBitmap(bitmapCardPic);
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
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




    private void checkVisitorAvailability(String checkData, String params) {
        //getting the progressbar

        String check="";

        final String access_token = SaveAccessToken.getInstance(SecondStepRegister.this).getUserId();
        final ProgressDialog progressDialog = new ProgressDialog(SecondStepRegister.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        if (params.equals("mobile")) {
            check = "mobile=" + checkData;
        }else if (params.equals("email")){
            check = "email=" + checkData;
        }
        Log.d("url", Urls.CHECK_VISITOR_DATA_EMAIL_MOBILE+check);
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.CHECK_VISITOR_DATA_EMAIL_MOBILE+check,
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
                                old_id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String mobile = jsonObject.getString("mobile");
                                gender = jsonObject.getString("gender");
                                country = jsonObject.getString("country");
                                state = jsonObject.getString("state");
                                city = jsonObject.getString("city");
                                address = jsonObject.getString("address");
                                String zip_code = jsonObject.getString("zip_code");
                                String src_img = jsonObject.getString("src_img");
                                String src_id = jsonObject.getString("src_id");
                                String opr_id = jsonObject.getString("opr_id");


                                moboileText.setText(mobile);
                                emailText.setText(email);
                                text=false;
                                nameText.setText(name);
                                isNew=false;
                                Glide.with(getApplicationContext()).load(src_img).into(profile_pic);
                                Glide.with(getApplicationContext()).load(src_id).into(card_pic);

                            }else {
                                //String msg = obj.getString("msg");
                               // Log.d("tttttttttt", msg);
                                text=false;
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }
}
