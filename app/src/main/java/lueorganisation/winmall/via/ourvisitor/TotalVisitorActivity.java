package lueorganisation.winmall.via.ourvisitor;

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

import java.util.ArrayList;

import lueorganisation.winmall.via.ourvisitor.local.CustomAdapter;
import lueorganisation.winmall.via.ourvisitor.local.DataModel;

public class TotalVisitorActivity extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    ListView visitorList;
    ImageView close;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_visitor);
        visitorList = findViewById(R.id.visitorList);
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close current screen
                finish();
            }
        });
        dataModels= new ArrayList<>();
        dataModels.add(new DataModel("Alex", "type", "Delhi, India","11:02 AM"));
        dataModels.add(new DataModel("Robort", "type", "Kolkata, West Bengal, India","10:22 AM"));
        dataModels.add(new DataModel("Tran", "type", "Pune, Maharastra, India","9:42 AM"));
        dataModels.add(new DataModel("Alex", "type", "Delhi, India","11:02 AM"));
        dataModels.add(new DataModel("Robort", "type", "Kolkata, West Bengal India","10:22 AM"));
        dataModels.add(new DataModel("Tran", "type", "Pune, Maharastra, India","9:42 AM"));
        dataModels.add(new DataModel("Alex", "type", "Delhi, India","11:02 AM"));
        dataModels.add(new DataModel("Robort", "type", "Kolkata, West Bengal India","10:22 AM"));
        dataModels.add(new DataModel("Tran", "type", "Pune, Maharastra, India","9:42 AM"));
        dataModels.add(new DataModel("Lee", "type", "New York, America","12:14 AM"));
        adapter= new CustomAdapter(dataModels,getApplicationContext());

        visitorList.setAdapter(adapter);

        visitorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              String name = dataModels.get(i).getName();
                AlertDialog();
            }
        });
    }

    public void AlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(TotalVisitorActivity.this);
        View subView = inflater.inflate(R.layout.visitors_detail_alert, null);
        final ImageView close = (ImageView) subView.findViewById(R.id.close);
        final TextView emailText = (TextView)subView.findViewById(R.id.emailText);
        Drawable dr = getResources().getDrawable(R.drawable.email);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));
        emailText.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null);

        final TextView mobileText = (TextView)subView.findViewById(R.id.mobileText);
        Drawable dr2 = getResources().getDrawable(R.drawable.mobile);
        Bitmap bitmap2 = ((BitmapDrawable) dr2).getBitmap();
        Drawable d2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 28, 40, true));
        mobileText.setCompoundDrawablesWithIntrinsicBounds(d2,null,null,null);

        final TextView locationText = (TextView)subView.findViewById(R.id.locationText);
        Drawable dr3 = getResources().getDrawable(R.drawable.location);
        Bitmap bitmap3 = ((BitmapDrawable) dr3).getBitmap();
        Drawable d3 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 30, 30, true));
        locationText.setCompoundDrawablesWithIntrinsicBounds(d3,null,null,null);

        final TextView typeText = (TextView)subView.findViewById(R.id.typeText);
        Drawable dr4 = getResources().getDrawable(R.drawable.visitor_purpose);
        Bitmap bitmap4 = ((BitmapDrawable) dr4).getBitmap();
        Drawable d4 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap4, 30, 30, true));
        typeText.setCompoundDrawablesWithIntrinsicBounds(d4,null,null,null);

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
}
