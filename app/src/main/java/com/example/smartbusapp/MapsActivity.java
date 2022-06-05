package com.example.smartbusapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.smartbusapp.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback
{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Button buttonMap;
    LatLng laUbi;
    private String lat;
    private String lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttonMap = findViewById(R.id.buttonMap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        getLocalization();
        buttonMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alerts();}
        });
    }
    private void getLocalization(){
        int perms = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (perms==PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }
    private void alerts(){
        try {
                Intent i = new Intent(this, MainActivity3.class);
                i.putExtra("dato1", lat);
                i.putExtra("dato2", lon);
           // Toast.makeText(MapsActivity.this, "Latitud " + lat, Toast.LENGTH_SHORT).show();
           // Toast.makeText(MapsActivity.this, "Longitud " + lon, Toast.LENGTH_SHORT).show();
                startActivity(i);

        }catch (Exception e){}
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

        // Add a marker in Sydney and move the camera
        /* LatLng cordS = new LatLng(19.690192115709806, -100.55558102700047);
        mMap.addMarker(new MarkerOptions().position(cordS).title("Marker in Jardín Central"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cordS));
        */
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LatLng miUbi = new LatLng(location.getLatitude(), location.getLongitude());
                 lat = String.valueOf(location.getLatitude());
                lon = String.valueOf(location.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(miUbi).title("Usted esta aqui"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbi));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbi,15));
                /*CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbi)
                       // .zoom(16)
                        //.bearing(90)
                        //.tilt(45)
                        .build();*/
                //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){

            }

            @Override
            public void onProviderEnabled(String provider){

            }

            @Override
            public void onProviderDisabled(String provider){

            }


        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,15,15,locationListener);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}