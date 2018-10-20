package com.example.a7lab204.gadir;

import android.app.Activity;
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

public class MainActivity extends Activity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnEnter;
    DatabaseReference mRootRef;
    public static final String DB_NAME = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);

        mRootRef= FirebaseDatabase.getInstance().getReference(DB_NAME);

        btnEnter = (Button)findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(this);

        /*String id = mRootRef.push().getKey();
        User user = new User("User1", "1", "MANAGER",id);
        mRootRef.child(id).setValue(user);*/


    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnEnter) {
            if (etUsername.getText().toString().equals(""))
                Toast.makeText(this, "לא הוזן שם משתמש", Toast.LENGTH_SHORT).show();
            if (etPassword.getText().toString().equals(""))
                Toast.makeText(this, "לא הוזן סיסמא", Toast.LENGTH_SHORT).show();
            if (!etPassword.getText().toString().equals("") && !etPassword.getText().toString().equals(""))
            {
                mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user;
                        boolean entered=false;
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            user = child.getValue(User.class);
                            if(user.getUsername().equals(etUsername.getText().toString()) &&
                                    user.getPassword().equals(etPassword.getText().toString())){
                                entered =true;
                               final Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                               i.putExtra("Auth", user.getAuth());
                               i.putExtra("Username", user.getUsername());
                                startActivity(i);
                            }
                        }
                        if(entered==false)
                            Toast.makeText(getApplicationContext()," שם משתמש או סיסמא שגויים", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
