package com.example.a32150.a2017110902.Map;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a32150.a2017110902.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng location;
    Intent it;

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
        it = getIntent();//取得從main activity過來的data
        String latitude = it.getStringExtra("latitude");
        String longitude = it.getStringExtra("longitude");
        String city = it.getStringExtra("city");
        String loc = it.getStringExtra("loc");
        Log.d("DATA","longitude="+longitude+" latitude="+latitude);
        // Add a marker in Sydney and move the camera
        location = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)) ;
        mMap.addMarker(new MarkerOptions().position(location).title(city+" "+loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.camera_2)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}