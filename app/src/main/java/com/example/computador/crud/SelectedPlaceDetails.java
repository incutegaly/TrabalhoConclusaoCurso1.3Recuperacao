package com.example.computador.crud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SelectedPlaceDetails extends AppCompatActivity implements OnMapReadyCallback {
    private static final String PREF_NAME = "LoginActivityPreference";
    MapFragment mapFragment;
    GPSManager gpsManager;
    GoogleMap googleMap;
    android.support.v7.app.ActionBar actionBar;

    AlertDialogManager alert;
    ListView listView;
    TextView textView;
    ImageView placeImage;
    TextView placeStatus;

    LoadImage loadImage;

    String placeName, placeVicinity, placeLat, placeLng, placePhotoRef, placeDistance, placeType;

    public static String KEY_NAME = "Name";
    public static String KEY_ADDRESS = "Address";
    public static String KEY_PHOTOREFERENCE = "PhotoReference";
    public static String KEY_LATITUDE = "Latitude";
    public static String KEY_LONGITUDE = "Longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_place);
        Intent fromMain = getIntent();
        placeVicinity = fromMain.getStringExtra(KEY_ADDRESS);
        placeName = fromMain.getStringExtra(KEY_NAME);
        placeLat = fromMain.getStringExtra(KEY_LATITUDE);
        placeLng = fromMain.getStringExtra(KEY_LONGITUDE);
        placePhotoRef = fromMain.getStringExtra(KEY_PHOTOREFERENCE);
        setTitle(placeName);
        gpsManager = new GPSManager(this);


        listView = (ListView)findViewById(R.id.listView);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Name", placeName);
        editor.commit();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        alert = new AlertDialogManager();

        placeStatus = (TextView) findViewById(R.id.placeStatus);
        placeImage = (ImageView) findViewById(R.id.placeImage);
        textView = (TextView) findViewById(R.id.coment);



        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(placePhotoRef.equals("Not Available")){
            placeStatus.setText("Sorry Image Not Available.\nAddress : " + placeVicinity);
        }else{
            loadImage = new LoadImage();
            loadImage.execute(placePhotoRef);
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void onResume(){
        super.onResume();
        List<String> lista;
        List<Avaliacao> arrayList;
        DbHelper dbH = new DbHelper(this);
        try{

            lista = dbH.selectComentario(placeName);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista );
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);

        }catch (Exception e ){
            Log.i("Error:  ", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng placeLocation = new LatLng(Double.parseDouble(placeLat), Double.parseDouble(placeLng));
        Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation).title(placeName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
    }


    class LoadImage extends AsyncTask<String, String, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(SelectedPlaceDetails.this);
            pDialog.setMessage("Fetching place image...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try{
                InputStream inputStream = new URL(AppData.PLACES_IMAGE_URL + "&key=" + AppData.ServerAPI + "&photoreference=" + params[0]).openStream();
                return BitmapFactory.decodeStream(inputStream);
            }catch(MalformedURLException mue){
                return null;
            }catch (IOException ioe){
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmapImage) {
            pDialog.dismiss();
            if(bitmapImage != null){
                placeStatus.setText("Address : " + placeVicinity);
                placeImage.setImageBitmap(bitmapImage);
            }else{
                placeStatus.setText("Sorry failed to retrieve Image.\nAddress : " + placeVicinity);
                alert.showAlertDialog(SelectedPlaceDetails.this, "Place Image", "Sorry failed to retrieve Image.", false);
            }
            placeStatus.setText(placeVicinity);
        }

    }

    public void avaliar(View v ){
        Intent selectedPlace= new Intent(SelectedPlaceDetails.this, Avalicao.class);
        selectedPlace.putExtra(KEY_NAME, placeName);
        selectedPlace.putExtra(KEY_ADDRESS, placeVicinity);
        selectedPlace.putExtra(KEY_PHOTOREFERENCE, placePhotoRef);
        selectedPlace.putExtra(KEY_LATITUDE, placeLat);
        selectedPlace.putExtra(KEY_LONGITUDE, placeLng);
        startActivity(selectedPlace);
    }

}