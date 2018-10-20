package com.example.a7lab204.gadir;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditLightingPolesActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSave, btnUpload;
    DatabaseReference mRootRef;
    public static final String DB_NAME = "LightingPoles";
    double lat, lng;
    String enteredUsername, auth;
    String addressLine;
    Button btnPointMove;
    String picid= "";

    Button btnChooseImg, btnUploadImg;
    ImageView imgView;
    int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;
    ProgressDialog pd;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://gadir-ns.appspot.com/"); // copy from firebase this line


    EditText etType, etNumber ,etHeight, etModel, etMaterial, etArmType, etSwitchBoard, etColor,
            etEndLine, etBase,etGModel ,etGPower, etGNumber, etFlags, etNotes;
    Switch swAntennas, swCameras, swWires, swSockets, swSolarf;

    private final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lighting_poles);



        mRootRef = FirebaseDatabase.getInstance().getReference(DB_NAME);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnChooseImg = (Button)findViewById(R.id.btnChooseImg);
        btnUploadImg = (Button)findViewById(R.id.btnUploadImg);
        imgView = (ImageView)findViewById(R.id.imgView);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");


////קישור הטקסטים של האקטיבטי
        etType = (EditText)findViewById(R.id.etType);
        etNumber = (EditText)findViewById(R.id.etNumber);
        etHeight = (EditText)findViewById(R.id.etHeight);
        etModel = (EditText)findViewById(R.id.etModel);
        etMaterial = (EditText)findViewById(R.id.etMaterial);
        etArmType = (EditText)findViewById(R.id.etArmType);
        etSwitchBoard = (EditText)findViewById(R.id.etSwitchBoard);
        etColor = (EditText)findViewById(R.id.etColor);
        etEndLine = (EditText)findViewById(R.id.etEndLine);
        etBase = (EditText)findViewById(R.id.etBase);
        etGModel  = (EditText)findViewById(R.id.etGModel);
        etGPower  = (EditText)findViewById(R.id.etGPower);
        etGNumber  = (EditText)findViewById(R.id.etGNumber);
        etFlags  = (EditText)findViewById(R.id.etFlags);
        etNotes  = (EditText)findViewById(R.id.etNotes);

        swAntennas = (Switch)findViewById(R.id.swAntennas);
        swCameras = (Switch)findViewById(R.id.swCameras);
        swWires = (Switch)findViewById(R.id.swWires);
        swSockets = (Switch)findViewById(R.id.swSockets);
        swSolarf = (Switch)findViewById(R.id.swSolarf);

        btnPointMove =(Button)findViewById(R.id.btnPointMove);
        btnPointMove.setOnClickListener(this);

        Intent prevIntent = getIntent();
        lat= prevIntent.getDoubleExtra("lat",0.0);
        lng= prevIntent.getDoubleExtra("lng",0.0);
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("Username");

        //StorageReference srTemp=FirebaseStorage.getInstance().getReference().child("image"+"_"+picid+".jpg");



        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LightingPoles lp;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    lp = child.getValue(LightingPoles.class);
                    if(lp.getX()== lat && lp.getY()== lng) {

                        etType.setText(lp.getType().toString());
                        etNumber.setText(lp.getNumber());
                        etHeight.setText(lp.getHeight());
                        etModel.setText(lp.getModel());
                        etMaterial.setText(lp.getMaterial());
                        etArmType.setText(lp.getArm_type());
                        etSwitchBoard.setText(lp.getSwitchboard());
                        etColor.setText(lp.getColor());
                        etEndLine.setText(lp.getEnd_line());
                        etBase.setText(lp.getBase());
                        etGModel.setText(lp.getG_model());
                        etGPower.setText(lp.getG_power());
                        etGNumber.setText(lp.getG_count());
                        etFlags.setText(lp.getFlag_nests());
                        swAntennas.setChecked(lp.isAntennas());
                        swCameras.setChecked(lp.isCameras());
                        swWires.setChecked(lp.isWires());
                        swSockets.setChecked(lp.isSockets());
                        swSolarf.setChecked(lp.isSolar_flashlight());
                        picid = lp.getPicid();



                    }
                }


                storageRef.child("image"+"_"+picid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        //Uri uri = storageRef.child("image"+"_"+picid+".jpg").getDownloadUrl().getResult();
                        Picasso.get().load(uri).into(imgView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                // Load the image using Glide
               /* Glide.with(getApplicationContext())
                        .load(srTemp)
                        .into(imgView);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }



    @Override
    public void onClick(View view) {

     /*   if(view.getId() == R.id.btnUpload)
        {
            selectImage();
        }*/

        if(view.getId() == R.id.btnPointMove) {
            Intent intent = new Intent(this, MoveLightingPolesActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("Auth", auth);
            intent.putExtra("enteredUsername", enteredUsername);
            startActivity(intent);
        }
        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null) {
                    pd.show();
                    picid = mRootRef.push().getKey();;

                    StorageReference childRef = storageRef.child("image"+"_"+picid+".jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(EditLightingPolesActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            pd.dismiss();
                            Toast.makeText(EditLightingPolesActivity.this, "Upload Failed ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(EditLightingPolesActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(view.getId() == R.id.btnSave
                && !etType.getText().toString().equals("")
                && !etNumber.getText().toString().equals("")
                && !etHeight.getText().toString().equals("")
                && !etModel.getText().toString().equals("")
                && !etMaterial.getText().toString().equals("")
                && !etArmType.getText().toString().equals("")
                && !etSwitchBoard.getText().toString().equals("")
                && !etColor.getText().toString().equals("")
                && !etEndLine.getText().toString().equals("")
                && !etBase.getText().toString().equals("")
                && !etGModel.getText().toString().equals("")
                && !etGPower .getText().toString().equals("")
                && !etGNumber.getText().toString().equals("")
                && !etFlags.getText().toString().equals("")) {

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LightingPoles lp;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    lp = child.getValue(LightingPoles.class);
                    if(lp.getX()== lat && lp.getY()== lng) {

                        child.child("type").getRef().setValue(etType.getText().toString());
                        child.child("number").getRef().setValue(etNumber.getText().toString());
                        child.child("height").getRef().setValue(etHeight.getText().toString());
                        child.child("model").getRef().setValue(etModel.getText().toString());
                        child.child("material").getRef().setValue(etMaterial.getText().toString());
                        child.child("arm_type").getRef().setValue(etArmType.getText().toString());
                        child.child("switchboard").getRef().setValue(etSwitchBoard.getText().toString());
                        child.child("color").getRef().setValue(etColor.getText().toString());
                        child.child("notes").getRef().setValue(etNotes.getText().toString());
                        child.child("end_line").getRef().setValue(etEndLine.getText().toString());
                        child.child("base").getRef().setValue(etBase.getText().toString());
                        child.child("g_model").getRef().setValue(etGModel.getText().toString());
                        child.child("g_power").getRef().setValue(etGPower.getText().toString());
                        child.child("g_count").getRef().setValue(etGNumber.getText().toString());
                        child.child("flag_nests").getRef().setValue(etFlags.getText().toString());
                        child.child("antennas").getRef().setValue(swAntennas.isChecked());
                        child.child("cameras").getRef().setValue(swCameras.isChecked());
                        child.child("wires").getRef().setValue(swWires.isChecked());
                        child.child("sockets").getRef().setValue(swSockets.isChecked());
                        child.child("solar_flashlight").getRef().setValue(swSolarf.isChecked());


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


 /*   public void chooseImageFromGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            startActivityForResult(Intent.createChooser(intent, "העלה תמונה"), PICK_IMAGE_REQUEST);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void selectImage() {
        final CharSequence[] items = { "העלה תמונה מהמצלמה", "העלה תמונה מהגלריה",
                "בטל" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("העלה תמונה מהמצלמה")) {
                        cameraIntent();
                } else if (items[item].equals("העלה תמונה מהגלריה")) {
                        chooseImageFromGallery();
                } else if (items[item].equals("בטל")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

