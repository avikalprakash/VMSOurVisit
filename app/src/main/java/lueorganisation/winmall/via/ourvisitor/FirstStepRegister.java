package lueorganisation.winmall.via.ourvisitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FirstStepRegister extends AppCompatActivity implements View.OnClickListener {
     ImageView close;
     Button visitor, contractor, delivery, staff;
     String visitor_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step_register);
        close = findViewById(R.id.close);
        visitor = findViewById(R.id.visitor);
        contractor = findViewById(R.id.contractor);
        delivery = findViewById(R.id.delivery);
        staff = findViewById(R.id.staff);
        close.setOnClickListener(this);
        visitor.setOnClickListener(this);
        contractor.setOnClickListener(this);
        delivery.setOnClickListener(this);
        staff.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:
                finish();

                break;

            case R.id.visitor:
                //set background color
                visitor_type = "Visitor";
                contractor.setBackgroundResource(R.drawable.btn_bg_white);
                delivery.setBackgroundResource(R.drawable.btn_bg_white);
                staff.setBackgroundResource(R.drawable.btn_bg_white);
                visitor.setBackgroundResource(R.drawable.btn_bg_yellow);

                //set text color
                visitor.setTextColor(getResources().getColor(R.color.white));
                contractor.setTextColor(getResources().getColor(R.color.black));
                delivery.setTextColor(getResources().getColor(R.color.black));
                staff.setTextColor(getResources().getColor(R.color.black));

                //go to next step

                Intent i1 = new Intent(getApplicationContext(), SecondStepRegister.class);
                i1.putExtra("visitor_type", visitor_type);
                startActivity(i1);

                break;

            case R.id.contractor:
                //set background color
                visitor_type = "Constractor";
                contractor.setBackgroundResource(R.drawable.btn_bg_yellow);
                delivery.setBackgroundResource(R.drawable.btn_bg_white);
                staff.setBackgroundResource(R.drawable.btn_bg_white);
                visitor.setBackgroundResource(R.drawable.btn_bg_white);

                //set text color
                contractor.setTextColor(getResources().getColor(R.color.white));
                delivery.setTextColor(getResources().getColor(R.color.black));
                staff.setTextColor(getResources().getColor(R.color.black));
                visitor.setTextColor(getResources().getColor(R.color.black));

                //go to next step

                Intent i2 = new Intent(getApplicationContext(), SecondStepRegister.class);
                i2.putExtra("visitor_type", visitor_type);
                startActivity(i2);
                break;

            case R.id.delivery:
                //set background color
                visitor_type = "Delivery";
                contractor.setBackgroundResource(R.drawable.btn_bg_white);
                delivery.setBackgroundResource(R.drawable.btn_bg_yellow);
                staff.setBackgroundResource(R.drawable.btn_bg_white);
                visitor.setBackgroundResource(R.drawable.btn_bg_white);

                //set text color
                delivery.setTextColor(getResources().getColor(R.color.white));
                contractor.setTextColor(getResources().getColor(R.color.black));
                staff.setTextColor(getResources().getColor(R.color.black));
                visitor.setTextColor(getResources().getColor(R.color.black));

                //go to next step

                Intent i3 = new Intent(getApplicationContext(), SecondStepRegister.class);
                i3.putExtra("visitor_type", visitor_type);
                startActivity(i3);

                break;

            case R.id.staff:
                //set background color
                visitor_type = "Staff";
                contractor.setBackgroundResource(R.drawable.btn_bg_white);
                delivery.setBackgroundResource(R.drawable.btn_bg_white);
                staff.setBackgroundResource(R.drawable.btn_bg_yellow);
                visitor.setBackgroundResource(R.drawable.btn_bg_white);


                //set text color
                staff.setTextColor(getResources().getColor(R.color.white));
                delivery.setTextColor(getResources().getColor(R.color.black));
                contractor.setTextColor(getResources().getColor(R.color.black));
                visitor.setTextColor(getResources().getColor(R.color.black));

                //go to next step

                Intent i4 = new Intent(getApplicationContext(), SecondStepRegister.class);
                i4.putExtra("visitor_type", visitor_type);
                startActivity(i4);
                break;
        }
    }
}
