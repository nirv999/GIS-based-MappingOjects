package com.example.a7lab204.gadir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersManagementActivity extends AppCompatActivity implements View.OnClickListener{

    ListView lstUsers;
    Button btnAddUser;

    private ArrayList<String> arrayList;
    private ArrayAdapter <String> arrayAdapter;

    String enteredUsername, auth;
    DatabaseReference mRootRef;
    public static final String DB_NAME = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_management);

        lstUsers = (ListView)findViewById(R.id.lstUsers);
        btnAddUser = (Button)findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(this);

        Intent prevIntent = getIntent();
        auth = prevIntent.getStringExtra("Auth");
        enteredUsername = prevIntent.getStringExtra("Username");

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lstUsers.setAdapter(arrayAdapter);

        mRootRef = FirebaseDatabase.getInstance().getReference(DB_NAME);
        mRootRef.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   User user = dataSnapshot.getValue(User.class);
                   arrayList.add(user.toString());
                   arrayAdapter.notifyDataSetChanged();
               }

               @Override
               public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

               @Override
               public void onChildRemoved(DataSnapshot dataSnapshot) {}

               @Override
               public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

               @Override
               public void onCancelled(DatabaseError databaseError) {}
           }
        );

        lstUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),AddEditUserActivity.class);
                intent.putExtra("editUsername", arrayList.get(position));
                intent.putExtra("Auth", auth);
                intent.putExtra("enteredUsername", enteredUsername);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {

        Intent intent;
        if (v.getId() == R.id.btnAddUser)
        {
            intent = new Intent(this,AddEditUserActivity.class);
            intent.putExtra("Auth", auth);
            intent.putExtra("enteredUsername", enteredUsername);
            startActivity(intent);
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
