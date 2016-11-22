package com.example.computador.crud;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.mail.Quota;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TesteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TesteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class TesteFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    AlertDialogManager alert;
    private List<PlaceItem> mItems;

    GPSManager userCoordinates;
    ProgressDialog pDialog;

    RecyclerView recyclerView;
    MainActivityAdapter mainActivityAdapter;

    String latitude;
    String longitude;
    private static final String PREF_NAME = "StringActivity";
    public static String STRING_NAME = "Name";
    public static String KEY_REFERENCE = "Reference";
    public static String KEY_NAME = "Name";
    public static String KEY_ADDRESS = "Address";
    public static String KEY_TYPE = "Type";
    public static String KEY_PHOTOREFERENCE = "PhotoReference";
    public static String KEY_DISTANCE = "Distance";
    public static String KEY_LATITUDE = "Latitude";
    public static String KEY_LONGITUDE = "Longitude";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TesteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename and change types and number of parameters
    public static TesteFragment newInstance(String param1, String param2) {
        TesteFragment fragment = new TesteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.activity_list, container, false);
       /* ListView listView = (ListView) rootview.findViewById(R.id.listTeste);
        Resources res = getResources();
        String [] cities = res.getStringArray(R.array.string_array_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, cities);
        listView.setAdapter(adapter);
        */
        alert = new AlertDialogManager();
        userCoordinates = new GPSManager(getActivity());

        if (userCoordinates.canGetLocation()) {
            Log.d("Your Location", "latitude:" + userCoordinates.getLatitude() + ", longitude: " + userCoordinates.getLongitude());
        } else {
            alert.showAlertDialog(getActivity(), "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            return null;
        }

        mItems = new ArrayList<PlaceItem>();
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        latitude = Double.toString(userCoordinates.getLatitude());
        longitude = Double.toString(userCoordinates.getLongitude());
        recyclerView.addOnItemTouchListener(new ListActivity.RecyclerTouchListener(getActivity(), recyclerView, new ListActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PlaceItem placeItem = mItems.get(position);
               // Toast.makeText(get, placeItem.placeName + " is selected!", Toast.LENGTH_SHORT).show();
                Intent selectedPlace = new Intent(getActivity(), SelectedPlaceDetails.class);
                selectedPlace.putExtra(KEY_NAME, placeItem.placeName);
                selectedPlace.putExtra(KEY_ADDRESS, placeItem.placeVicinity);
                selectedPlace.putExtra(KEY_PHOTOREFERENCE, placeItem.placePhotoRef);
                selectedPlace.putExtra(KEY_LATITUDE, placeItem.placeLat);
                selectedPlace.putExtra(KEY_LONGITUDE, placeItem.placeLng);
                startActivity(selectedPlace);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        new LoadPlaces().execute(latitude, longitude);
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class LoadPlaces extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        JSONParser jsonParserDistance = new JSONParser();
        String placeName, placeVicinity, placeType, placeDistance, placeLat, placeLng, placePhotoRef;
        Double nota;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(Html.fromHtml("Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            DbHelper dbHelper = new DbHelper(getActivity());
            List<Float> notas = new ArrayList<>();
            HashMap<String, String> params = new HashMap<>();
            params.put("location", args[0]+","+args[1]);//TODO:CHANGE
            SharedPreferences sp = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String name = sp.getString("string", "");
            params.put("key", AppData.ServerAPI);
            params.put("radius", "1000");
            params.put("type", name);
            JSONObject jsonObject = jsonParser.makeHttpRequest(AppData.PLACES_SEARCH_URL, "GET", params);
            try{
                if(jsonObject.getString("status").equals("OK")){
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    Log.d("resultsArray", resultsArray.toString());
                    if(resultsArray.length()>0){
                        for(int i = 0; i < resultsArray.length(); i++) {
                            float media = 0;
                            JSONObject jObj = resultsArray.getJSONObject(i);
                            Log.d("resultsObject: " + i, jObj.toString());
                            //LAT AND LNG
                            String geometry = jObj.getString("geometry");
                            JSONObject geometryObject = new JSONObject(geometry);
                            String location = geometryObject.getString("location");
                            JSONObject locationObject = new JSONObject(location);
                            placeLat = locationObject.getString("lat");
                            placeLng = locationObject.getString("lng");
                            Log.d("Location", placeLat + ", " + placeLng);


                            placeName = jObj.getString("name");
                            Log.d("Name", placeName);

                            placeType = jObj.getString("types");
                            Log.d("Type", placeType);

                            placeVicinity = jObj.getString("vicinity");
                            Log.d("Address", placeVicinity);

                            HashMap<String, String> par = new HashMap<>();
                            par.put("origins", args[0] + "," + args[1]);
                            //par.put("origins", "40.6655101,-73.89188969999998");
                            par.put("destinations", placeLat + "," + placeLng);//TODO:Change
                            //par.put("destinations", "41.43206,-81.38992");
                            par.put("key", AppData.ServerAPI);
                            par.put("units", "metric");

                            JSONObject jsonObjectDistance = jsonParserDistance.makeHttpRequest(AppData.PLACES_DISTANCE_URL, "GET", par);
                            JSONArray distanceArray = jsonObjectDistance.getJSONArray("rows");
                            JSONObject distanceObject = distanceArray.getJSONObject(0);
                            JSONArray distanceElement = distanceObject.getJSONArray("elements");
                            JSONObject elementObject = distanceElement.getJSONObject(0);

                            if (elementObject.getString("status").equals("OK")) {
                                String distance = elementObject.getString("distance");
                                JSONObject finalObject = new JSONObject(distance);
                                placeDistance = finalObject.getString("text");
                            } else {
                                placeDistance = "NA";
                            }
                            Log.d("Distance", placeDistance);

                            boolean hasPhoto;
                            try {
                                JSONArray photoArray = jObj.getJSONArray("photos");
                                Log.d("Photo Array", photoArray.toString());
                                JSONObject photoObject = photoArray.getJSONObject(0);
                                placePhotoRef = photoObject.getString("photo_reference");
                                hasPhoto = true;
                            } catch (Exception e) {
                                hasPhoto = false;
                            }

                            if (hasPhoto) {
                                JSONArray photoArray = jObj.getJSONArray("photos");
                                Log.d("Photo Array", photoArray.toString());
                                JSONObject photoObject = photoArray.getJSONObject(0);
                                placePhotoRef = photoObject.getString("photo_reference");
                            } else {
                                placePhotoRef = "Not Available";
                            }
                            Log.d("Photo Ref", placePhotoRef);

                            String uservalue = placeName;
                            uservalue = uservalue.replace("'","\\").replace("\"", "\"\"");
                            placeName = uservalue;

                            notas = dbHelper.selectNotasPlace(placeName);

                            if (notas.size() > 0) {
                            for (int k = 0; k < notas.size(); k++) {
                                media = (media + notas.get(k) / notas.size());
                            }
                        }
                            mItems.add(new PlaceItem(placeName, placeVicinity, placeType, placeDistance, placeLat, placeLng, placePhotoRef, media));

                        }
                    }
                }else if(jsonObject.getString("status").equals("ZERO_RESULTS")){
                    alert.showAlertDialog(getActivity(), "Near Places", "Sorry no places found.", false);
                }
            }catch(JSONException je){

            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            Log.d("Items", mItems.toString());
            pDialog.dismiss();

            mainActivityAdapter = new MainActivityAdapter(mItems);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mainActivityAdapter);
            mainActivityAdapter.notifyDataSetChanged();
            ItemSorter itemSorter = new ItemSorter();

            SharedPreferences sp = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String filtro = sp.getString("filtro", "");
            if(filtro.equals("nota")){
                itemSorter.sortItemByNota(mItems);
                mainActivityAdapter = new MainActivityAdapter(mItems);
                recyclerView.setAdapter(mainActivityAdapter);
                mainActivityAdapter.notifyDataSetChanged();
            }
            else if (filtro.equals("distancia")){
                ItemSorterByDistance itemSorterByDistance = new ItemSorterByDistance();
                itemSorterByDistance.sortItemByDistance(mItems);
                mainActivityAdapter = new MainActivityAdapter(mItems);
                recyclerView.setAdapter(mainActivityAdapter);
                mainActivityAdapter.notifyDataSetChanged();
            }


        }
    }

    public class ItemSorter {
        public void sortItemByNota(List<PlaceItem> dealer) {

            Collections.sort(dealer, new Comparator<PlaceItem>() {
                @Override
                public int compare(PlaceItem lhs, PlaceItem rhs) {
                    return Float.compare(rhs.nota, lhs.nota);
                }

            });
        }
    }

    public class ItemSorterByDistance {
        public void sortItemByDistance (List<PlaceItem> dealer) {

            Collections.sort(dealer, new Comparator<PlaceItem>() {
                @Override
                public int compare(PlaceItem lhs, PlaceItem rhs) {
                    return lhs.placeDistance.compareTo(rhs.placeDistance);
                }

            });
        }
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}
