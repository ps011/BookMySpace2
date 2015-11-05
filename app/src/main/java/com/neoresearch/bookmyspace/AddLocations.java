    package com.neoresearch.bookmyspace;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

    public class AddLocations extends Fragment implements View.OnClickListener {

    EditText location_name, h_no, street, area, space;
    protected ProgressDialog pDialog;
    ImageButton redirect;
    String mobile, loc_name, house, street_n, area_n, space_n,longitude,latitude;
     Double longi,lat;
    SessionManager session;
    JSONParser jsonParser = new JSONParser();
    Button add;

    private static final String LOCATION_URL = "http://prasheel-acropolis.rhcloud.com/add_locations.php";
    private static final String TAG_SUCCESS = "success";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addlocations, container, false);
        location_name = (EditText) v.findViewById(R.id.location_name);

        h_no = (EditText) v.findViewById(R.id.home_number);
        space = (EditText) v.findViewById(R.id.space);
        street = (EditText) v.findViewById(R.id.street);
        area = (EditText) v.findViewById(R.id.area);
        add=(Button)v.findViewById(R.id.add_locations);

       session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        redirect = (ImageButton) v.findViewById(R.id.imageButton4);
        redirect.setOnClickListener(this);
        add.setOnClickListener(this);
        mobile = user.get(SessionManager.KEY_MOBILE);


        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_locations:
                new AddLocation().execute();

            case R.id.imageButton4:
                ((ProvideParking) getActivity()).setCurrentItem(0, true);

        }
    }


    class AddLocation extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Location...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();



        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            house = h_no.getText().toString();
            space_n = space.getText().toString();
            area_n = area.getText().toString();
             street_n = street.getText().toString();
            loc_name = location_name.getText().toString();


            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("mobile", mobile));
                params.add(new BasicNameValuePair("location_name", loc_name));
                params.add(new BasicNameValuePair("house_no", house));
                params.add(new BasicNameValuePair("street", street_n));
                params.add(new BasicNameValuePair("area", area_n));
                params.add(new BasicNameValuePair("space", space_n));


                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOCATION_URL, "POST", params);
                // check your log for json response
                Log.d("Sending Mobile", json.toString());
                // json success tag
                success = json.getInt(TAG_SUCCESS);


                if (success == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Locations Added!!!", Toast.LENGTH_LONG).show();
                    Log.d("Locations Added", json.toString());
                    //***CODE HERE TO SHOW LOCATIONS*****

                    return json.getString(TAG_SUCCESS);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured!!!", Toast.LENGTH_LONG).show();
                    Log.d("Error Occured", json.getString(TAG_SUCCESS));
                    //*****CODE HERE TO SHOW EMPTY SCREEN********

                    return json.getString(TAG_SUCCESS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();


            if (file_url != null) {
                Toast.makeText(getActivity().getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
            }


        }

    }





    }




