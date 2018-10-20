package com.example.a7lab204.gadir;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectPointActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap addPointMap;
    private GPSTracker userLocation;
    Button btnAddSwitchBoard, btnAddLightingPole;
    private double lat=0.0,lng=0.0;
    String auth, enteredUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_point);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addPointMap);
        mapFragment.getMapAsync(this);

        userLocation = new GPSTracker(this);

        btnAddLightingPole = (Button)findViewById(R.id.btnAddLightingPole);
        btnAddLightingPole.setOnClickListener(this);
        btnAddSwitchBoard = (Button)findViewById(R.id.btnAddSwitchBoard);
        btnAddSwitchBoard.setOnClickListener(this);

        Intent prevIntent = getIntent();
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("Username");


    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnAddSwitchBoard ) {
            if(lat!= 0.0 && lng!=0.0) {
                Intent intent = new Intent(this, AddSwitchBoardActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("Auth", auth);
                intent.putExtra("enteredUsername", enteredUsername);
                startActivity(intent);
            }
            else
                Toast.makeText(this, "לא נבחרה נקודה", Toast.LENGTH_SHORT).show();
        }
        if(view.getId()==R.id.btnAddLightingPole) {
            Intent intent = new Intent(this, AddLightingPolesActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("Auth", auth);
            intent.putExtra("enteredUsername", enteredUsername);
            startActivity(intent);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        addPointMap = googleMap;
        float zoomLevel = 12.0f;
        LatLng tlv = new LatLng(32.0873782, 34.7990298);
        addPointMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlv, zoomLevel));

        if(userLocation.isCanGetLocation() == true)
        {
            LatLng location = new LatLng(userLocation.getLongitude(), userLocation.getLatitude());
            addPointMap.addMarker(new MarkerOptions().position(location).title("אתה נמצא כאן"));
            addPointMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
        }

        addPointMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat = latLng.latitude;
                lng = latLng.longitude;
                addPointMap.clear();
                addPointMap.addMarker(new MarkerOptions().position(latLng));

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("Username", enteredUsername);
            startActivity(intent);

            return true;
        }
        return false;
    }
}
