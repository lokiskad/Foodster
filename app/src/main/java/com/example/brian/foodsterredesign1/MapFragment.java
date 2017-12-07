package com.example.brian.foodsterredesign1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kerem on 23.04.2017.
 */

//TODO: Bitte dokumentieren.

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GeoQueryEventListener {

    public GoogleMap mMap;
    MapView mMapView;

    private Button btnQuery;

    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private static final GeoLocation INITIAL_CENTER = new GeoLocation(51.443061, 7.262201);
    private static final String TAG = "MyActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser userr;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("location");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
    private GeoFire geoFire = new GeoFire(ref);
    private GeoQuery geoQuery;
    private double userlat;
    private double userlong;

    User user = new User();

    private Map<String,Marker> markers;
    private ArrayList<String> keyList = new ArrayList<>();
    private ArrayList<String> userList = new ArrayList<>();

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.map_layout, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userr = firebaseAuth.getCurrentUser();
        if(firebaseAuth.getCurrentUser() == null){
            getActivity().finish();
        }

        btnQuery = (Button) myView.findViewById(R.id.btnQuery);
        // setup markers
        this.markers = new HashMap<String, Marker>();

        this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 10);

        btnQuery.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) myView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "onMarkerClick Titel: " + marker.getTitle());
                return false;
                }
        });

        // CameraPosition ort = CameraPosition.builder().target(new LatLng(51.4556432, 7.0115552)).zoom(16).bearing(0).tilt(45).build();

        // googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(ort));


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            Intent intent = new Intent(getActivity(), GPSTrackerActivity.class);

            startActivityForResult(intent,1);

        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        mMap.setMyLocationEnabled(true);

                    }
                }else {
                    Toast.makeText(getActivity(), "Diese App funktioniert nicht ohne Gps", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    break;
                }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1){
            Bundle extras = data.getExtras();
            double longitude = extras.getDouble("Longitude");
            double latitude = extras.getDouble("Latitude");

            //geoFire.setLocation("Ahmed", new GeoLocation(51.454109 ,  7.267999));
            geoFire.setLocation(userr.getUid(), new GeoLocation(latitude, longitude));
            geoFire.setLocation("CkfCtzkMf9MhxnejqdXjysfEwPh1", new GeoLocation(51.443061 ,  7.262201));

            LatLng latLng = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    //.bearing(90)                // Sets the orientation of the camera to east
                    //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // add an event listener to start updating locations again
        this.geoQuery.addGeoQueryEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        this.geoQuery.removeAllListeners();
        for (Marker marker: this.markers.values()) {
            marker.remove();
        }
        this.markers.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQuery:
                FireQuery();
                break;
        }
    }

    private void FireQuery()
    {
        geoFire.getLocation(userr.getUid(), new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                userlat = location.latitude;
                userlong = location.longitude;
                Toast.makeText(getContext(), "latitude: " + userlat + " und longitude: " + userlong, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.geoQuery.setCenter(new GeoLocation(userlat, userlong));
        this.geoQuery.setRadius(100);
        this.geoQuery = this.geoFire.queryAtLocation(new GeoLocation(userlat, userlong), 100);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(userlat, userlong), 100);


    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        // Add a new marker to the map
        Toast.makeText(getContext(), "KEY: " + key, Toast.LENGTH_LONG).show();
        Marker marker = this.mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude)));

        this.markers.put(key, marker);
        //Setze key in List rein
        keyList.add(key);
    }

    @Override
    public void onKeyExited(String key) {
        // Remove any old marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            marker.remove();
            this.markers.remove(key);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
        Marker marker = this.markers.get(key);
    }

    @Override
    public void onGeoQueryReady() {
        //Setze Titel für jeden User in Liste ein
        for (int i=0; i<keyList.size(); i++)
        {
            //Log.d(TAG, "Userkey in OnGeoQueryReady: " + keyList.get(i));
            this.getUserInformation(keyList.get(i));
        }
    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void getUserInformation(String uniqueID)
    {
        userList.add(uniqueID);
        //Log.d(TAG, "Userkey in getUserInformation VOR onSucess: " + user.getUniqueID());
        readData(userRef, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(int i = 0; i<userList.size(); i++) {
                    User user1 = dataSnapshot.child(userList.get(i)).getValue(User.class);
                    markers.get(userList.get(i)).setTitle(user1.getSurname() + " " + user1.getName());
                    markers.get(userList.get(i)).setSnippet("Klicke Hier um auf das Profil von " + user1.getSurname() + " zu gelangen!");
                    //Log.d(TAG, "Userkey in ONSUCCESS in getUserInformation: " + userList.get(i));
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "ERROR IN LISTENER", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener)
    {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }
}
