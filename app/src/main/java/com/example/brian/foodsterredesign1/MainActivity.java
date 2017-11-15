package com.example.brian.foodsterredesign1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

//TODO: Überprüfen ob die Möglichkeit besteht, die SignedOn Erkennung über die MainActivity für alle Fragmente zu übernehmen

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    //Neu 16.07.2017:
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //TODO: Notwendigkeit der Toolbar überprüfen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //TODO: NavigationView Gruppenelemente sollten eingeblendet werden, abhängig davon ob der Benutzer eingeloggt ist oder nicht
        //navigationView.getMenu().setGroupVisible(R.id.group_unregistered, true)
        //navigationView.getMenu().setGroupVisible(R.id.group_registered, true)
        //true oder false sollten eingetragen sein in Abhängigkeit des LoginStatus

        //Neu 16.07.2017
        //Je nachdem, ob der User eingeloggt ist oder nicht, wird die jeweilige Gruppe visible.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    navigationView.getMenu().setGroupVisible(R.id.group_unregistered, false);
                    navigationView.getMenu().setGroupVisible(R.id.group_registered, true);
                    changeFragment("profil");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    navigationView.getMenu().setGroupVisible(R.id.group_unregistered, true);
                    navigationView.getMenu().setGroupVisible(R.id.group_registered, false);
                    changeFragment("register");
                }
                // ...
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    //Wenn ein Item in der Navigation gedrückt/ausgewählt wurde, wird dieses als ID gespeichert.
    //Über die jeweilige ID wird dann im layout der richtige Name genutzt
    //Um zum richtigen Fragment auch zu wechseln, wird das content_frame zum jeweiligen Fragment ausgetauscht

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(id)
        {
            case R.id.nav_register_layout:
                changeFragment("register");
                break;
            case R.id.nav_login_layout:
                changeFragment("login");
                break;
            case R.id.nav_map_layout:
                changeFragment("maps");
                break;
            case R.id.nav_profil_layout:
                changeFragment("profil");
                break;
            case R.id.nav_editprofil_layout:
                changeFragment("editprofil");
                break;
            case R.id.nav_logout:
                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                changeFragment("login");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    //Methode um die Fragmente innerhalb jedes Fragments zu wechseln. Jedes Fragment brauch eine eigene ID.
    //Wichtig: in jedem Fragment muss folgende Zeile hinzugefügt worden sein:
    //private OnFragmentInteractionListener mListener;
    //Und in der onStart Methode muss folgendes mit einer TryCatch Methode abgefangen werden:
    // mListener = (OnFragmentInteractionListener) myView.getContext();
    //Changelog: die ID wurde geändert zu einem String um die lesbarkeit zu verbessern.
    public void changeFragment(String id) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(id)
        {
            case "register":
                fragmentManager.beginTransaction().replace(R.id.content_frame, new RegisterFragment()).commit();
                break;
            case "login":
                fragmentManager.beginTransaction().replace(R.id.content_frame, new LoginFragment()).commit();
                break;
            case "maps":
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MapFragment()).commit();
                break;
            case "profil":
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfilFragment()).commit();
                break;
            case "editprofil":
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfilEditFragment()).commit();
        }
    }

}
