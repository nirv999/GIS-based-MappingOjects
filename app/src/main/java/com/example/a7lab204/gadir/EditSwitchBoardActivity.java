package com.example.a7lab204.gadir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditSwitchBoardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;
    DatabaseReference mRootRef;
    public static final String DB_NAME = "SwitchBoard";
    double lat, lng;
    String enteredUsername, auth;
    String addressLine;
    Button btnPointMove;

    EditText etType, etNumber ,etGadirNum , etIecNum, etContractNum,
            etCirclesNum, etControllerPow, etConnectionSize, etNotes, etPlaceDesc ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_switch_board);

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

        btnPointMove =(Button)findViewById(R.id.btnPointMove);
        btnPointMove.setOnClickListener(this);

        Intent prevIntent = getIntent();
        lat= prevIntent.getDoubleExtra("lat",0.0);
        lng= prevIntent.getDoubleExtra("lng",0.0);
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("Username");



        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SwitchBoard sb;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    sb = child.getValue(SwitchBoard.class);
                    if(sb.getX()== lat && sb.getY()== lng) {

                        etType.setText(sb.getType().toString());
                        etNumber.setText(sb.getNumber());
                        etCirclesNum.setText(sb.getNum_of_circles());
                        etConnectionSize.setText(sb.getConnection_size());
                        etContractNum.setText(sb.getContract_num());
                        etControllerPow.setText(sb.getController_power());
                        etGadirNum.setText(sb.getGadir_num());
                        etIecNum.setText(sb.getIec_num());
                        etNotes.setText(sb.getNotes());
                        etPlaceDesc.setText(sb.getPlace_desc());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnPointMove) {
            Intent intent = new Intent(this, MovePointActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("Auth", auth);
            intent.putExtra("enteredUsername", enteredUsername);
            startActivity(intent);
        }

        if(view.getId() == R.id.btnSave
                && !etType.getText().toString().equals("")
                && !etNumber.getText().toString().equals("")
                && !etGadirNum.getText().toString().equals("")
                && !etIecNum.getText().toString().equals("")
                && !etContractNum.getText().toString().equals("")
                && !etCirclesNum.getText().toString().equals("")
                && !etControllerPow.getText().toString().equals("")
                && !etConnectionSize.getText().toString().equals("")) {

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SwitchBoard sb;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        sb = child.getValue(SwitchBoard.class);
                        if(sb.getX()== lat && sb.getY()== lng) {

                            child.child("type").getRef().setValue(etType.getText().toString());
                            child.child("number").getRef().setValue(etNumber.getText().toString());
                            child.child("gadir_num").getRef().setValue(etGadirNum.getText().toString());
                            child.child("iec_num").getRef().setValue(etIecNum.getText().toString());
                            child.child("contract_num").getRef().setValue(etContractNum.getText().toString());
                            child.child("num_of_circles").getRef().setValue(etCirclesNum.getText().toString());
                            child.child("controller_power").getRef().setValue(etControllerPow.getText().toString());
                            child.child("connection_size").getRef().setValue(etConnectionSize.getText().toString());
                            child.child("notes").getRef().setValue(etNotes.getText().toString());
                            child.child("place_desc").getRef().setValue(etPlaceDesc.getText().toString());


                            Toast.makeText(getApplicationContext(), "נתונים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
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
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("Username", enteredUsername);
            startActivity(intent);

            return true;
        }
        return false;
    }
}
