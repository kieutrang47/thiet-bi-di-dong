package com.example.smarthospital.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.smarthospital.model.Prescription;

import java.util.List;

@Dao
public interface PrescriptionDao {
    @Insert
    void insert(Prescription prescription);

    @Query("SELECT * FROM prescriptions WHERE appointmentId = :appointmentId")
    List<Prescription> getPrescriptionsByAppointment(int appointmentId);

    @Query("SELECT * FROM prescriptions WHERE appointmentId IN (:appointmentIds)")
    List<Prescription> getPrescriptionsByAppointment(List<Integer> appointmentIds);
}