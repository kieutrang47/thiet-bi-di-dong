package com.example.smarthospital.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smarthospital.model.Appointment;

import java.util.List;

@Dao
public interface AppointmentDao {
    @Insert
    long insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE patientId = :patientId")
    List<Appointment> getAppointmentsByPatient(int patientId);

    @Query("SELECT * FROM appointments WHERE doctorId = :doctorId")
    List<Appointment> getAppointmentsByDoctor(int doctorId);

    @Query("SELECT * FROM appointments WHERE id = :appointmentId")
    Appointment getAppointmentById(int appointmentId); // Thêm phương thức này

    @Query("SELECT * FROM appointments")
    List<Appointment> getAllAppointments(); // Thêm phương thức này
}