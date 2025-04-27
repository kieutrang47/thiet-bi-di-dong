package com.example.smarthospital.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "prescriptions",
        foreignKeys = {
                @ForeignKey(entity = Appointment.class, parentColumns = "id", childColumns = "appointmentId"),
                @ForeignKey(entity = Medicine.class, parentColumns = "id", childColumns = "medicineId")
        },
        indices = {@Index("appointmentId"), @Index("medicineId")}
)
public class Prescription {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int appointmentId;
    public int medicineId;
    public String dosage;
    public String instructions;
    public String dateIssued;

    // Constructor rỗng yêu cầu bởi Room
    public Prescription() {}

    // Constructor đầy đủ, bị bỏ qua bởi Room
    @Ignore
    public Prescription(int appointmentId, int medicineId, String dosage, String instructions, String dateIssued) {
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.instructions = instructions;
        this.dateIssued = dateIssued;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getDateIssued() { return dateIssued; }
    public void setDateIssued(String dateIssued) { this.dateIssued = dateIssued; }
}