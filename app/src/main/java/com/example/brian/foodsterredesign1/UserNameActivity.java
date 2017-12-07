/*package com.example.brian.foodsterredesign1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserNameActivity extends Activity{

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mUserReference = mRootReference.child("users");

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //String data = getIntent().getExtras().getString("UserID");
        //Toast.makeText(this, data,Toast.LENGTH_LONG).show();
        //getIntent().putExtra("UserID", data);

        this.getUserName(mUserReference);
        setResult(2,intent);
        finish();
    }

    public void getUserName(DatabaseReference mUserReference){
        readData(mUserReference, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String data = getIntent().getExtras().getString("UserID");
                //Toast.makeText(UserNameActivity.this, "USER ID IN ONDATACHANGE: " + data, Toast.LENGTH_LONG).show();
                //String userName;
                User user = dataSnapshot.child(data).getValue(User.class);
                //userName = user.getName();
                //Toast.makeText(UserNameActivity.this, "USERNAME IN ACTIVITY: " + user.getName(), Toast.LENGTH_LONG).show();
                intent.putExtra("UserID", data);
                intent.putExtra("UserName", user.getName());
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {
                Toast.makeText(UserNameActivity.this, "ERROR IN LISTENER", Toast.LENGTH_LONG).show();
            }
        });
    }
}*/
