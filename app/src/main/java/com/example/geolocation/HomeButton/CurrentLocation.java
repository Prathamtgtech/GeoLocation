package com.example.geolocation.HomeButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geolocation.Database.MyDatabase;
import com.example.geolocation.Database.SaveLocation;
import com.example.geolocation.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationRequest.*;

public class CurrentLocation extends AppCompatActivity {
    Button getloc,save;
    TextView lattxt, langtxt, latval, langval,addrtxt,addrval;
    FusedLocationProviderClient fusedLocationProviderClient;
    SaveLocation saveLocation;
    List<Address> currentLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        getloc = findViewById(R.id.getLoc);
        lattxt = findViewById(R.id.lattxt);
        latval = findViewById(R.id.latvalue);
        langtxt = findViewById(R.id.langtxt);
        langval = findViewById(R.id.langvalue);
        addrtxt = findViewById(R.id.addrtxt);
        addrval = findViewById(R.id.addevalue);
        save=findViewById(R.id.save);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Get Current Location
        getCurrentLocation();
        SaveData();

    }

    private void SaveData() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude=currentLocations.get(0).getLatitude();
                double longitude=currentLocations.get(0).getLongitude();
                String address=currentLocations.get(0).getAddressLine(0);
                MyDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "DATADB")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build();
                saveLocation = new SaveLocation(latitude,longitude,address);
                if (saveLocation != null) {
                    Toast.makeText(getApplicationContext(), "asdadasd Details", Toast.LENGTH_LONG).show();
                    myDatabase.dao().InsertLatLang(saveLocation);
                    Toast.makeText(getApplicationContext(), "Submit Details", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Submit Details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void getCurrentLocation() {
        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CurrentLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        (ActivityCompat.checkSelfPermission(CurrentLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Location> task) {
                            Location currentLocation=task.getResult();
                            if (currentLocation !=null){
                                try{
        Geocoder geocoder=new Geocoder(CurrentLocation.this,Locale.getDefault());
        currentLocations=geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
        latval.setText(""+currentLocations.get(0).getLatitude());
        langval.setText(""+currentLocations.get(0).getLongitude());
        addrval.setText(""+currentLocations.get(0).getAddressLine(0));
        }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                else {
                    ActivityCompat.requestPermissions(CurrentLocation.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);


                }
            }
        });
    }
}