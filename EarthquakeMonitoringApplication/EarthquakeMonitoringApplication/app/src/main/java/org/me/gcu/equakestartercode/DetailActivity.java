//Author: Okemdi Udeh
//Student ID: S1903344

package org.me.gcu.equakestartercode;

//Import the necessary libraries
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//Definition of DetailActivity class
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    Item item;
    TextView date, location, magnitude, depth, longitude, lat, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Add a back icon in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get the item from the adapter
        item = getIntent().getParcelableExtra("item");

        //Locate the widgets in the activity and initialize them
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        magnitude = findViewById(R.id.magnitude);
        depth = findViewById(R.id.depth);
        lat = findViewById(R.id.lat);
        longitude = findViewById(R.id.longitude);
        link = findViewById(R.id.link);

        //Set text values for the different textViews
        date.setText(item.getPubDate());
        date.setTypeface(date.getTypeface(), Typeface.BOLD);
        location.setText(item.getLocation());
        magnitude.setText("Magnitude: " + item.getMagnitude());

        //Change the color of the location according to the magnitude of the quake
        double strength = item.getMagnitude();
        if (strength <= 0.9 && strength > 0.0) {
            location.setTextColor(Color.parseColor("#F5666D"));
        } else if (strength <= 1.9 && strength >= 1.0) {
            location.setTextColor(Color.parseColor("#C90F1A"));
        } else if (strength <= 2.9 && strength >= 2.0) {
            location.setTextColor(Color.parseColor("#880B11"));
        } else if (strength >= 3.0) {
            location.setTextColor(Color.parseColor("#60070D"));
        } else {
            location.setTextColor(Color.parseColor("#F5666D"));
        }

        depth.setText("Depth: " + item.getDepth() + " km");
        lat.setText("Latitude: " + item.getLatitude());
        longitude.setText("Longitude: " + item.getLongitude());
        link.setText("Link: " + item.getLink());

        //Set the google map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    //Activate google-map functionality
    public void onMapReady(GoogleMap googleMap) {
        //Create objects for the latitude and longitude and store the values from the Item object
        LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
        //Add google-map marker
        googleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(item.getLocation())
                        .icon(getMarkerIcon(getPinColor(item.getMagnitude()))));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    //Method for adding changing the color of the pin according to magnitude
    public int getPinColor(double magnitude) {
        if (magnitude >= 3.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.red_900);
        } else if (magnitude <= 2.9 && magnitude >= 2.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.red_600);
        } else if (magnitude <= 1.9 && magnitude >= 1.0) {
            return ContextCompat.getColor(DetailActivity.this, R.color.red_400);
        } else {
            return ContextCompat.getColor(DetailActivity.this, R.color.red_200);
        }
    }

    public BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
