package main.com.dvb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.security.auth.login.LoginException;

import main.com.dvb.fragments.ReportFragment;
import main.com.dvb.pojos.User;
import main.com.dvb.pojos.UserRegistration;
import main.com.dvb.services.WebServices;

public class UserRegistrationActivity extends AppCompatActivity implements TextWatcher {
    public static int height, width;
    private AlertDialog alertDialogok;
    private ProgressDialog progressDialog;
    private Button submit,datepic,ok_date;
    private Spinner regplace,regGender;
    private EditText regFirstName, regLastName,regmobileNum,regEmail;
    private TextInputLayout regFirstNameTTL,regLastNameTIL, regmobileTIL,regDateofBirthTTL,regEmailTTL,regGenderTTL,regPlaceTTL;
    private WebServices webServices;
    private SharedPreferences sharedPreferences;
    private UserRegistration userRegistration;
    private String regexStr = "^[789]\\d{9}$",name,emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private RelativeLayout relativeLayout;
    private LinearLayout datelinearLayout;
    private TextView resendOtp,regDateOfBirth;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    InputMethodManager inputManager;
    String fcm_regid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration_activity);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        submit = (Button) findViewById(R.id.submit);
        datepic = (Button) findViewById(R.id.regdatepic);
        regFirstName = (EditText)findViewById(R.id.regFirstName);
        regmobileNum = (EditText)findViewById(R.id.regmobileNum);
        regplace = (Spinner)findViewById(R.id.reglocalitySpinner);
        regGender = (Spinner) findViewById(R.id.reggenderSpinner);
        regLastName = (EditText) findViewById(R.id.regLastName);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regDateOfBirth = (TextView) findViewById(R.id.regDateOfBirth);
        regFirstNameTTL = (TextInputLayout) findViewById(R.id.regFirstNameTIL);
        regLastNameTIL = (TextInputLayout)findViewById(R.id.regLastNnameTIL);
        regmobileTIL = (TextInputLayout)findViewById(R.id.regmobileNumTIL);
        regEmailTTL = (TextInputLayout) findViewById(R.id.regLastNnameTIL);
        regDateofBirthTTL = (TextInputLayout) findViewById(R.id.regDateOfBirthTTL);
        regPlaceTTL = (TextInputLayout) findViewById(R.id.regPlaceTTL);
        regGenderTTL = (TextInputLayout) findViewById(R.id.reggenderTTL);
        datelinearLayout = (LinearLayout) findViewById(R.id.datepiclinearlayout);
        final LinearLayout formlinear = (LinearLayout) findViewById(R.id.form_linearlayout);
        ok_date = (Button) findViewById(R.id.ok_datepicker);
       final DatePicker picker = (DatePicker) findViewById(R.id.regdatepicker);

        regFirstName.addTextChangedListener(this);
        regLastName.addTextChangedListener(this);
        regmobileNum.addTextChangedListener(this);
        regEmail.addTextChangedListener(this);
        getSupportActionBar().setTitle("User Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        regDateOfBirth.setEnabled(false);
        progressDialog = new ProgressDialog(this);
        webServices = new WebServices();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        regGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regGenderTTL.setErrorEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        regplace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    regPlaceTTL.setErrorEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        datepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regDateofBirthTTL.setErrorEnabled(false);
                regDateOfBirth.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.textfield_active));
                regDateofBirthTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
                datelinearLayout.setVisibility(View.VISIBLE);
                formlinear.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
            }
        });
        ok_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regplace.requestFocus();
                datelinearLayout.setVisibility(View.GONE);
                formlinear.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                int day = picker.getDayOfMonth();
                int month = picker.getMonth() + 1;
                int year = picker.getYear();
                if (day <=9 && month <=9){
                    regDateOfBirth.setText(new StringBuilder()
                            .append(year).append("-0").append(month).append("-0").append(day).append(" "));
                }else if(day <= 9 ){
                    regDateOfBirth.setText(new StringBuilder()
                            .append(year).append("-").append(month).append("-0").append(day).append(" "));
                }else if(month <= 9 ){
                    regDateOfBirth.setText(new StringBuilder()
                            .append(year).append("-0").append(month).append("-").append(day).append(" "));
                }else {
                    regDateOfBirth.setText(new StringBuilder()
                            .append(year).append("-").append(month).append("-").append(day).append(" "));
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = regFirstName.getText().toString();
                String mobileNumber = regmobileNum.getText().toString();
                String lastname = regLastName.getText().toString();
                String fullname=firstname+" "+lastname;
                String email = regEmail.getText().toString();
                String place = regplace.getSelectedItem().toString();
                String gender = regGender.getSelectedItem().toString();
                String dateOfBorth = regDateOfBirth.getText().toString();
Log.e("registration",""+place);
                if(firstname.equalsIgnoreCase("") || mobileNumber.equalsIgnoreCase("")
                        || mobileNumber.length()<10 || !mobileNumber.matches(regexStr)
                        || lastname.equalsIgnoreCase("")
                        || regplace.getSelectedItemPosition()== 0
                        || dateOfBorth.equalsIgnoreCase("")|| regGender.getSelectedItemPosition()==0){

                    if (firstname.isEmpty()){
                        regFirstNameTTL.setErrorEnabled(true);
                        regFirstNameTTL.setError("You missed the First Name field");
                    }
                    if (lastname.isEmpty()){
                        regLastName.requestFocus();
                        regLastNameTIL.setErrorEnabled(true);
                        regLastNameTIL.setError("You missed the Second Name field");
                    }
                    if(mobileNumber.isEmpty()){
                        regmobileNum.requestFocus();
                        regmobileTIL.setErrorEnabled(true);
                        regmobileTIL.setError("You missed the mobile number field");

                    }else if(mobileNumber.length()<10 || !mobileNumber.matches(regexStr)){
                        regmobileNum.requestFocus();
                        regmobileTIL.setErrorEnabled(true);
                        regmobileTIL.setError("Please enter a valid mobile number");
                    }

                    if(regplace.getSelectedItemPosition()== 0 ){
                        regPlaceTTL.setErrorEnabled(true);
                        regPlaceTTL.setError("You missed the Locality field");
                    }
                    if(dateOfBorth.isEmpty()){
                        regDateofBirthTTL.setErrorEnabled(true);
                        regDateofBirthTTL.setError("You missed the Date Of Birth field");
                    }
                    if(regGender.getSelectedItemPosition()== 0){
                        regGenderTTL.setErrorEnabled(true);
                        regGenderTTL.setError("You missed the Gender field");
                    }
                    // Snackbar.make(view,"Please fill all the fields", Snackbar.LENGTH_LONG).show();
                    return;
                }
                sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
                fcm_regid = sharedPreferences.getString("regId", "");
                if (fcm_regid.equalsIgnoreCase("")){
                    fcm_regid = FirebaseInstanceId.getInstance().getToken();
                }
                 userRegistration= new main.com.dvb.pojos.UserRegistration();
                userRegistration.fullname = fullname;
                userRegistration.mobile = mobileNumber;
                userRegistration.emailid = email;
                userRegistration.gender = gender;
                userRegistration.place = place;
                userRegistration.dateofBorth = dateOfBorth;

                String formOtpData ="";
                try {

                    formOtpData = registrationFormData(userRegistration);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (email.length()>0 && !email.matches(emailPattern)) {
                    Toast.makeText(UserRegistrationActivity.this, "Please enter valid Email Address", Toast.LENGTH_SHORT).show();
                }else {
                    if (checkIsNetworkAvailable()){
                       new SubmitRegistrationFormData().execute(formOtpData);
                    }else{
                        errorMessage();
                    }
                }
            }
        });

    }

    private String registrationFormData(main.com.dvb.pojos.UserRegistration user) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(user.getFullname(), "UTF-8");
        data += "&" + URLEncoder.encode("emailid", "UTF-8")
                + "=" + URLEncoder.encode(user.getEmailid(), "UTF-8");
        data += "&" + URLEncoder.encode("fcmid", "UTF-8")
                + "=" + URLEncoder.encode(fcm_regid, "UTF-8");
        data += "&" + URLEncoder.encode("mobilenumber", "UTF-8")
                + "=" + URLEncoder.encode(user.getMobile(), "UTF-8");
        data += "&" + URLEncoder.encode("place", "UTF-8")
                + "=" + URLEncoder.encode(user.getPlace(), "UTF-8");
        data += "&" + URLEncoder.encode("dateofbirth", "UTF-8")
                + "=" + URLEncoder.encode(user.getDateofBorth(), "UTF-8");
        data += "&" + URLEncoder.encode("gender", "UTF-8")
                + "=" + URLEncoder.encode(user.getGender(), "UTF-8");

        return  data;
    }
    private void clearAllFields() {

        regFirstName.setText("");
        regLastName.setText("");
        regEmail.setText("");
        regmobileNum.setText("");
        regDateOfBirth.setText("");
        regGender.setSelection(0);
        regplace.setSelection(0);




    }

    public class SubmitRegistrationFormData extends AsyncTask<String, Void, String> {

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
                    clearAllFields();
                    showSuccessAlert();
                }else{
                    Toast.makeText(getApplicationContext(), "This number is already registered, please try again", Toast.LENGTH_LONG).show();

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
        if(count == 0){
            if(regFirstName.isFocused()){
                regFirstNameTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(regLastName.isFocused()){
                regLastNameTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(regEmail.isFocused()){
                regEmailTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(regGenderTTL.isFocused()){
                regGenderTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(regmobileNum.isFocused()){
                regmobileTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(regplace.isFocused()){
                regPlaceTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }
            else if(regDateOfBirth.isFocused()){
                regDateofBirthTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }
        }else{
            if(regFirstName.isFocused()){
                regFirstNameTTL.setErrorEnabled(false);
                regFirstName.setBackground(ContextCompat.getDrawable(this, R.drawable.textfield_active));
                regFirstNameTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(regLastName.isFocused()){
                regLastNameTIL.setErrorEnabled(false);
                regLastName.setBackground(ContextCompat.getDrawable(this,R.drawable.textfield_active));
                regLastNameTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(regEmail.isFocused()){
                regEmailTTL.setErrorEnabled(false);
                regEmail.setBackground(ContextCompat.getDrawable(this,R.drawable.textfield_active));
                regEmailTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(regmobileNum.isFocused()) {
                regmobileTIL.setErrorEnabled(false);
                regmobileNum.setBackground(ContextCompat.getDrawable(this, R.drawable.textfield_active));
                regmobileTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(regDateOfBirth.isFocused()){
                regDateofBirthTTL.setErrorEnabled(false);
                regDateOfBirth.setBackground(ContextCompat.getDrawable(this,R.drawable.textfield_active));
                regDateofBirthTTL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void showSuccessAlert() {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        View okDialog = li.inflate(R.layout.registration_success_popup, null);
        LinearLayout dialogLayout = (LinearLayout) okDialog.findViewById(R.id.dialogLayout);
       int dWidth = UserRegistrationActivity.width - UserRegistrationActivity.width/4;
        dialogLayout.getLayoutParams().width = dWidth;
        dialogLayout.getLayoutParams().height = dWidth;
//        okDialog.getLayoutParams().width = dWidth;
//        okDialog.getLayoutParams().height = dWidth;

        TextView text = (TextView) okDialog.findViewById(R.id.submit_text);
        text.setText("Thank you "+userRegistration.getFullname()+".  Your are registered successfully");
        AlertDialog.Builder alertok = new AlertDialog.Builder(this);

        alertok.setView(okDialog);
        alertDialogok = alertok.create();
//        alertDialogok.getWindow().setLayout(dWidth, dWidth);
        alertDialogok.show();

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

public void setFocus(){
    regFirstName.setFocusable(false);
    regLastName.setFocusable(false);
    regEmail.setFocusable(false);
    regmobileNum.setFocusable(false);
    regDateOfBirth.setFocusable(false);
}

}
