package com.example.a7lab204.gadir;

import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddSwitchBoardActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSave;
    DatabaseReference mRootRef;
    public static final String DB_NAME = "SwitchBoard";
    double lat, lng;
    String enteredUsername, auth;
    String addressLine;


    EditText etType, etNumber ,etGadirNum , etIecNum, etContractNum,
            etCirclesNum, etControllerPow, etConnectionSize, etNotes, etPlaceDesc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_switch_board);
        mRootRef = FirebaseDatabase.getInstance().getReference(DB_NAME);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
////קישור הטקסטים של האקטיבטי
        etType = (EditText)findViewById(R.id.etType);
        etNumber = (EditText)findViewById(R.id.etNumber);
        etGadirNum = (EditText)findViewById(R.id.etGadirNum);
        etIecNum = (EditText)findViewById(R.id.etIecNum);
        etContractNum = (EditText)findViewById(R.id.etContractNum);
        etCirclesNum = (EditText)findViewById(R.id.etCirclesNum);
        etControllerPow = (EditText)findViewById(R.id.etControllerPow);
        etConnectionSize = (EditText)findViewById(R.id.etConnectionSize);
        etNotes = (EditText)findViewById(R.id.etNotes);
        etPlaceDesc = (EditText)findViewById(R.id.etPlaceDesc);


        Intent prevIntent = getIntent();
        lat= prevIntent.getDoubleExtra("lat",0.0);
        lng= prevIntent.getDoubleExtra("lng",0.0);
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("Username");

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addressLine = addresses.get(0).getAddressLine(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        //etGadirNum , etIecNum, etContractNum, etCirclesNum, etControllerNum, etConnectionSize//

        if(view.getId() == R.id.btnSave && !etType.getText().toString().equals("")
                                        && !etNumber.getText().toString().equals("")
                                        && !etGadirNum.getText().toString().equals("")
                                        && !etIecNum.getText().toString().equals("")
                                        && !etContractNum.getText().toString().equals("")
                                        && !etCirclesNum.getText().toString().equals("")
                                        && !etControllerPow.getText().toString().equals("")
                                        && !etConnectionSize.getText().toString().equals(""))
        {
            Time Today = new Time(Time.getCurrentTimezone());
            Today.setToNow();
            String id = mRootRef.push().getKey();
            SwitchBoard sb=new SwitchBoard(etType.getText().toString(),
                    etNumber.getText().toString(),
                    etGadirNum.getText().toString(),
                    lat,lng,
                    etIecNum.getText().toString(),
                    etContractNum.getText().toString(),
                    etCirclesNum.getText().toString(),
                    etControllerPow.getText().toString(),
                    etConnectionSize.getText().toString(),
                    etNotes.getText().toString(),addressLine,
                    etPlaceDesc.getText().toString(),
                    Today.monthDay,Today.month+1,Today.year );
            mRootRef.child(id).setValue(sb);
            Toast.makeText(this, "נתונים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("enteredUsername", enteredUsername);
            startActivity(intent);
        }

        else{
            Toast.makeText(this, "לא הוזנו שדות חובה!", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(),SelectPointActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("Username", enteredUsername);
            startActivity(intent);

            return true;
        }
        return false;
    }
}
