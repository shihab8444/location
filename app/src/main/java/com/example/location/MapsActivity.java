package com.example.location;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.location.databinding.ActivityMapsBinding;

import androidx.annotation.NonNull;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import com.example.location.R;
import com.example.location.data.Out;
import com.example.location.data.Point;
import com.example.location.data.Sender;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Sender.NotifyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    public static Out out;

    MapView mapView;

    private static final String TAG = "MapsActivity";

    Switch switch_updates;

    TextView title;

    Button switch_cliecker;

    String myid;

    FirebaseDatabase database;

    Marker userLocationMarker;

    boolean locats = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        database = FirebaseDatabase.getInstance();

        mapView = findViewById(R.id.mapView);

        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);

        title = findViewById(R.id.title);

        switch_updates = findViewById(R.id.switch_updates);
        switch_cliecker = findViewById(R.id.switch_cliecker);

        title.setText(out.getEmail());

        DatabaseReference myRef = database.getReference("trackers")
                .child(myid).child("outs").child(out.getId()).child("location_updates");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean aBoolean = dataSnapshot.getValue(Boolean.class);
                assert aBoolean != null;
                locats = aBoolean;
                out.setLocation_updates(aBoolean);
                switch_updates.setChecked(out.isLocation_updates());
                mMap.clear();
                if (aBoolean){
                    setUserLocationMarker();
                }else if (mapView != null){
                    addPointsOnMap();
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

        switch_cliecker.setOnClickListener(v -> {
            if (out.isLocation_updates()){
                Sender.sendNotifications(out.getToken(),"0",this);
            }else {
                Sender.sendNotifications(out.getToken(),"1",this);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addPointsOnMap();


    }
    void addPointsOnMap(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.2452266, 30.7113809), 8f));
        DatabaseReference myRef = database.getReference("trackers")
                .child(myid).child("outs").child(out.getId()).child("locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (locats)return;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Point point = ds.getValue(Point.class);

                    assert point != null;
                    LatLng sydney = new LatLng(point.getLat(), point.getLog());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(point.getTime()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());

            }
        });

    }

    private void setUserLocationMarker() {
        userLocationMarker = null;

        DatabaseReference myRef = database.getReference("trackers")
                .child(myid).child("outs").child(out.getId()).child("lastLocation");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Point point = dataSnapshot.getValue(Point.class);
                assert point != null;
                LatLng latLng = new LatLng(point.getLat(), point.getLog());
                if (userLocationMarker == null) {
                    //Create a new marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                    markerOptions.rotation(point.getBearing());
                    markerOptions.anchor((float) 0.5, (float) 0.5);
                    userLocationMarker = mMap.addMarker(markerOptions);
                } else  {
                    //use the previously created marker
                    userLocationMarker.setPosition(latLng);
                    userLocationMarker.setRotation(point.getBearing());
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailed() {

    }




}

