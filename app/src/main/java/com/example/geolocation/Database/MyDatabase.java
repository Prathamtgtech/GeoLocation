package com.example.geolocation.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = SaveLocation.class,version = 2,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public abstract Dao dao();


}
