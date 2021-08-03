package com.example.geolocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.geolocation.HomeButton.CurrentLocation;

public class Home extends AppCompatActivity {
Button gpsloc,gmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gpsloc=findViewById(R.id.getLocation);
        gmap=findViewById(R.id.gmap);

        //Open new activity To get Current Location
        gpsloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this, CurrentLocation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Open Google Map Fragment
        gmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}