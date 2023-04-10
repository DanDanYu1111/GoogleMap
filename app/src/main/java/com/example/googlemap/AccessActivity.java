package com.example.googlemap;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccessActivity extends AppCompatActivity {
    private static String testData;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    protected void onCreate(Bundle savedInstanceState) {
        Log.i("database","onCreate的第一行");
            String url = "jdbc:mysql:// 172.20.10.3:3306/testDB";
            String username = "root";
            String password = "root";

            Connection conn = null;
            try {
                Log.i("database","尝试连接数据库");
                conn = DriverManager.getConnection(url, username, password);
                Log.i("database","数据库连接成功");
            } catch (SQLException e) {
                Log.i("database","数据库连接失败");
            } finally {
                Log.i("database","进入finally");
                if (conn != null) {
                    try {

                        Statement statement = conn.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * From testtable where id=1");
                        resultSet.next();
                        String data1=resultSet.getString(2);
                        String data2=resultSet.getString(3);
                        String data3=resultSet.getString(4);

                        testData=data1;

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
     // testData="My Current Position";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.access_main);
//
        Button button =findViewById(R.id.button_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(AccessActivity.this, MapActivity.class);
                    startActivity(intent);
            }
        });
////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Dexter.withContext(getApplicationContext()).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
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

                            MarkerOptions marker_myLocation=new MarkerOptions().position(myLatLng).title(testData);
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

                                    Toast.makeText(getApplicationContext(),"经度:"+marker.getPosition().latitude+"\n纬度："+marker.getPosition().longitude+"\n"+testData,Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            });
                        }
                    }
                });
            }
        });
    }
        //////////////////////////////////////



}