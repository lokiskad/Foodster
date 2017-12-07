package com.example.brian.foodsterredesign1;

import android.app.Activity;
import android.content.Intent;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.content.ContentValues.TAG;

/**
 * Created by Brian on 23.04.2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    View myView;
    private Button buttonLogin;
    private EditText editEmail;
    private EditText editPassword;
    private TextView textViewSignUp;
    private SignInButton btn_gSignIn;


    //Google SignIn Code
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private ProgressDialog progressDialog;
    //Der mListener dient dazu, die Fragmente zu wechseln.
    private OnFragmentInteractionListener mListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.login_layout, container, false);

        //Google SignIn Code
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        editEmail = (EditText) myView.findViewById(R.id.editEmail);
        editPassword = (EditText) myView.findViewById(R.id.editPassword);
        textViewSignUp = (TextView) myView.findViewById(R.id.textViewSignUp);
        buttonLogin = (Button) myView.findViewById(R.id.buttonLogin);
        btn_gSignIn = (SignInButton) myView.findViewById(R.id.btn_gSignIn);

        btn_gSignIn.setOnClickListener(this);
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
            case R.id.btn_gSignIn:
                gSignIn();
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

    //Google SignIn Code
    private void gSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //startActivity(new Intent(getContext(), MainActivity.class));
                            //mGoogleApiClient.stopAutoManage(getActivity());
                            //mGoogleApiClient.disconnect();
                            mListener.changeFragment("profil");
                            //getActivity().finish();
                        }
                    }
                });
    }
}
