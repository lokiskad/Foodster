package com.example.brian.foodsterredesign1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class ProfilEditFragment extends Fragment implements View.OnClickListener, ValueEventListener {

    View myView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mUserReference;

    private String uniqueID;

    private Button buttonSpeichern;

    private TextView tvSurname;
    private TextView tvName;

    private EditText etSurname;
    private EditText etName;

    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profil_edit, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uniqueID = user.getUid();

        buttonSpeichern = (Button) myView.findViewById(R.id.btpe_Save);
        buttonSpeichern.setOnClickListener(this);

        tvSurname = (TextView) myView.findViewById(R.id.tvpe_Surname);
        tvName = (TextView) myView.findViewById(R.id.tvpe_Name);

        etSurname = (EditText) myView.findViewById(R.id.etpe_Surname);
        etName = (EditText) myView.findViewById(R.id.etpe_Name);

        mUserReference = FirebaseDatabase.getInstance().getReference().child("users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mUserReference.addValueEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void writeNewUser(String uniqueID, String name, String surname)
    {
        User user = new User(uniqueID, name, surname);
        mUserReference.child(uniqueID).setValue(user);
        tvName.setText(user.getName());
        tvSurname.setText(user.getSurname());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btpe_Save:
                String name = etName.getText().toString();
                String surname = etSurname.getText().toString();

                writeNewUser(uniqueID, name, surname);
                break;
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.child(uniqueID).getValue(User.class);
        if(dataSnapshot.child(uniqueID).exists())
        {
            tvName.setText(user.getName());
            tvSurname.setText(user.getSurname());
            Log.d(TAG, "Value is from onDataChange: " + user.getName() + ", " + user.getSurname());
        }
        else
        {
            tvName.setText("");
            tvSurname.setText("");
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}