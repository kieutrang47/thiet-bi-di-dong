package com.example.smarthospital.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "appointments",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "patientId"),
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "doctorId")
        },
        indices = {@Index("patientId"), @Index("doctorId")}
)
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int patientId;
    public int doctorId;
    public String date;
    public String time;
    public String reason;
    public String status; // pending, accepted, rejected
    public String symptoms; // Triệu chứng
    public String diagnosis; // Chẩn đoán

    public Appointment() {}

    @Ignore
    public Appointment(int patientId, int doctorId, String date, String time, String reason, String status) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
        this.symptoms = null;
        this.diagnosis = null;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
}
