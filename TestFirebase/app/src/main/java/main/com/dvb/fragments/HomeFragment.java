package main.com.dvb.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

import main.com.dvb.Dashboard_main;
import main.com.dvb.banner_fragments.HeaderPage1Fragment;
import main.com.dvb.banner_fragments.HeaderPage2Fragment;
import main.com.dvb.banner_fragments.HeaderPage3Fragment;
import main.com.dvb.R;
import main.com.dvb.adapters.ViewPagerAdapter;
import main.com.dvb.homePageFragments.FacebookFragment;
import main.com.dvb.homePageFragments.InstagramFragment;
import main.com.dvb.homePageFragments.NewsFragment;
import main.com.dvb.homePageFragments.TwitterFragment;
import main.com.dvb.homePageFragments.YoutubeFragment;

//import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by SREEVATSAVA on 30-07-2016.
 */
public class HomeFragment extends Fragment {

    ArrayList<String> resultList = new ArrayList<>();
    public static HomeFragment simpleFragment;
//    ViewPager viewPagerBanner;
    ViewPagerAdapter adapter;

    ViewPager viewPager;
    TabLayout tabLayout;




    public HomeFragment() {
        simpleFragment = HomeFragment.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Log.e("","");


//        FacebookSdk.sdkInitialize(((Dashboard_main)Dashboard_main.context).getApplicationContext());
//        AppEventsLogger.activateApp(((Dashboard_main)Dashboard_main.context).getApplication());



//        viewPagerBanner = (ViewPager) view.findViewById(R.id.viewpagerBanner);
//        setupViewPagerBanner(viewPagerBanner);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager();

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);


        if (ViewCompat.isLaidOut(tabLayout)) {
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        switch (i){
                            case 0:
                                tabLayout.getTabAt(i).setIcon(R.mipmap.facebook_normal);
                                break;
                            case 1:
                                tabLayout.getTabAt(i).setIcon(R.mipmap.facebook_normal);
                                break;
                            case 2:
                                tabLayout.getTabAt(i).setIcon(R.mipmap.twitter_normal);
                                break;
                            case 3:
                                tabLayout.getTabAt(i).setIcon(R.mipmap.facebook_normal);
                                break;
                        }

                    }
                    tabLayout.removeOnLayoutChangeListener(this);
                }
            });
        }

//        viewPagerBanner.getLayoutParams().height = (int) (Dashboard_main.width/2.5f);
//        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                selectedPage(position);
            }

            @Override
            public void onPageSelected(int position) {
                selectedPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        prepareList();
        return view;
    }

    private void selectedPage(int position) {
        switch (position){
            case 0:
                tabLayout.getTabAt(0).setIcon(R.mipmap.news_active);
                tabLayout.getTabAt(1).setIcon(R.mipmap.facebook_normal);
                tabLayout.getTabAt(2).setIcon(R.mipmap.twitter_normal);
//                tabLayout.getTabAt(3).setIcon(R.mipmap.facebook_normal);
                break;
            case 1:
                tabLayout.getTabAt(0).setIcon(R.mipmap.news_normal);
                tabLayout.getTabAt(1).setIcon(R.mipmap.facebook_active);
                tabLayout.getTabAt(2).setIcon(R.mipmap.twitter_normal);
//                tabLayout.getTabAt(3).setIcon(R.mipmap.facebook_normal);
                break;
            case 2:
                tabLayout.getTabAt(0).setIcon(R.mipmap.news_normal);
                tabLayout.getTabAt(1).setIcon(R.mipmap.facebook_normal);
                tabLayout.getTabAt(2).setIcon(R.mipmap.twitter_active);
//                tabLayout.getTabAt(3).setIcon(R.mipmap.facebook_normal);
                break;
            case 3:
                tabLayout.getTabAt(0).setIcon(R.mipmap.news_normal);
                tabLayout.getTabAt(1).setIcon(R.mipmap.facebook_normal);
                tabLayout.getTabAt(2).setIcon(R.mipmap.twitter_normal);
//                tabLayout.getTabAt(3).setIcon(R.mipmap.facebook_active);
                break;
        }

    }


    private void setupViewPager() {
        if(Dashboard_main.context != null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(((Dashboard_main) Dashboard_main.context).getSupportFragmentManager());
            adapter.addFrag(new NewsFragment(), "News");
            adapter.addFrag(new FacebookFragment(), "Facebook");
            adapter.addFrag(new TwitterFragment(), "Twitter");
//            adapter.addFrag(new YoutubeFragment(), "Youtube");
//            adapter.addFrag(new InstagramFragment(), "Instagram");

            viewPager.setAdapter(adapter);
        }
    }

    public void prepareList(){
        for(int i =0;i<10;i++){
            resultList.add("Simple "+i);
        }
    }

    private void setupViewPagerBanner(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(((Dashboard_main)Dashboard_main.context).getSupportFragmentManager());
        adapter.addFrag(new HeaderPage1Fragment(), "Page1");
        adapter.addFrag(new HeaderPage2Fragment(), "Page2");
        adapter.addFrag(new HeaderPage3Fragment(), "Page3");

//        viewPagerBanner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refreshBanner(){
//        adapter.notifyDataSetChanged();
//        viewPagerBanner.setAdapter(adapter);
    }


}
