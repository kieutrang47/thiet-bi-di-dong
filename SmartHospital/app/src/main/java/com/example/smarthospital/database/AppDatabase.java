package com.example.smarthospital.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.Medicine;
import com.example.smarthospital.model.Prescription;
import com.example.smarthospital.model.User;

@Database(entities = {User.class, Appointment.class, Prescription.class, Medicine.class, com.example.smarthospital.model.Room.class}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AppointmentDao appointmentDao();
    public abstract PrescriptionDao prescriptionDao();
    public abstract MedicineDao medicineDao();
    public abstract RoomDao roomDao();

    private static volatile AppDatabase INSTANCE;

    // Các migration cũ (1 đến 11) giữ nguyên, chỉ thêm MIGRATION_11_12
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS users");
            database.execSQL("CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "fullName TEXT, " +
                    "role TEXT NOT NULL, " +
                    "specialty TEXT)");
            database.execSQL("CREATE UNIQUE INDEX index_users_email ON users(email)");

            database.execSQL("DROP TABLE IF EXISTS appointments");
            database.execSQL("CREATE TABLE appointments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "patientId INTEGER NOT NULL, " +
                    "doctorId INTEGER NOT NULL, " +
                    "date TEXT, " +
                    "time TEXT, " +
                    "reason TEXT, " +
                    "status TEXT, " +
                    "FOREIGN KEY(patientId) REFERENCES users(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(doctorId) REFERENCES users(id) ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX index_appointments_patientId ON appointments(patientId)");
            database.execSQL("CREATE INDEX index_appointments_doctorId ON appointments(doctorId)");

            database.execSQL("DROP TABLE IF EXISTS medicines");
            database.execSQL("CREATE TABLE medicines (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "expiryDate TEXT, " +
                    "price REAL NOT NULL)");

            database.execSQL("DROP TABLE IF EXISTS prescriptions");
            database.execSQL("CREATE TABLE prescriptions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "appointmentId INTEGER NOT NULL, " +
                    "medicineId INTEGER NOT NULL, " +
                    "dosage TEXT, " +
                    "instructions TEXT, " +
                    "dateIssued TEXT, " +
                    "FOREIGN KEY(appointmentId) REFERENCES appointments(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(medicineId) REFERENCES medicines(id) ON DELETE CASCADE)");
            database.execSQL("CREATE INDEX index_prescriptions_appointmentId ON prescriptions(appointmentId)");
            database.execSQL("CREATE INDEX index_prescriptions_medicineId ON prescriptions(medicineId)");

            database.execSQL("DROP TABLE IF EXISTS rooms");
            database.execSQL("CREATE TABLE rooms (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT, " +
                    "type TEXT, " +
                    "status TEXT)");
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE appointments ADD COLUMN symptoms TEXT");
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE appointments ADD COLUMN diagnosis TEXT");
        }
    };

    private static class DatabaseCallback extends RoomDatabase.Callback {
        private final Context context;

        DatabaseCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(() -> {
                AppDatabase database = AppDatabase.getInstance(context);
                Log.d("AppDatabase", "Users: " + database.userDao().getAllUsers().toString());
                Log.d("AppDatabase", "Appointments: " + database.appointmentDao().getAllAppointments().toString());
            }).start();
        }
    }

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "smart_hospital_db")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
                                    MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9,
                                    MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new Thread(() -> {
                                        AppDatabase database = AppDatabase.getInstance(context);
                                        UserDao userDao = database.userDao();
                                        MedicineDao medicineDao = database.medicineDao();
                                        userDao.insert(new User("admin@example.com", "123", "Admin", "admin", null));
                                        userDao.insert(new User("doctor1@example.com", "123", "Doctor One", "doctor", "Tim mạch"));
                                        userDao.insert(new User("patient1@example.com", "123", "Patient One", "patient", null));
                                        medicineDao.insert(new Medicine("Paracetamol", 100, "2025-12-31", 5000.0));
                                        medicineDao.insert(new Medicine("Amoxicillin", 50, "2025-06-30", 10000.0));
                                    }).start();
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
