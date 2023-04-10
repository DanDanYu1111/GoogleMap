package com.example.googlemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

 public class MapActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                           while(true){
                            Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG);
                    }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        while(true){
                            Toast.makeText(getApplicationContext(),"Permission Should Be Shown",Toast.LENGTH_LONG);
                        }
                    }


                }).check();
    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if(location!=null){
                            LatLng myLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                            LatLng ParisLatLng=new LatLng(48.85860776408882, 2.3518395875561215);
                            LatLng NewYorkLatLng=new LatLng(40.72525550418975, -73.99894180900309);
                            LatLng LondonLatLng=new LatLng(51.5110405451875, -0.13250497992199345);

                            MarkerOptions marker_myLocation=new MarkerOptions().position(myLatLng).title("Current Location");
                            MarkerOptions marker_Paris=new MarkerOptions().position(ParisLatLng).title("Paris");
                            MarkerOptions marker_NewYork=new MarkerOptions().position(NewYorkLatLng).title("NewYork");
                            MarkerOptions marker_London=new MarkerOptions().position(LondonLatLng).title("London");

                            googleMap.addMarker(marker_myLocation);
                            googleMap.addMarker(marker_Paris);
                            googleMap.addMarker(marker_NewYork);
                            googleMap.addMarker(marker_London);

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,12));

                            Button  button_myLocation,
                                    button_Paris,
                                    button_NewYork,
                                    button_London;

                            button_myLocation=findViewById(R.id.button_myLocation);
                            button_Paris=findViewById(R.id.button_Paris);
                            button_NewYork=findViewById(R.id.button_NewYork);
                            button_London=findViewById(R.id.button_London);

                            button_myLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,12));
                                }

                            });

                            button_Paris.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ParisLatLng,12));
                                }

                            });
                            button_NewYork.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NewYorkLatLng,12));
                                }

                            });

                            button_London.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LondonLatLng,12));
                                }

                            });





                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {

                                    Toast.makeText(getApplicationContext(),"经度:"+marker.getPosition().latitude+"\n纬度："+marker.getPosition().longitude,Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            });

                        }

                    }
                });
            }
        });

    }
}