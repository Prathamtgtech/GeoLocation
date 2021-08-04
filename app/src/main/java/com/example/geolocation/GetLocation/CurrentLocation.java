package com.example.geolocation.GetLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geolocation.Database.MyDatabase;
import com.example.geolocation.Database.SaveLocation;
import com.example.geolocation.R;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CurrentLocation extends AppCompatActivity {
    Button getloc,save;
    TextView lattxt, langtxt, latval, langval,addrtxt,addrval,imguri;
    FusedLocationProviderClient fusedLocationProviderClient;
    SaveLocation saveLocation;
    List<Address> currentLocations;
    Bitmap bitmap;
    Uri imageUri;
    private int CODE = 100;
    String currentImagePath=null;
    ImageView imageView;
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
        imageView=findViewById(R.id.ImageView);
        save=findViewById(R.id.save);
        imguri=findViewById(R.id.imgUri);

        //fused location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Get Current Location
        getCurrentLocation();
        SaveData();
        cameraClick();


    }


//Save image In Gallery

    //Save Image in Gallery

        public File getImageFile() throws IOException {
            String timeStamp=new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
            String imageName=timeStamp;
            File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile=File.createTempFile(imageName,".jpg",storageDir);
            currentImagePath=imageFile.getAbsolutePath();
            imguri.setText(currentImagePath);
            return imageFile;
        }

    //camer Intent
    private void cameraClick() {
      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (ActivityCompat.checkSelfPermission(CurrentLocation.this,Manifest.permission.CAMERA)
              == PackageManager.PERMISSION_GRANTED){
                  Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  if (cameraIntent.resolveActivity(getPackageManager())!=null){
                      File imageFile=null;

                      try {
                          imageFile=getImageFile();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                      if (imageFile!=null){
                          imageUri=FileProvider.getUriForFile(CurrentLocation.this,
                                  "com.example.geolocation"
                          ,imageFile);
                          cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                          startActivityForResult(cameraIntent,100);
                      }
                  }
              }else {
                  ActivityCompat.requestPermissions(CurrentLocation.this,
                          new String[]{Manifest.permission.CAMERA},100);
              }
          }
      });
    }
//onActivity Bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap= BitmapFactory.decodeFile(currentImagePath);
        imageView.setImageBitmap(bitmap);
    }
//save data
    private void SaveData() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           SaveLocationDetails();
            }
        });
    }



//save location Details
    public void SaveLocationDetails(){
        double latitude=currentLocations.get(0).getLatitude();
        double longitude=currentLocations.get(0).getLongitude();
        String address=currentLocations.get(0).getAddressLine(0);
        String imagePath=currentImagePath;
        MyDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "DATADB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        saveLocation = new SaveLocation(latitude,longitude,address,imagePath);
        if (saveLocation != null) {
            myDatabase.dao().InsertLatLang(saveLocation);
            Toast.makeText(getApplicationContext(), "Submit Details", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not Submit Details", Toast.LENGTH_LONG).show();
        }
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