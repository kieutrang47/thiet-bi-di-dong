package com.example.smarthospital.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smarthospital.model.Room;

import java.util.List;

@Dao
public interface RoomDao {
    @Insert
    void insert(com.example.smarthospital.model.Room room);

    @Update
    void update(com.example.smarthospital.model.Room room);

    @Delete
    void delete(com.example.smarthospital.model.Room room);

    @Query("SELECT * FROM rooms")
    List<com.example.smarthospital.model.Room> getAllRooms();
}