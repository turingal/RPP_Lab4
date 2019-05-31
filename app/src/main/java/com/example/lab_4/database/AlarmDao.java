package com.example.lab_4.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {


    @Query("SELECT * FROM alarm WHERE id_widget = :id")
    Alarm getByIdWidget(long id);

    @Query("SELECT * FROM alarm")
    List<Alarm> getAll();

    @Query("DELETE FROM alarm")
    void deleteAll();

    @Insert
    void insert(Alarm schedule);

    @Update
    void update(Alarm schedule);

    @Delete
    void delete(Alarm schedule);
}