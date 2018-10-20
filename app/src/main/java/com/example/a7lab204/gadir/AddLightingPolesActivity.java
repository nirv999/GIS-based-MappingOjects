package com.example.a7lab204.gadir;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddLightingPolesActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSave, btnUpload;
    DatabaseReference mRootRef;
    public static final String DB_NAME = "LightingPoles";
    double lat, lng;
    String enteredUsername, auth;
    String addressLine;
    String picid ="";


    EditText etType, etNumber ,etHeight, etModel, etMaterial, etArmType, etSwitchBoard, etColor,
            etEndLine, etBase,etGModel ,etGPower, etGNumber, etFlags, etNotes;
    Switch swAntennas, swCameras, swWires, swSockets, swSolarf;


    Button btnChooseImg, btnUploadImg;
    ImageView imgView;
    int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;
    ProgressDialog pd;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://gadir-ns.appspot.com/"); // copy from firebase this line



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lighting_poles);
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
                            Toast.makeText(AddLightingPolesActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddLightingPolesActivity.this, "Upload Failed ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(AddLightingPolesActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        //etGadirNum , etIecNum, etContractNum, etCirclesNum, etControllerNum, etConnectionSize//

        /*if(view.getId() == R.id.btnUpload)
        {
            selectImage();
        }*/
        if(view.getId() == R.id.btnSave && !etType.getText().toString().equals("")
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
                && !etFlags.getText().toString().equals("")
                && !picid.equals(""))
        {
            Time Today = new Time(Time.getCurrentTimezone());
            Today.setToNow();

            LightingPoles lp=new LightingPoles (swAntennas.isChecked(),
                    swCameras.isChecked(),
                    swWires.isChecked(),
                    swSockets.isChecked(),
                    swSolarf.isChecked(),
                    lat,lng,
                    etType.getText().toString(),
                    etNumber.getText().toString(),
                    etHeight.getText().toString(),
                    etModel.getText().toString(),
                    etMaterial.getText().toString(),
                    etArmType.getText().toString(),
                    etNotes.getText().toString(),
                    etSwitchBoard.getText().toString(),
                    etColor.getText().toString(),
                    etEndLine.getText().toString(),
                    etBase.getText().toString(),
                    etGModel.getText().toString(),
                    etGNumber.getText().toString(),
                    etGPower.getText().toString(),
                    etFlags.getText().toString(),
                    addressLine,picid );
            String id=picid;  // here we saving the same key for the pic and the obj
            mRootRef.child(id).setValue(lp);
            Toast.makeText(this, filePath.toString() ,Toast.LENGTH_SHORT).show();
            //uploadImage(id);
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

/*    public void uploadImage(String idLP)
    {
        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("מעלה תמונה...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+idLP);
            ref.putFile(filePath)
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddLightingPolesActivity.this,"ישנה בעיה עם העלאת התמונה", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(AddLightingPolesActivity.this,"התמונה הועלתה בהצלחה", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setTitle((int)progress +"%");
                }
            });

        }

    }*/

/*
    public void chooseImageFromGallery()
    {
        Intent intent = new Intent();
        intent.setType("image*/
/*");
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
    }
*/

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
