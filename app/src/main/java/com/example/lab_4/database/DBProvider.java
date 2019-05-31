package com.example.lab_4.database;


import android.app.Application;
import android.arch.persistence.room.Room;

public class DBProvider extends Application {

    public static DBProvider instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static DBProvider getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

}