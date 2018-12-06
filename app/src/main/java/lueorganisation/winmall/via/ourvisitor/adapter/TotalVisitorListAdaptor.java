package lueorganisation.winmall.via.ourvisitor.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import lueorganisation.winmall.via.ourvisitor.R;
import lueorganisation.winmall.via.ourvisitor.model.VisitorListPojo;

public class TotalVisitorListAdaptor extends BaseAdapter {
    LayoutInflater inflater;
    private Activity context;
    LayoutInflater mInflater;
//    @BindView(R.id.user_name)
    TextView tvName;
 //   @BindView(R.id.invoice_no)
    TextView tvAddress;
 //   @BindView(R.id.product_name)
    TextView tvType;
 //   @BindView(R.id.redeem_date)
    TextView tvTime;
 //   @BindView(R.id.image)
    CircleImageView profilePic;
    ArrayList<VisitorListPojo> visitorListPojos=new ArrayList<>();
    public TotalVisitorListAdaptor(FragmentActivity activity, ArrayList<VisitorListPojo> storeContacts){
        this.context=activity;
        this.visitorListPojos=storeContacts;
    }
    @Override
    public int getCount() {
        return visitorListPojos.size();
    }

    @Override
    public Object getItem(int i) {
        return visitorListPojos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //   View v=inflater.inflate(R.layout.transactionsinglerow,null);
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.row_item, null);
        tvName = (TextView)view.findViewById(R.id.name);
        tvType = (TextView)view.findViewById(R.id.type);
        tvAddress = (TextView)view.findViewById(R.id.address);
        tvTime = (TextView)view.findViewById(R.id.time);
        profilePic= (CircleImageView)view.findViewById(R.id.profilePic);
        tvName.setText(visitorListPojos.get(i).getName());
        tvType.setText(visitorListPojos.get(i).getVisit_type());
        tvTime.setText(visitorListPojos.get(i).getTime());
        tvAddress.setText(visitorListPojos.get(i).getCity()+", "+visitorListPojos.get(i).getState()+", "+visitorListPojos.get(i).getCountry());
        Glide.with(context).load(visitorListPojos.get(i).getImage()).into(profilePic);
        return view;
    }
}
