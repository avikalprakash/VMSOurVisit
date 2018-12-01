package lueorganisation.winmall.via.ourvisitor.Fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import lueorganisation.winmall.via.ourvisitor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TermsOfUseFragment extends Fragment {
    WebView para1,para2,para3,para5,para6,para7,para8,para9,para4;
    private ProgressDialog pDialog;
    TextView tvheadertitle;
    ViewPager viewpager;
    ImageView back;
    String p1,p2,p3,p4;

    public TermsOfUseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_of_use, container, false);
        para1 = (WebView) view.findViewById(R.id.para1);
        String kkk = "We may need to update these Terms from time to time to reflect changes in law or the services that we provide. You are encouraged to regularly check these Terms for any updates. If we make any material changes that may affect you, we will try to notify you for";


        String youtContentStr = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#F2B728;" +
                     //   " background-color:#44515F;" +
                        "\">"
                        + kkk + "</body>]]>"));

        para1.loadData(youtContentStr, "text/html", "UTF-8");
        para1.setBackgroundColor(Color.TRANSPARENT);
        return view;

    }
}
