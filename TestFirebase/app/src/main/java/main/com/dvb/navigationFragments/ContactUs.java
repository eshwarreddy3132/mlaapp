package main.com.dvb.navigationFragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;

/**
 * Created by AIA on 11/6/16.
 */

public class ContactUs extends Fragment {
private TextView contact_addresstext,name,contact_address,phone,phone_number,email,emailid;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_us, container, false);
            contact_addresstext = (TextView) view.findViewById(R.id.contact_address_text);
        name = (TextView) view.findViewById(R.id.name);
        contact_address = (TextView) view.findViewById(R.id.contact_address);
        phone = (TextView) view.findViewById(R.id.phone);
        phone_number = (TextView) view.findViewById(R.id.contact_number_text);
        email = (TextView) view.findViewById(R.id.email);
        emailid = (TextView) view.findViewById(R.id.email);

        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            contact_addresstext.setText(R.string.contact_address_headding);
            contact_address.setText(getString(R.string.address));
            name.setText(getString(R.string.fullname));
            contact_address.setText(getString(R.string.address));
            phone.setText(getString(R.string.contact_phone));
            email.setText(getString(R.string.contact_email));

        }else {
            contact_addresstext.setText(R.string.telugu_contactheadding);
            contact_address.setText(getString(R.string.telugu_address));
            name.setText(getString(R.string.telugu_fullname));
            contact_address.setText(getString(R.string.telugu_address));
            phone.setText(getString(R.string.contact_telugu_phone));
            email.setText(getString(R.string.contact_telugu_email));
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(Dashboard_main.context, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},123);
//            requestPermissions(
//                    (Dashboard_main)Dashboard_main.context,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    123);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone)));
            Dashboard_main.context.startActivity(intent);
//            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:12345678901")));
        }
    }

}
