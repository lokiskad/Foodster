package com.example.brian.foodsterredesign1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * Created by Brian on 23.04.2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    View myView;
    private Button buttonLogin;
    private EditText editEmail;
    private EditText editPassword;
    private TextView textViewSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private ProgressDialog progressDialog;
    //Der mListener dient dazu, die Fragmente zu wechseln.
    private OnFragmentInteractionListener mListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.login_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) myView.findViewById(R.id.editEmail);
        editPassword = (EditText) myView.findViewById(R.id.editPassword);
        textViewSignUp = (TextView) myView.findViewById(R.id.textViewSignUp);
        buttonLogin = (Button) myView.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);


        //Wenn der Benutzer schon eingeloggt ist, wechsel direkt zum Profil.
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
        if(mAuth.getCurrentUser() != null)
            mListener.changeFragment("profil");
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


    //Wenn der Button zum Einloggen gedrückt wird, versuche User einzuloggen.
    //Wenn das EditText Feld angeklickt wurde, wechsel das Fragment zum Registrieren.
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonLogin:
                userLogin();
                break;
            case R.id.textViewSignUp:
                //Aufruf zum Wechseln des Fragments
                mListener.changeFragment("register");
                break;
        }
    }

    private void userLogin(){
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

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

        //progressDialog.setMessage("Einloggen..");
        //progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //progressDialog.dismiss();
                if(task.isSuccessful()){
                    //wenn Erfolgreich, wechsel zu Profil
                    Toast.makeText(getContext(), "Erfolgreich eingeloggt", Toast.LENGTH_SHORT).show();
                    mListener.changeFragment("profil");
                }
            }
        });


    }
}
