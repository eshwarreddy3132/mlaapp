package main.com.dvb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import main.com.dvb.services.WebServices;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UserLogin extends AppCompatActivity implements TextWatcher {

    private AlertDialog alertDialogok;
    private ProgressDialog progressDialog;
    private Button usersignin,usersignup,submit;
    private EditText loginmobile,loginotp;
    private TextInputLayout loginmobileTTL,loginotpTTL;
    private WebServices webServices;
    private TextView resendotptextview,notregistertext;
    private LinearLayout resendlayout;
    private SharedPreferences sharedPreferences;
    String FCM_RegisterID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        loginmobile = (EditText) findViewById(R.id.loginmobile);
        loginotp = (EditText) findViewById(R.id.loginotp);
        loginmobileTTL = (TextInputLayout)findViewById(R.id.loginMobileTtL);
        loginotpTTL = (TextInputLayout)findViewById(R.id.loginotpTIL);
        usersignin = (Button) findViewById(R.id.signin);
        usersignup = (Button) findViewById(R.id.signup);
        submit = (Button) findViewById(R.id.loginSubmit);
        resendlayout = (LinearLayout) findViewById(R.id.resendlayout);
        resendotptextview = (TextView) findViewById(R.id.loginResendotp);
        notregistertext= (TextView) findViewById(R.id.notregistertext);
        loginmobile.addTextChangedListener(this);
        loginotp.addTextChangedListener(this);
        getSupportActionBar().setTitle("User Login");
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,0);
         FCM_RegisterID = sharedPreferences.getString("regId","");
        if (FCM_RegisterID.equals("")){
            FCM_RegisterID = FirebaseInstanceId.getInstance().getToken();
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("regId", FCM_RegisterID);
            editor.commit();
        }
        progressDialog = new ProgressDialog(this);
        webServices = new WebServices();
        resendotptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersignin.setVisibility(View.VISIBLE);
                loginotpTTL.setVisibility(View.GONE);
                resendlayout.setVisibility(View.GONE);
            }
        });
        usersignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notregistertext.setVisibility(View.GONE);
               startActivity( new Intent(UserLogin.this,UserRegistrationActivity.class));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = loginmobile.getText().toString();
                if(mobile.equalsIgnoreCase("")){

                    if (mobile.isEmpty()){
                        loginmobile.requestFocus();
                        loginmobileTTL.setErrorEnabled(true);
                        loginmobileTTL.setError("You missed the Mobile Number field");
                    }
                    return;
                }

                String formPostData ="";
                try {
                    formPostData = otpFormData(mobile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (checkIsNetworkAvailable()){
                    new OTPTask().execute(formPostData);
                }else{
                    errorMessage();
                }


            }
        });
        usersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = loginmobile.getText().toString();
                String otp = loginotp.getText().toString();
                if(mobile.equalsIgnoreCase("") || otp.equalsIgnoreCase("")){

                    if (mobile.isEmpty()){
                        loginmobile.requestFocus();
                        loginmobileTTL.setErrorEnabled(true);
                        loginmobileTTL.setError("You missed the Mobile Number field");
                    }
                    if (otp.isEmpty()){
                        loginotp.requestFocus();
                        loginotpTTL.setErrorEnabled(true);
                        loginotpTTL.setError("You missed the OTP field");

                    }
                    return;
                }

                String formLoginData ="";
                try {
                    formLoginData = userLoginFormData(mobile,otp);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (checkIsNetworkAvailable()){

                    new UserLoginTask().execute(formLoginData);
                }else{
                    errorMessage();
                }


            }
        });

    }

    private String userLoginFormData(String mobile,String otp) throws UnsupportedEncodingException {
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF,0);
        String fcm = sharedPreferences.getString("regId","");
        String data = URLEncoder.encode("mobile", "UTF-8")
                + "=" + URLEncoder.encode(mobile, "UTF-8");
        data += "&" + URLEncoder.encode("otp", "UTF-8")
                + "=" + URLEncoder.encode(otp, "UTF-8");
        data += "&" + URLEncoder.encode("fcmid", "UTF-8")
                + "=" + URLEncoder.encode(fcm, "UTF-8");
        return  data;
    }
    private String otpFormData(String mobile) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("mobile", "UTF-8")
                + "=" + URLEncoder.encode(mobile, "UTF-8");
        return  data;
    }

    //asunctask to login
    public class UserLoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result="";

            try {
                result = webServices.postRequest(params[0], Constants.SUBMIT_REGISTRATION);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("result",""+s);
            progressDialog.dismiss();
            try {
                JSONObject responseObj = new JSONObject(s);
                boolean error = responseObj.getBoolean("error");
                boolean result = responseObj.getBoolean("result");

                if(result){
                    String session = "TRUE";
                    String mobile =loginmobile.getText().toString();
                    storeLoginSessionInPref(session,mobile);
                    loginmobile.setText("");
                    loginotp.setText("");
                    Intent intent = new Intent(UserLogin.this,Dashboard_main.class);
                    finishAffinity();
                    startActivity(intent);
                    finish();
                  Toast.makeText(UserLogin.this, "Login successfull", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Not processed, please try again", Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//Asynctask to send the otp
    public class OTPTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result="";

            try {
                result = webServices.postRequest(params[0], Constants.SUBMIT_REGISTRATION);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("result",""+s);
            progressDialog.dismiss();
            try {
                JSONObject responseObj = new JSONObject(s);
                boolean error = responseObj.getBoolean("error");
                boolean result = responseObj.getBoolean("result");

                if(result){
                    submit.setVisibility(View.GONE);
                    usersignin.setVisibility(View.VISIBLE);
                    loginotpTTL.setVisibility(View.VISIBLE);
                    resendlayout.setVisibility(View.VISIBLE);
                    Toast.makeText(UserLogin.this, "You will receive OTP shortly", Toast.LENGTH_SHORT).show();
                }else{
                    notregistertext.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "You may not registered. please register and try again", Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count == 0) {
            if (loginmobile.isFocused()) {
                loginmobileTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            } else if (loginotp.isFocused()) {
                loginotpTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }
        }else {
                if (loginmobile.isFocused()) {
                    loginmobileTTL.setErrorEnabled(false);
                    loginmobile.setBackground(ContextCompat.getDrawable(this, R.drawable.textfield_active));
                    loginmobileTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
                } else if (loginotp.isFocused()) {
                    loginotpTTL.setErrorEnabled(false);
                    loginotp.setBackground(ContextCompat.getDrawable(this, R.drawable.textfield_active));
                    loginotpTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
                }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    //Checking for network connection
    public boolean checkIsNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    public void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert.!");
        builder.setMessage("Unable to connect server. Please check your internet connection.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return;
    }

    private void storeLoginSessionInPref(String session,String mobile) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.LOGIN_SESSION, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SESSION", session);
        editor.putString("MOBILENUMBER",mobile);
        editor.commit();
    }
}
