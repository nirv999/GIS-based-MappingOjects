package com.example.a7lab204.gadir;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private GPSTracker userLocation;
    Button btnUserManagment, btnAddNewPoint;
    TestGPS testGPS;

    DatabaseReference mRootRefSB;
    public static final String DB_NAME_SB = "SwitchBoard";

    DatabaseReference mRootRefLP;
    public static final String DB_NAME_LP = "LightingPoles";

    String username, auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocation = new GPSTracker(this);
        testGPS = new TestGPS(getApplicationContext());
        Intent prevIntent = getIntent();
        auth = prevIntent.getStringExtra("Auth");
        username = prevIntent.getStringExtra("Username");

        btnUserManagment = (Button)findViewById(R.id.btnUserManagment);
        btnUserManagment.setOnClickListener(this);
        btnAddNewPoint = (Button)findViewById(R.id.btnAddNewPoint);
        btnAddNewPoint.setOnClickListener(this);
        if(auth.equals("VIEWER")) {
            btnUserManagment.setVisibility(View.GONE);
            btnAddNewPoint.setVisibility(View.GONE);
            btnUserManagment.setClickable(false);
            btnAddNewPoint.setClickable(false);
        }
        if(auth.equals("EDITOR")){
            btnUserManagment.setVisibility(View.GONE);
            btnUserManagment.setClickable(false);
        }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        float zoomLevel = 12.0f;
        LatLng tlv = new LatLng(32.0873782, 34.7990298);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlv, zoomLevel));

        if(userLocation.isCanGetLocation() == true)
        {
            //LatLng location = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            LatLng location = new LatLng(testGPS.getLat(), testGPS.getLng());
            mMap.addMarker(new MarkerOptions().position(location).title("אתה נמצא כאן"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
        }
        mRootRefSB= FirebaseDatabase.getInstance().getReference(DB_NAME_SB);
        mRootRefSB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                SwitchBoard switchBoard;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    switchBoard = child.getValue(SwitchBoard.class);
                    //allDetails.add(details);
                    final LatLng position = new LatLng(switchBoard.getX(), switchBoard.getY());
                    final MarkerOptions options = new MarkerOptions().position(position).title("מרכזיית תאורה").snippet(
                            "סוג מרכזיה: " + switchBoard.getType() + "\n"
                            +  "מספר מרכזיה: " + switchBoard.getNumber() + "\n"
                                    + "מספר גדיר: " + switchBoard.getGadir_num() + "\n"
                                    + "מספר מונה ח'ח: " + switchBoard.getIec_num() + "\n"
                                    + "תאריך ביקורת: " + switchBoard.getDay() + "/" + switchBoard.getMonth() + "/" + switchBoard.getYear() + "\n");


                    mMap.addMarker(options);

                    builder.include(position);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRootRefLP= FirebaseDatabase.getInstance().getReference(DB_NAME_LP);
        mRootRefLP.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LightingPoles lightingPoles;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    lightingPoles = child.getValue(LightingPoles.class);
                    //allDetails.add(details);
                    final LatLng position = new LatLng(lightingPoles.getX(), lightingPoles.getY());
                    final MarkerOptions options = new MarkerOptions().position(position).title("עמוד תאורה").snippet(
                            "סוג עמוד: " + lightingPoles.getType() + "\n"
                                    +  "מספר עמוד: " + lightingPoles.getNumber() + "\n"
                                    + "דגם עמוד: " + lightingPoles.getModel() + "\n"
                                    + "צבע העמוד: " + lightingPoles.getColor() + "\n"
                                    + " מרכזיה מקושרת: " + lightingPoles.getSwitchboard() + "\n").icon(BitmapDescriptorFactory.defaultMarker((float) 120.3));


                    mMap.addMarker(options);

                    builder.include(position);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);


                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        LatLng latlng = marker.getPosition();
                        if (marker.getTitle().equals("מרכזיית תאורה"))
                        {
                            Intent intent = new Intent(getApplicationContext(), EditSwitchBoardActivity.class);
                            intent.putExtra("lat", latlng.latitude);
                            intent.putExtra("lng", latlng.longitude);
                            intent.putExtra("Auth", auth);
                            intent.putExtra("Username", username);
                            startActivity(intent);
                        }
                       if (marker.getTitle().equals("עמוד תאורה"))
                        {
                            Intent intent = new Intent(getApplicationContext(), EditLightingPolesActivity.class);
                            intent.putExtra("lat", latlng.latitude);
                            intent.putExtra("lng", latlng.longitude);
                            intent.putExtra("Auth", auth);
                            intent.putExtra("Username", username);
                            startActivity(intent);
                        }

                    }
                });


                return info;
            }
        });




    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnUserManagment)
        {
            Intent i = new Intent(this, UsersManagementActivity.class);
            i.putExtra("Auth", auth);
            i.putExtra("Username", username);
            startActivity(i);
        }

        if(view.getId()== R.id.btnAddNewPoint)
        {
            Intent i = new Intent(this, SelectPointActivity.class);
            i.putExtra("Auth", auth);
            i.putExtra("Username", username);
            startActivity(i);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("האם אתה בטוח כי ברצונך להתנתק ממשתמש זה?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "כן",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        }
                    });

            builder1.setNegativeButton(
                    "לא",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            return true;
        }
        return false;
    }
}
