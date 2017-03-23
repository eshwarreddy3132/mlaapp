package main.com.dvb.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.CustomSpinnerAdapter;
import main.com.dvb.pojos.MyReportsBean;
import main.com.dvb.pojos.User;
import main.com.dvb.services.WebServices;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by AIA on 11/6/16.
 */

public class ReportFragment extends Fragment implements TextWatcher{

//    private DatabaseReference mDatabase;
//    private FirebaseDatabase database;
    ArrayList<String> items;
    private String mUserId;
    public static ReportFragment reportFragment;
    int RESULT_LOAD_IMAGE = 999;
    private AppCompatButton buttonConfirm, submitpopup_ok;
    AlertDialog alertDialog,alertDialogok;
    ProgressDialog loading;
    EditText userName,mobileNum,department,place,subject,problem,editTextConfirmOtp;
    ImageView addImages, attachImg;
    TextView attachedFileName,resendotp;
    LinearLayout attachLay;
    TextInputLayout userNameTIL, mobileTIL, deptTIL, placeTIL, subjectTIL, problemTIL,otpTIL;
    WebServices webServices;
    SharedPreferences sharedPreferences;
    String FCM_RegisterID="",uname;
    Spinner subjectSpinner;
    User user;
    String regexStr = "^[789]\\d{9}$";
    public ReportFragment() {
        reportFragment = ReportFragment.this;
    }
    String[] subjectNames;
    int subjectflags[] = {R.mipmap.arrow_down,
            R.mipmap.education,
            R.mipmap.health,
            R.mipmap.water,
            R.mipmap.sanitation,
            R.mipmap.roads,
            R.mipmap.electricity,
            R.mipmap.others};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.report_fragment, container, false);

        Button submit = (Button) view.findViewById(R.id.submit);
        userName = (EditText) view.findViewById(R.id.uName);
        mobileNum = (EditText) view.findViewById(R.id.mobileNum);
        department = (EditText) view.findViewById(R.id.dept);
        place = (EditText) view.findViewById(R.id.placeArea);
        subject = (EditText) view.findViewById(R.id.subj);
        problem = (EditText) view.findViewById(R.id.prob);
        addImages = (ImageView) view.findViewById(R.id.addImage);
        attachImg = (ImageView) view.findViewById(R.id.attachImg);
        attachedFileName = (TextView) view.findViewById(R.id.attachedFileName);
        userNameTIL = (TextInputLayout) view.findViewById(R.id.userNameTIL);
        mobileTIL = (TextInputLayout) view.findViewById(R.id.mobileNumTIL);
        deptTIL = (TextInputLayout) view.findViewById(R.id.deptTIL);
        placeTIL = (TextInputLayout) view.findViewById(R.id.placeAreaTIL);
        subjectTIL = (TextInputLayout) view.findViewById(R.id.subjTIL);
        problemTIL = (TextInputLayout) view.findViewById(R.id.probTIL);

        subjectSpinner = (Spinner) view.findViewById(R.id.subjectSpinner);
        sharedPreferences =getActivity().getSharedPreferences(Constants.LANGUAGE_SHARED_PREF, 0);
        String preff_lang = sharedPreferences.getString("LANGUAGE", "");
        if (preff_lang.equalsIgnoreCase("Engilsh")){
            subjectNames=getResources().getStringArray(R.array.report_submit_subject);
        }else{
            subjectNames=getResources().getStringArray(R.array.telugureport_submit_subject);
        }

        attachLay = (LinearLayout) view.findViewById(R.id.attachLay);

        userName.addTextChangedListener(this);
        mobileNum.addTextChangedListener(this);
        department.addTextChangedListener(this);
        place.addTextChangedListener(this);
        subject.addTextChangedListener(this);
        problem.addTextChangedListener(this);

        int attachLayHeight = Dashboard_main.width/5;


        attachLay.getLayoutParams().height = attachLayHeight;
        attachImg.getLayoutParams().height = attachLayHeight/2;

        webServices = new WebServices();
        CustomSpinnerAdapter customAdapter=new CustomSpinnerAdapter(getApplicationContext(),subjectflags,subjectNames);
        subjectSpinner.setAdapter(customAdapter);
        sharedPreferences = Dashboard_main.context.getSharedPreferences(Constants.SHARED_PREF,0);
        FCM_RegisterID = sharedPreferences.getString("regId","");
        if (FCM_RegisterID.equals("")){
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("regId", refreshedToken);
            editor.commit();
        }
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = subjectSpinner.getSelectedItemPosition();
                   if(i == 7){
                       subjectTIL.setVisibility(View.VISIBLE);
                   }else{
                       subjectTIL.setVisibility(View.GONE);
                       subject.setText("");
                   }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 uname = userName.getText().toString();
                String mobileNumber = mobileNum.getText().toString();
                String dept = department.getText().toString();
                String placeValue = place.getText().toString();
                String sub = "";
                if(subjectSpinner.getSelectedItemPosition() == 0){sub="";
                }else if(subjectSpinner.getSelectedItemPosition() == 1){sub="EDUCATION";
                }else if(subjectSpinner.getSelectedItemPosition() == 2){sub="HEALTH";
                }else if(subjectSpinner.getSelectedItemPosition() == 3){sub="WATER";
                }else if(subjectSpinner.getSelectedItemPosition() == 4){sub="SANITATION";
                }else if(subjectSpinner.getSelectedItemPosition() == 5){sub="ROADS";
                }else if(subjectSpinner.getSelectedItemPosition() == 6){sub="ELECTRICITY";
                }else {sub = subject.getText().toString();
                }
                String prob = problem.getText().toString();


                if(uname.equalsIgnoreCase("") || mobileNumber.equalsIgnoreCase("")
                        || mobileNumber.length()<10 || !mobileNumber.matches(regexStr)
                        || placeValue.equalsIgnoreCase("")
                        || sub.equalsIgnoreCase("")|| prob.equalsIgnoreCase("")){

                    if (uname.isEmpty()){
                        userNameTIL.setErrorEnabled(true);
                        userNameTIL.setError("You missed the name field");
                    }
                    if(mobileNumber.isEmpty()){
                        mobileNum.requestFocus();
                        mobileTIL.setErrorEnabled(true);
                        mobileTIL.setError("You missed the mobile number field");

                    }else if(mobileNumber.length()<10 || !mobileNumber.matches(regexStr)){
                        mobileNum.requestFocus();
                        mobileTIL.setErrorEnabled(true);
                        mobileTIL.setError("Please enter a valid mobile number");
                    }
                    if (placeValue.isEmpty()){
                        place.requestFocus();
                        placeTIL.setErrorEnabled(true);
                        placeTIL.setError("You missed the place field");

                    } if(subjectSpinner.getSelectedItemPosition() == 0){
                        Toast.makeText(getApplicationContext(), "Please select the subject", Toast.LENGTH_SHORT).show();
                    }
                    if (sub.isEmpty()){
                        subject.requestFocus();
                        subjectTIL.setErrorEnabled(true);
                        subjectTIL.setError("You missed the subject field");
                    }
                    if(prob.isEmpty()){
                        problem.requestFocus();
                        problemTIL.setErrorEnabled(true);
                        problemTIL.setError("You missed the problem field");
                    }
                    // Snackbar.make(view,"Please fill all the fields", Snackbar.LENGTH_LONG).show();
                    return;
                }
                Date cDate = new Date();

                user = new User();
                user.FCM_RegisterID = FCM_RegisterID;
                user.department =dept;
                user.mobile_number = Long.parseLong(mobileNumber);
                user.name = uname;
                user.place = placeValue;
                user.problem = prob;
                user.subject = sub;
                user.date = new SimpleDateFormat("dd-MM-yyyy").format(cDate);


                String formPostData ="";
                try {
                    formPostData = formPostDataOTP(user);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (checkIsNetworkAvailable()){
                new GetOTP().execute(formPostData);
                }else{
                    errorMessage();
                }


            }
        });

        attachLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });


        return view;
    }

    private String formPostData(User user) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(user.getName(), "UTF-8");

        data += "&" + URLEncoder.encode("fcm_regid", "UTF-8") + "="
                + URLEncoder.encode(user.getFCM_RegisterID(), "UTF-8");

        data += "&" + URLEncoder.encode("mobile", "UTF-8") + "="
                + URLEncoder.encode(user.getMobile_number()+"", "UTF-8");

