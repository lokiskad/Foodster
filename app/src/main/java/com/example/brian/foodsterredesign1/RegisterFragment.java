package com.example.brian.foodsterredesign1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * Created by Brian on 23.04.2017.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener {

    View myView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText textEmailAddress;
    private EditText textPassword;
    private TextView textViewSignin;
    private Button buttonRegister;

    private ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.register_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        textEmailAddress = (EditText) myView.findViewById(R.id.editEmail);
        textPassword = (EditText) myView.findViewById(R.id.editPassword);
        textViewSignin = (TextView) myView.findViewById(R.id.textViewSignin);

        buttonRegister = (Button) myView.findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(getContext());

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


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
        try {
            mListener = (OnFragmentInteractionListener) myView.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(myView.getContext().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            mListener = null;
        }
    }


    public void onClick(View v) {

        switch(v.getId()){
            case R.id.buttonRegister:
                registerUser();
                break;
            case R.id.textViewSignin:
                //Aufruf zum Wechseln des Fragments
                mListener.changeFragment("login");
                break;
        }
    }

    private void registerUser(){
        String email = textEmailAddress.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(getContext(), "Bitte E-Mail Adresse eingeben", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(getContext(), "Bitte Passwort eingeben", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registriere Benutzer..");
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Benutzer ist erfolgreich eingeloggt
                            Toast.makeText(getContext(), "Erfolgreich registriert", Toast.LENGTH_SHORT).show();
                            mListener.changeFragment("profil");
                            progressDialog.dismiss();
                        }else{
                            Toast.makeText(getContext(), "Registrierung fehlgeschlagen, bitte erneut versuchen", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }



}
