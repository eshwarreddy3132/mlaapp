package main.com.dvb.navigationFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.JourneyListAdapter;
import main.com.dvb.pojos.JourneyBean;

/**
 * Created by AIA on 11/6/16.
 */

public class HisJourney extends Fragment {
    SharedPreferences sharedPreferences;
    ArrayList<JourneyBean> journeyBeenList;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.journey_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.journeyList);

        journeyBeenList = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            formEnglishData();
        }else {
            formTeluguData();
        }


        JourneyListAdapter journeyListAdapter = new JourneyListAdapter(journeyBeenList,Dashboard_main.context);
        listView.setAdapter(journeyListAdapter);
        return view;
    }

    private void formTeluguData() {
        JourneyBean journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1986);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_1986_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1987);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_1987_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1990);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_1990_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1997);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_1997_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1998);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_1998_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1999);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_1999_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2000);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2000_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2004);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2004_value);
        journeyBeenList.add(journeyBean);


        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2005);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2005_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2006);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2006_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2007);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2007_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2009);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2009_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2010);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2010_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2014);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2014_value);
        journeyBeenList.add(journeyBean);


        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2015);
        journeyBean.journeyValue = getResources().getString(R.string.tjourney_2015_value);
        journeyBeenList.add(journeyBean);
    }

    private void formEnglishData() {
        JourneyBean journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1986);
        journeyBean.journeyValue = getResources().getString(R.string.journey_1986_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1987);
        journeyBean.journeyValue = getResources().getString(R.string.journey_1987_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1990);
        journeyBean.journeyValue = getResources().getString(R.string.journey_1990_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1997);
        journeyBean.journeyValue = getResources().getString(R.string.journey_1997_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1998);
        journeyBean.journeyValue = getResources().getString(R.string.journey_1998_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_1999);
        journeyBean.journeyValue = getResources().getString(R.string.journey_1999_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2000);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2000_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2004);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2004_value);
        journeyBeenList.add(journeyBean);


        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2005);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2005_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2006);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2006_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2007);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2007_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2009);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2009_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2010);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2010_value);
        journeyBeenList.add(journeyBean);

        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2014);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2014_value);
        journeyBeenList.add(journeyBean);


        journeyBean = new JourneyBean();
        journeyBean.journeyDate=getResources().getString(R.string.journey_2015);
        journeyBean.journeyValue = getResources().getString(R.string.journey_2015_value);
        journeyBeenList.add(journeyBean);

    }

}
