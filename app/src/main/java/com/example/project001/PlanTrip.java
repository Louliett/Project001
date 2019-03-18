package com.example.project001;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project001.database.Trip;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlanTrip extends Fragment  implements AdapterView.OnItemClickListener {











    AutoCompleteTextView autoCompView;
    AutoCompleteTextView autoCompView2;

    //variables
    public static String date;
    public static String time;
    public static String email;
    public static EditText departure;
    public static EditText destination;
    public static EditText price;
    public static EditText seats;
    Button createButton;


    Trip trip;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_trip, container, false);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        autoCompView = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        autoCompView2 = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView2);
        autoCompView2.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        autoCompView2.setOnItemClickListener(this);


        if(getArguments() != null) {
            email = getArguments().getString("email");
            //Log.e("tripFragment", email);
        } else {
            Log.e("doesnt", "work");
        }


        Log.e("something", "happening");



        price = getView().findViewById(R.id.priceTXT);
        seats = getView().findViewById(R.id.seatsTXT);

        final TimePicker timePicker = getView().findViewById(R.id.time);
        final DatePicker datePicker = getView().findViewById(R.id.date);

        createButton = getView().findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = timePicker.getHour();
                int min = timePicker.getMinute();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                time = String.valueOf(hour) + ":" + String.valueOf(min);
                date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

                addTrip();

            }
        });
    }


    public void addTrip() {

        trip = new Trip(
                date,
                time,
                autoCompView.getText().toString(),
                autoCompView2.getText().toString(),
                price.getText().toString(),
                seats.getText().toString(),
                email
        );

        final ArrayList<Trip> list = new ArrayList<>();

        list.add(trip);
        System.out.println(list);

        if(autoCompView.getText().toString().isEmpty() ||
                autoCompView2.getText().toString().isEmpty() ||
                date.isEmpty() ||
                price.getText().toString().isEmpty() ||
                seats.getText().toString().isEmpty() ||
                time.isEmpty()) {

            createButton.setClickable(false);

        } else {

            Intent myIntent = new Intent(getActivity(), ConfirmTrip.class);
            myIntent.putParcelableArrayListExtra("An Object", list);
            PlanTrip.this.startActivity(myIntent);

        }

        createButton.setClickable(true);

    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();

    }
}


class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList resultList;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return (String) resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            private ArrayList autocomplete(String toString) {
                String LOG_TAG = "GooglePlaceAutocomplete";
                 String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
                 String TYPE_AUTOCOMPLETE = "/autocomplete";
                 String OUT_JSON = "/json";
                  String API_KEY = "AIzaSyC6m2lEXUWFI7csc1393xi-ldWIe_SC9FM";

                ArrayList resultList = null;

               HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                try {
                    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                    sb.append("?key=" + API_KEY);
                    sb.append("&components=country:se");
                    sb.append("&input=" + URLEncoder.encode(toString, "utf8"));

                    URL url = new URL(sb.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());

                    // Load the results into a StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                        System.out.println(jsonResults);
                    }
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error processing Places API URL", e);
                    return resultList;
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error connecting to Places API", e);
                    return resultList;
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

                try {
                    // Create a JSON object hierarchy from the results
                    JSONObject jsonObj = new JSONObject(jsonResults.toString());
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    // Extract the Place descriptions from the results
                    resultList = new ArrayList(predsJsonArray.length());
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                        System.out.println("============================================================");
                        resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Cannot process JSON results", e);
                }

                return resultList;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();

                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}

