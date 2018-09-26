package com.appwestideas.mimapa;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        Intent intent = getIntent();
        int listIndex = intent.getIntExtra("listIndex",0);

        if(listIndex != 0){
            LatLng latLng = MainActivity.locations.get(listIndex);
            String title = MainActivity.places.get(listIndex);
            changeAndCenterLocation(latLng, title);
        }else {
            LatLng sydney = new LatLng(-34.6521226, -58.6510328);
            changeAndCenterLocation(sydney, "La placita de los Españoles");
        }
    }

    public void changeAndCenterLocation(LatLng latLng, String title){
        if(latLng!=null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        List<Address> addresses;
        String country = "Agua";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

            if(addresses.size()>0){
                country = addresses.get(0).getCountryName();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.locations.add(latLng);
        MainActivity.places.add(country);

        MainActivity.arrayAdapter.notifyDataSetChanged();

        changeAndCenterLocation(latLng, country);
    }
}
