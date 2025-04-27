package com.example.smarthospital.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.smarthospital.model.MedicalRecord;

@Dao
public interface MedicalRecordDao {
    @Insert
    void insert(MedicalRecord record);

    @Query("SELECT * FROM medical_records WHERE appointmentId = :appointmentId")
    MedicalRecord getRecordByAppointment(int appointmentId);
}