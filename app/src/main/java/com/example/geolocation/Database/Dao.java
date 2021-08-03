package com.example.geolocation.Database;

import android.location.Address;

import androidx.room.Insert;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert
    void InsertLatLang(SaveLocation saveLocation);
}
