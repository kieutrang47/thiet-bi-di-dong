package com.example.smarthospital.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Medicine;
import com.example.smarthospital.model.Room;
import com.example.smarthospital.model.User;

public class RoleSelectionActivity extends AppCompatActivity {
    private AppDatabase db;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final String PREFS_NAME = "SmartHospitalPrefs";
    private static final String KEY_DATA_INITIALIZED = "data_initialized";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        // Thiết lập sự kiện cho các nút trước để giao diện phản hồi nhanh
        Button btnPatient = findViewById(R.id.btnPatient);
        Button btnDoctor = findViewById(R.id.btnDoctor);
        Button btnAdmin = findViewById(R.id.btnAdmin);

        btnPatient.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
            intent.putExtra("role", "patient");
            startActivity(intent);
        });

        btnDoctor.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
            intent.putExtra("role", "doctor");
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
            intent.putExtra("role", "admin");
            startActivity(intent);
        });

        // Kiểm tra xem dữ liệu mẫu đã được chèn chưa
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDataInitialized = prefs.getBoolean(KEY_DATA_INITIALIZED, false);

        if (!isDataInitialized) {
            // Khởi tạo cơ sở dữ liệu và dữ liệu mẫu trong background thread
            new InitializeDatabaseTask().execute();
        } else {
            // Nếu dữ liệu đã được chèn, chỉ khởi tạo cơ sở dữ liệu
            new InitializeDatabaseOnlyTask().execute();
        }
    }

    // AsyncTask chỉ khởi tạo cơ sở dữ liệu (không chèn dữ liệu mẫu)
    private class InitializeDatabaseOnlyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db = AppDatabase.getInstance(RoleSelectionActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(RoleSelectionActivity.this, "Database initialized", Toast.LENGTH_SHORT).show();
        }
    }

    // AsyncTask để khởi tạo cơ sở dữ liệu và chèn dữ liệu mẫu
    private class InitializeDatabaseTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Khởi tạo cơ sở dữ liệu
                db = AppDatabase.getInstance(RoleSelectionActivity.this);

                // Sử dụng transaction để chèn dữ liệu nhanh hơn
                db.runInTransaction(() -> {
                    // Thêm bác sĩ mẫu (giảm từ 10 xuống 2)
                    if (db.userDao().getDoctors().isEmpty()) {
                        db.userDao().insert(new User("doctor1@gmail.com", "123", "Dr. John", "doctor", "Tim mạch"));
                        db.userDao().insert(new User("doctor2@gmail.com", "123", "Dr. Jane", "doctor", "Thần kinh"));
                    }

                    // Thêm admin mẫu (giữ nguyên)
                    if (db.userDao().getUsersByRole("admin").isEmpty()) {
                        db.userDao().insert(new User("admin@example.com", "123", "Admin", "admin", null));
                    }

                    // Thêm bệnh nhân mẫu (giảm từ 3 xuống 1)
                    if (db.userDao().getUsersByRole("patient").isEmpty()) {
                        db.userDao().insert(new User("patient1@example.com", "123", "John Doe", "patient", null));
                    }

                    // Thêm thuốc mẫu (giảm từ 10 xuống 2)
                    if (db.medicineDao().getAllMedicines().isEmpty()) {
                        db.medicineDao().insert(new Medicine("Paracetamol (giảm đau, hạ sốt)", 100, "2026-12-31", 0.5));
                        db.medicineDao().insert(new Medicine("Amoxicillin (kháng sinh)", 50, "2025-12-31", 1.0));
                    }

                    // Thêm phòng mẫu (giảm từ 9 xuống 2)
                    if (db.roomDao().getAllRooms().isEmpty()) {
                        db.roomDao().insert(new Room("Phòng 101", "Khám bệnh", "Còn trống"));
                        db.roomDao().insert(new Room("Phòng 201", "Nội trú", "Còn trống"));
                    }
                });

                // Kiểm tra số lượng bản ghi sau khi chèn
                int doctorCount = db.userDao().getDoctors().size();
                int adminCount = db.userDao().getUsersByRole("admin").size();
                int patientCount = db.userDao().getUsersByRole("patient").size();
                int medicineCount = db.medicineDao().getAllMedicines().size();
                int roomCount = db.roomDao().getAllRooms().size();

                // Lưu trạng thái "đã chèn dữ liệu mẫu"
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply();

                // Trả về thông tin số lượng bản ghi
                return String.format("Sample data initialized:\nDoctors: %d\nAdmins: %d\nPatients: %d\nMedicines: %d\nRooms: %d",
                        doctorCount, adminCount, patientCount, medicineCount, roomCount);

            } catch (Exception e) {
                return "Failed to initialize sample data: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hiển thị thông báo thành công hoặc lỗi
            Toast.makeText(RoleSelectionActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
//package com.example.smarthospital.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.smarthospital.R;
//import com.example.smarthospital.database.AppDatabase;
//import com.example.smarthospital.model.Medicine;
//import com.example.smarthospital.model.Room;
//import com.example.smarthospital.model.User;
//
//public class RoleSelectionActivity extends AppCompatActivity {
//    private AppDatabase db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_role_selection);
//
//        db = AppDatabase.getInstance(this);
//
//        // Initialize sample data
//        new Thread(() -> {
//            // Add sample doctors
//            if (db.userDao().getDoctors().isEmpty()) {
//                db.userDao().insert(new User("doctor1@example.com", "123", "Dr. John", "doctor", "Cardiology"));
//                db.userDao().insert(new User("doctor2@example.com", "123", "Dr. Jane", "doctor", "Neurology"));
//            }
//            // Add sample admin
//            if (db.userDao().getAllUsers().stream().noneMatch(u -> u.getRole().equals("admin"))) {
//                db.userDao().insert(new User("admin@example.com", "123", "Admin", "admin", null));
//            }
//            // Add sample medicines
//            if (db.medicineDao().getAllMedicines().isEmpty()) {
//                db.medicineDao().insert(new Medicine("Paracetamol", 100, "2026-12-31", 0.5));
//                db.medicineDao().insert(new Medicine("Amoxicillin", 50, "2025-12-31", 1.0));
//            }
//            // Add sample rooms
//            if (db.roomDao().getAllRooms().isEmpty()) {
//                db.roomDao().insert(new Room("Room 101", "examination", "available"));
//                db.roomDao().insert(new Room("Room 201", "inpatient", "available"));
//            }
//        }).start();
//
//        Button btnPatient = findViewById(R.id.btnPatient);
//        Button btnDoctor = findViewById(R.id.btnDoctor);
//        Button btnAdmin = findViewById(R.id.btnAdmin);
//
//        btnPatient.setOnClickListener(v -> {
//            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
//            intent.putExtra("role", "patient");
//            startActivity(intent);
//        });
//
//        btnDoctor.setOnClickListener(v -> {
//            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
//            intent.putExtra("role", "doctor");
//            startActivity(intent);
//        });
//
//        btnAdmin.setOnClickListener(v -> {
//            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
//            intent.putExtra("role", "admin");
//            startActivity(intent);
//        });
//    }
//}