//        data += "&" + URLEncoder.encode("user", "UTF-8")
//                + "=" + URLEncoder.encode(user.getDepartment(), "UTF-8");

        data += "&" + URLEncoder.encode("complaint_details", "UTF-8")
                + "=" + URLEncoder.encode(user.getProblem(), "UTF-8");

        data += "&" + URLEncoder.encode("subject", "UTF-8")
                + "=" + URLEncoder.encode(user.getSubject(), "UTF-8");

        data += "&" + URLEncoder.encode("place", "UTF-8")
                + "=" + URLEncoder.encode(user.getPlace(), "UTF-8");

        return  data;
    }



    private String formPostDataOTP(User user) throws UnsupportedEncodingException {

        String data = URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(user.getName(), "UTF-8");

        data += "&" + URLEncoder.encode("mobile", "UTF-8") + "="
                + URLEncoder.encode(user.getMobile_number()+"", "UTF-8");
        return  data;
    }

    private void clearAllFields() {

        userName.setText("");
        mobileNum.setText("");
        department.setText("");
        place.setText("");
        subject.setText("");
        problem.setText("");
        subjectSpinner.setSelection(0);

    }

        public class SubmitReport extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                String result="";
                try {
                    result = webServices.postRequest(params[0], Constants.SUBMIT_REPORT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ((Dashboard_main)Dashboard_main.context).card_view_report.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                clearAllFields();
                Log.e("","******   "+s);
                ((Dashboard_main)Dashboard_main.context).card_view_report.setVisibility(View.GONE);
                try {
                    JSONObject responseObj = new JSONObject(s);
                    boolean error = responseObj.getBoolean("error");
                    boolean result = responseObj.getBoolean("result");

                    if(result){

                        showSuccessAlert();
                    }else{
                        Toast.makeText((Dashboard_main)Dashboard_main.context, "Report not submitted, please try again", Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    public class GetOTP extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String result="";
            try {
                result = webServices.postRequest(params[0], Constants.OTP_FIRST);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((Dashboard_main)Dashboard_main.context).card_view_report.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            clearAllFields();
            ((Dashboard_main)Dashboard_main.context).card_view_report.setVisibility(View.GONE);


            try {
                JSONObject responseObj = new JSONObject(s);
                boolean error = responseObj.getBoolean("error");

                if(!error){

                    try {
                        confirmOtp();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    showSuccessAlert();
                }else{
                    Toast.makeText((Dashboard_main)Dashboard_main.context, "Some problem has occured, please try again", Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //This method would confirm the otp
    private void confirmOtp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(getContext());
        View confirmDialog = li.inflate(R.layout.dialog_conform, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextConfirmOtp);
        otpTIL = (TextInputLayout) confirmDialog.findViewById(R.id.textinputlayoutotp);
        resendotp = (TextView) confirmDialog.findViewById(R.id.resendotp);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //alert.setCancelable(false);
        alertDialog = alert.create();
//        alertDialog.setCancelable(false);
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();
                if (otp.isEmpty()){
                    Toast.makeText(getContext(), "Please Enter the OTP", Toast.LENGTH_LONG).show();
                }else{
                    user.otp = otp;


                    String formPostOTP ="";
                    try {
                        formPostOTP = formPostOTP(user);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    new ConformOTP().execute(formPostOTP);

                }


            }
        });
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formResendOTP ="";
                resendotp.setVisibility(View.GONE);
                resendotp.postDelayed(new Runnable() {
                    public void run() {
                        resendotp.setVisibility(View.VISIBLE);
                    }
                }, 10000);
                try {
                    formResendOTP = formResendOTP(user);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                new ResendOTP().execute(formResendOTP);
                //Toast.makeText(getContext(), "resend otp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = Dashboard_main.context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            File file = new File(picturePath);
            attachedFileName.setText(file.getName());
            cursor.close();

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("","");

//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count == 0){
            if(userName.isFocused()){
                userNameTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(mobileNum.isFocused()){
                mobileTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(department.isFocused()){
                deptTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(place.isFocused()){
                placeTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(subject.isFocused()){
                subjectTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }else if(problem.isFocused()){
                problemTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout);
            }
        }else{
            if(userName.isFocused()){
                userNameTIL.setErrorEnabled(false);
                userName.setBackground(ContextCompat.getDrawable(Dashboard_main.context, R.drawable.textfield_active));
                userNameTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(mobileNum.isFocused()){
                mobileTIL.setErrorEnabled(false);
                mobileNum.setBackground(ContextCompat.getDrawable(Dashboard_main.context,R.drawable.textfield_active));
                mobileTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(department.isFocused()){
                deptTIL.setErrorEnabled(false);
                department.setBackground(ContextCompat.getDrawable(Dashboard_main.context,R.drawable.textfield_active));
                deptTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(place.isFocused()){
                placeTIL.setErrorEnabled(false);
                place.setBackground(ContextCompat.getDrawable(Dashboard_main.context,R.drawable.textfield_active));
                placeTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(subject.isFocused()){
                subjectTIL.setErrorEnabled(false);
                subject.setBackground(ContextCompat.getDrawable(Dashboard_main.context,R.drawable.textfield_active));
                subjectTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }else if(problem.isFocused()){
                problemTIL.setErrorEnabled(false);
                problem.setBackground(ContextCompat.getDrawable(Dashboard_main.context,R.drawable.textfield_active));
                problemTIL.setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Selected);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private String formPostOTP(User user) throws UnsupportedEncodingException {

        String otp = URLEncoder.encode("otp", "UTF-8")
                + "=" + URLEncoder.encode(user.getOtp(), "UTF-8");
        otp += "&" + URLEncoder.encode("mobile", "UTF-8") + "="
                + URLEncoder.encode(user.getMobile_number()+"", "UTF-8");

        return  otp;
    }
    //here resending the otp
    private String formResendOTP(User user) throws UnsupportedEncodingException {

        String resendotp = URLEncoder.encode("resend", "UTF-8")
                + "=" + URLEncoder.encode(user.getName(), "UTF-8");
        resendotp += "&" + URLEncoder.encode("mobile", "UTF-8") + "="
                + URLEncoder.encode(user.getMobile_number()+"", "UTF-8");

        Log.e("Resend otp params",""+resendotp);

        return  resendotp;
    }

    private class ConformOTP extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //Hiding the alert dialog
            alertDialog.dismiss();

            //Displaying a progressbar
            loading = ProgressDialog.show(getContext(), "Authenticating", "Please wait while we check the entered code", false,false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result="";
            try {
                result = webServices.postRequest(params[0], Constants.OTP_CONFORM);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String otp) {

            loading.dismiss();
            String  result ="";
            String success="false";
            JSONObject responseObj = null;
            try {
                responseObj = new JSONObject(otp);
                result = responseObj.getString("error");

                Log.e("error",""+result);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (result.equals(success)){
            String formPostData ="";
            try {
                formPostData = formPostData(user);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            try {
//                confirmOtp();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            new SubmitReport().execute(formPostData);
//                showSuccessAlert();

            }else{
//                alertDialog.show();
                Toast.makeText(getContext(), "Please enter valid OTP and try again", Toast.LENGTH_LONG).show();

            }

        }
    }

    private void showSuccessAlert() {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(getContext());
        View okDialog = li.inflate(R.layout.reportsubmit_popup, null);
        LinearLayout dialogLayout = (LinearLayout) okDialog.findViewById(R.id.dialogLayout);
        int dWidth = Dashboard_main.width - Dashboard_main.width/4;
        dialogLayout.getLayoutParams().width = dWidth;
        dialogLayout.getLayoutParams().height = dWidth;
//        okDialog.getLayoutParams().width = dWidth;
//        okDialog.getLayoutParams().height = dWidth;

        TextView text = (TextView) okDialog.findViewById(R.id.submit_text);
        text.setText("Thank you "+uname+" we will try to resolve the issue as soon as possible.");
        AlertDialog.Builder alertok = new AlertDialog.Builder(getContext());

        alertok.setView(okDialog);
        alertDialogok = alertok.create();
//        alertDialogok.getWindow().setLayout(dWidth, dWidth);
        alertDialogok.show();

    }


    private class ResendOTP extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            //Hiding the alert dialog
            alertDialog.dismiss();
            resendotp.setVisibility(View.GONE);
            loading = ProgressDialog.show(getContext(), "Authenticating", "Please wait while we send otp", false,false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result="";
            try {
                result = webServices.postRequest(params[0], Constants.SUBMIT_REPORT);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String otp) {
            Log.e("OTP resend Response",""+otp);
            loading.dismiss();
//            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }
    //Checking for network connection
    public boolean checkIsNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    public void errorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
}
