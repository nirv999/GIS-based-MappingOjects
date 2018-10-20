package com.example.a7lab204.gadir;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddEditUserActivity extends Activity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnDeleteUser, btnSave;
    Switch switch1;

    DatabaseReference mRootRef;
    public static final String DB_NAME = "Users";
    String enteredUsername, auth, editUsername;

    private String addOrEdit;//ADD or EDIT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnDeleteUser = (Button)findViewById(R.id.btnDeleteUser);
        btnSave.setOnClickListener(this);
        btnDeleteUser.setOnClickListener(this);

        switch1 = (Switch)findViewById(R.id.switch1);

        mRootRef = FirebaseDatabase.getInstance().getReference(DB_NAME);

        Intent prevIntent = getIntent();
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("enteredUsername");
        editUsername = prevIntent.getStringExtra("editUsername");

        addOrEdit = "ADD";
        btnDeleteUser.setVisibility(View.GONE);
        btnDeleteUser.setClickable(false);

        //if this is not a new user, we want to edit
        if(editUsername != null)
        {
            addOrEdit = "EDIT";
            btnDeleteUser.setVisibility(View.VISIBLE);
            btnDeleteUser.setClickable(true);
            //getting info of the user

            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        user = child.getValue(User.class);
                        if(user.getUsername().equals(editUsername)) {

                            etUsername.setText(user.getUsername());
                            etPassword.setText(user.getPassword());

                            if (user.getAuth().equals("VIEWER"))
                            {
                                switch1.setClickable(true);
                                switch1.setChecked(false);
                            }
                            if(user.getAuth().equals("EDITOR"))
                            {
                                switch1.setClickable(true);
                                switch1.setChecked(true);
                            }
                            if(user.getAuth().equals("MANAGER"))
                            {
                                switch1.setClickable(false);
                                switch1.setChecked(true);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        if(v.getId() == R.id.btnSave)
        {
            if(addOrEdit.equals("ADD")) {
                String id = mRootRef.push().getKey();

                String permission = "";
                if (switch1.isChecked() == true)
                    permission = "EDITOR";
                else
                    permission = "VIEWER";

                User user = new User(etUsername.getText().toString(),
                                    etPassword.getText().toString(),
                                    permission,
                                    id);

                mRootRef.child(id).setValue(user);
                Toast.makeText(this, "נתונים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();

                intent = new Intent(this, UsersManagementActivity.class);
                intent.putExtra("Auth", auth);
                intent.putExtra("Username", enteredUsername);
                startActivity(intent);
            }
            else /*EDIT mode*/
            {
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            user = child.getValue(User.class);
                            if(user.getUsername().equals(editUsername)) {
                                child.child("password").getRef().setValue(etPassword.getText().toString());
                                child.child("username").getRef().setValue(etUsername.getText().toString());
                                if(!user.getAuth().equals("MANAGER"))
                                {
                                    if(switch1.isChecked() == true)
                                        child.child("auth").getRef().setValue("EDITOR");
                                    else
                                        child.child("auth").getRef().setValue("VIEWER");
                                }

                                Toast.makeText(getApplicationContext(), "נתונים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
                                final Intent intent1 = new Intent(getApplicationContext(), UsersManagementActivity.class);
                                intent1.putExtra("Auth", auth);
                                intent1.putExtra("Username", enteredUsername);
                                startActivity(intent1);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        if(v.getId() == R.id.btnDeleteUser)
        {
            final Context context = this;
            mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user;
                    //I needed to do this intent as an array because of the alert
                    final Intent[] intent = new Intent[1];
                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
                        user = child.getValue(User.class);
                        if(user.getUsername().equals(etUsername.getText().toString())) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setMessage("האם אתה בטוח כי ברצונך למחוק משתמש זה?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "כן",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            child.getRef().removeValue();
                                            Toast.makeText(getApplicationContext(), "נתוני המשתמש נמחקו", Toast.LENGTH_SHORT).show();
                                            intent[0] = new Intent(getApplicationContext(), UsersManagementActivity.class);
                                            intent[0].putExtra("Auth", auth);
                                            intent[0].putExtra("Username", enteredUsername);
                                            startActivity(intent[0]);
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

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(),UsersManagementActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("Username", enteredUsername);
            startActivity(intent);
            return true;
        }
        return false;
    }
}

