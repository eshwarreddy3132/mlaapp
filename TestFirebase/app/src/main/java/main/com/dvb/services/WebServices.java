package main.com.dvb.services;

import android.util.Base64;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import main.com.dvb.Constants;
import main.com.dvb.pojos.User;
import main.com.dvb.pojos.twitter.Authenticated;

import static main.com.dvb.Constants.TwitterStreamURL;

/**
 * Created by AIA on 11/30/16.
 */

public class WebServices {
    static int len = 500;

    public String getRequest(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
//            URLConnection conn = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
//            int response = conn.getResponseCode();
//            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String postRequest(String data, String serviceUrl) throws UnsupportedEncodingException{

        String contentAsString = "";

//        String data = URLEncoder.encode("name", "UTF-8")
//                + "=" + URLEncoder.encode(user.getName(), "UTF-8");
//
//        data += "&" + URLEncoder.encode("fcm_regid", "UTF-8") + "="
//                + URLEncoder.encode("12345"+"", "UTF-8");
//
//        data += "&" + URLEncoder.encode("mobile", "UTF-8") + "="
//                + URLEncoder.encode(user.getMobile_number()+"", "UTF-8");
//
////        data += "&" + URLEncoder.encode("user", "UTF-8")
////                + "=" + URLEncoder.encode(user.getDepartment(), "UTF-8");
//
//        data += "&" + URLEncoder.encode("complaint_details", "UTF-8")
//                + "=" + URLEncoder.encode(user.getProblem(), "UTF-8");
//
//        data += "&" + URLEncoder.encode("subject", "UTF-8")
//                + "=" + URLEncoder.encode(user.getSubject(), "UTF-8");
//
//        data += "&" + URLEncoder.encode("place", "UTF-8")
//                + "=" + URLEncoder.encode(user.getPlace(), "UTF-8");

        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(serviceUrl);

            // Send POST data request

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            conn.connect();
            // Get the server response
            InputStream is = conn.getInputStream();
            contentAsString = readIt(is, len);

//            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//
//            // Read Server Response
//            while((line = reader.readLine()) != null)
//            {
//                // Append server response in string
//                sb.append(line + "\n");
//            }
//
//
//            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
//        finally
//        {
//            try
//            {
//
//                reader.close();
//            }
//
//            catch(Exception ex) {}
//        }
//        contentAsString = contentAsString.replaceFirst("12345","");
        // Show response on activity
        return contentAsString;
    }

    public void postRequest_Twitter(String data, String serviceUrl){

        // Step 1: Encode consumer key and secret
        try {
            // URL encode the consumer key and secret
            String urlApiKey = URLEncoder.encode(Constants.TWITTER_KEY, "UTF-8");
            String urlApiSecret = URLEncoder.encode(Constants.TWITTER_SECRET, "UTF-8");

            // Concatenate the encoded consumer key, a colon character, and the
            // encoded consumer secret
            String combined = urlApiKey + ":" + urlApiSecret;

            // Base64 encode the string
            String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

            // Step 2: Obtain a bearer token
            URL url = new URL(Constants.TwitterTokenURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Authorization", "Basic " + base64Encoded);



            conn.setDoOutput(true);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            conn.connect();
            // Get the server response
            InputStream is = conn.getInputStream();
            String results = readIt(is, len);
            jsonToAuthenticated(results);

//            if (auth != null && auth.token_type.equals("bearer")) {
//
//                getRequest_twitter(auth, TwitterStreamURL + "urstrulyMahesh"+"&count=10");
//                getRequest(TwitterStreamURL +"urstrulyMahesh&max_id=750894636219011072"+"&count=10");
//                // Step 3: Authenticate API requests with bearer token
////                HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName +"&max_id=750894636219011072"+"&count=10");
//
//                // construct a normal HTTPS request and include an Authorization
//                // header with the value of Bearer <>
//                httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
//                httpGet.setHeader("Content-Type", "application/json");
//                // update the results with the body of the response
//                results = getResponseBody(httpGet);
//            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // convert a JSON authentication object into an Authenticated object
    private void jsonToAuthenticated(String rawAuthorization) {
        Authenticated auth = null;
        if (rawAuthorization != null && rawAuthorization.length() > 0) {
            try {
                Gson gson = new Gson();
                Constants.authenticated = gson.fromJson(rawAuthorization, Authenticated.class);
            } catch (IllegalStateException ex) {
                // just eat the exception
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {

//        byte[] bytes = new byte[1000];
        char[] buffer = new char[len];

        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");

//        BufferedReader r = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder total = new StringBuilder();
//        String line;
        int numRead = 0;
        while ((numRead = reader.read(buffer)) >= 0) {
            total.append(new String(buffer, 0, numRead));
        }

//        while ((line = r.readLine()) != null) {
//            total.append(line);
//        }

//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
////        reader.read(buffer);
//        reader.read();
        return total.toString();
    }


    public String getRequest_twitter(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
//            URLConnection conn = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + Constants.authenticated.access_token);

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
//            int response = conn.getResponseCode();
//            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
