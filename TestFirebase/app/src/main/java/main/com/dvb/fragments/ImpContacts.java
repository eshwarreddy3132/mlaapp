package main.com.dvb.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import main.com.dvb.Constants;
import main.com.dvb.Dashboard_main;
import main.com.dvb.R;
import main.com.dvb.adapters.ContactsAdapter;
import main.com.dvb.pojos.Contacts;
import main.com.dvb.services.WebServices;

/**
 * Created by SREEVATSAVA on 30-07-2016.
 */
public class ImpContacts extends Fragment{

    ListView list;
    ContactsAdapter contactsadapter;
    WebServices webServices;
    List<Contacts> contactslist = new ArrayList<Contacts>();
    Contacts contacts;
    ProgressBar progressBar;
    Toolbar toolbar;
    EditText search;

    public static ImpContacts impContacts;
    public ImpContacts() {
        // Required empty public constructor
        impContacts = ImpContacts.this;
        webServices= new WebServices();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.imp_contacts, container, false);
//        bottomImage = (ImageView) view.findViewById(R.id.bottomImage);
//
//        bottomImage.getLayoutParams().width = (int) (Dashboard_main.width/3f);
        progressBar = (ProgressBar) view.findViewById(R.id.eventslist_progressbar);
        list = (ListView) view.findViewById(R.id.listview);
        search = (EditText) view.findViewById(R.id.inputSearch);
        new FechContacts().execute();
        contactsadapter = new ContactsAdapter(Dashboard_main.context, contactslist);
        list.setAdapter(contactsadapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                contactsadapter.filter(text);

            }
        });
        return view;
    }

    class FechContacts extends AsyncTask<String,Void,String> {
        String response;
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                response= webServices.getRequest(Constants.IMP_CONTACTS);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);

            JSONArray arr = null;
            try {
                arr =new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Parsing json
            for (int i = 0; i < response.length(); i++) {
                try {

                    JSONObject obj =  arr.getJSONObject(i);
                    Contacts contacts=new Contacts();
                    contacts.setName(obj.getString("department"));
                    contacts.setPhoneNumber(obj.getString("mobile"));
                    contacts.setDesignation(obj.getString("designation"));
                    contactslist.add(contacts);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            contactsadapter.notifyDataSetChanged();


        }
    }

}
