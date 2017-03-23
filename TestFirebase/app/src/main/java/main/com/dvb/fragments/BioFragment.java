package main.com.dvb.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;

/**
 * Created by SREEVATSAVA on 30-07-2016.
 */
public class BioFragment extends Fragment{
    SharedPreferences sharedPreferences;
    private WebView text1,text2,text3;
    private TextView telugu_bio1,telugu_bio2,telugu_bio3,name,address;
    ArrayList<String> resultList = new ArrayList<>();

    public static BioFragment bioFragment;
    public BioFragment() {
        // Required empty public constructor
        bioFragment = BioFragment.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bio_fragment, container, false);
        text1 = (WebView) view.findViewById(R.id.biographytext1);
        text2 = (WebView) view.findViewById(R.id.biographytext2);
        text3 = (WebView) view.findViewById(R.id.biographytext3);
        name= (TextView) view.findViewById(R.id.name_t);
        address= (TextView) view.findViewById(R.id.name);
        telugu_bio1 = (TextView) view.findViewById(R.id.telugu_textview1);
        telugu_bio2 = (TextView) view.findViewById(R.id.telugu_textview2);
        telugu_bio3 = (TextView) view.findViewById(R.id.telugu_textview3);
        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
       String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            name.setText(getString(R.string.fullname));
            address.setText("(MLA, Warangal West)");
            String bio1=getResources().getString(R.string.biography1);
            String bio2=getResources().getString(R.string.biography2);
            String bio3=getResources().getString(R.string.biography3);
            text1.loadData("<p style=\"text-align: justify\">"+ bio1 +"</p>", "text/html", "UTF-8");
            text2.loadData("<p style=\"text-align: justify\">"+ bio2 +"</p>", "text/html", "UTF-8");
            text3.loadData("<p style=\"text-align: justify\">"+ bio3 +"</p>", "text/html", "UTF-8");
            prepareList();
        }else {
            name.setText(getString(R.string.telugu_fullname));
            address.setText("శాసన సభ్యులు , వరంగల్ పశ్చిమ");
            telugu_bio1.setVisibility(View.VISIBLE);
            telugu_bio2.setVisibility(View.VISIBLE);
            telugu_bio3.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void prepareList(){
        for(int i =0;i<8;i++){
            resultList.add("Weekly "+i);
        }
    }

}
