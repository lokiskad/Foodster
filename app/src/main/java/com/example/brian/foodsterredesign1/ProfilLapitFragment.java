package com.example.brian.foodsterredesign1;

import android.app.Fragment;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by Brian on 17.11.2017.
 */

public class ProfilLapitFragment extends android.support.v4.app.Fragment implements ValueEventListener {

    private CircleImageView mImage;
    private TextView txtDisplayName, txtStatus;
    private View myView;
    private Button btnImage, btnStatus;

    private String sUniqueID;

    private FirebaseDatabase mFirebaseInstance;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mUserReference = mRootReference.child("users");

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profil_layout, container, false);

        mImage = (CircleImageView) myView.findViewById(R.id.settings_image);
        txtDisplayName = (TextView) myView.findViewById(R.id.settings_displayName);
        txtStatus = (TextView) myView.findViewById(R.id.settings_status);
        btnImage = (Button) myView.findViewById(R.id.settings_btnImage);
        btnStatus = (Button) myView.findViewById(R.id.settings_btnStatus);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        sUniqueID = user.getUid();

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
        mUserReference.addValueEventListener(this);
    }

    //mListener nullen beim Stoppen
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.child(sUniqueID).getValue(User.class);
        if(dataSnapshot.child(sUniqueID).exists())
        {
            //txtDisplayName.setText("Brian Jarosz");
            //txtStatus.setText("");
        }
        else
        {
            //txtDisplayName.setText("");
            //txtStatus.setText("");
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
