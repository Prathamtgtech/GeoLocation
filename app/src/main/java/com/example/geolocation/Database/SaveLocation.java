package com.example.geolocation.Database;

import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SaveLocation {
    @PrimaryKey(autoGenerate = true)
    int id;
    double latval;
    double landval;
    String addrval;
    String path;

    public SaveLocation(double latval, double landval, String addrval,String path) {
        this.latval = latval;
        this.landval = landval;
        this.addrval = addrval;
        this.path = path;
    }

    public double getLatval() {
        return latval;
    }

    public void setLatval(double latval) {
        this.latval = latval;
    }

    public double getLandval() {
        return landval;
    }

    public void setLandval(double landval) {
        this.landval = landval;
    }

    public String getAddrval() {
        return addrval;
    }

    public void setAddrval(String addrval) {
        this.addrval = addrval;
    }
}