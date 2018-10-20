package com.example.a7lab204.gadir;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MoveLightingPolesActivity  extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener{

    private GoogleMap mMap;
    Button btnSave;
    double lat, lng;
    String auth, enteredUsername;
    Marker markerOnTheMap;

    DatabaseReference mRootRef;
    public static final String DB_NAME = "LightingPoles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_lighting_poles);

        mRootRef = FirebaseDatabase.getInstance().getReference(DB_NAME);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);


        Intent prevIntent = getIntent();
        lat = prevIntent.getDoubleExtra("lat", 0.0);
        lng = prevIntent.getDoubleExtra("lng", 0.0);
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("Username");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        float zoomLevel = 12.0f;
        LatLng tlv = new LatLng(32.0873782, 34.7990298);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlv, zoomLevel));

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LightingPoles lp;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    lp = child.getValue(LightingPoles.class);
                    if (lp.getX() == lat && lp.getY() == lng) {

                        final LatLng position = new LatLng(lp.getX(), lp.getY());
                        final MarkerOptions options = new MarkerOptions().position(position);
                        markerOnTheMap = mMap.addMarker(options);
                    } else {
                        final LatLng position = new LatLng(lp.getX(), lp.getY());
                        final MarkerOptions options = new MarkerOptions().position(position).
                                icon(BitmapDescriptorFactory.defaultMarker((float) 212.0));

                        mMap.addMarker(options);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker toDelete = markerOnTheMap;
                toDelete.remove();
                markerOnTheMap = mMap.addMarker(new MarkerOptions().position(latLng));

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnSave) {
            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    LightingPoles lp;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        lp = child.getValue(LightingPoles.class);
                        if (lp.getX() == lat && lp.getY() == lng) {
                            child.child("x").getRef().setValue(markerOnTheMap.getPosition().latitude);
                            child.child("y").getRef().setValue(markerOnTheMap.getPosition().longitude);

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(markerOnTheMap.getPosition().latitude,markerOnTheMap.getPosition().longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            child.child("address").getRef().setValue(addresses.get(0).getAddressLine(0));

                            Toast.makeText(getApplicationContext(), "שינוי מיקום נשמר בהצלחה", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),EditLightingPolesActivity.class);
                            intent.putExtra("lat", markerOnTheMap.getPosition().latitude);
                            intent.putExtra("lng", markerOnTheMap.getPosition().longitude);
                            intent.putExtra("Auth", auth);
                            intent.putExtra("enteredUsername", enteredUsername);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(),EditLightingPolesActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("Username", enteredUsername);
            startActivity(intent);

            return true;
        }
        return false;
    }
}
