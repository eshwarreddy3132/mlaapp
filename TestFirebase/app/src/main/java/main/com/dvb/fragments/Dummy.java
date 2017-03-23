package main.com.dvb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import main.com.dvb.R;

/**
 * Created by AIA on 11/11/16.
 */

public class Dummy extends Fragment{

    public static Dummy facebookFragment;
    public Dummy() {
        // Required empty public constructor
        facebookFragment = Dummy.this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dummy, container, false);

        return view;
    }

}
