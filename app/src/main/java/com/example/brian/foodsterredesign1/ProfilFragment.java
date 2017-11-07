package com.example.brian.foodsterredesign1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

public class ProfilFragment extends Fragment implements View.OnClickListener {

    View myView;
    private Button buttonSpeichern;
    private Button buttonLogout;
    private EditText etName;
    private EditText etGeb;
    private TextView textName;
    private TextView textGeb;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profil_layout, container, false);

        buttonSpeichern = (Button) myView.findViewById(R.id.buttonSpeichern);
        buttonLogout = (Button) myView.findViewById(R.id.editProfil_buttonLogout);

        etName = (EditText) myView.findViewById(R.id.profilLayout_etName);
        etGeb = (EditText) myView.findViewById(R.id.etGeb);
        textName = (TextView) myView.findViewById(R.id.textName);
        textGeb = (TextView) myView.findViewById(R.id.textGeb);

        buttonSpeichern.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("profile");

        mFirebaseInstance.getReference("name_title").setValue("Mein Name");

        // app_title change listener
        mFirebaseInstance.getReference("name_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Name aktualisiert");

                String appTitle = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read meinen Namen.", error.toException());
            }
        });

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

        //Prüfen, ob der mListener auch funktionieren wird
        mAuth.addAuthStateListener(mAuthListener);
        try {
            mListener = (OnFragmentInteractionListener) myView.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(myView.getContext().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    //mListener nullen beim Stoppen
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            mListener = null;
        }
    }

    @Override
    public void onClick(View v) {
     
    }

    private void createUser(String name, String geb) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(name, geb);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.geb);

                // Display newly updated name and email
                //txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                textName.setText(user.name);
                textGeb.setText(user.geb);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String name, String geb) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);

        if (!TextUtils.isEmpty(geb))
            mFirebaseDatabase.child(userId).child("geb").setValue(geb);
    }
}