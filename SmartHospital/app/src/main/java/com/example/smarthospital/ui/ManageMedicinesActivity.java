package com.example.smarthospital.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.adapter.MedicineAdapter;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Medicine;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ManageMedicinesActivity extends AppCompatActivity {
    private TextInputEditText etName, etQuantity, etExpiryDate, etPrice;
    private MaterialButton btnAddMedicine;
    private RecyclerView rvMedicines;
    private AppDatabase db;
    private MedicineAdapter adapter;
    private Medicine editingMedicine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medicines);

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etPrice = findViewById(R.id.etPrice);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        rvMedicines = findViewById(R.id.rvMedicines);
        db = AppDatabase.getInstance(this);

        rvMedicines.setLayoutManager(new LinearLayoutManager(this));

        loadMedicinesFromDatabase();

        btnAddMedicine.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String expiryDate = etExpiryDate.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty() || quantityStr.isEmpty() || expiryDate.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                Toast.makeText(this, "Ngày hết hạn phải có định dạng YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);

                if (quantity <= 0 || price <= 0) {
                    Toast.makeText(this, "Số lượng và giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (editingMedicine != null) {
                    editingMedicine.setName(name);
                    editingMedicine.setQuantity(quantity);
                    editingMedicine.setExpiryDate(expiryDate);
                    editingMedicine.setPrice(price);

                    new Thread(() -> {
                        try {
                            db.medicineDao().update(editingMedicine);
                            List<Medicine> updatedList = db.medicineDao().getAllMedicines();
                            runOnUiThread(() -> {
                                adapter.updateMedicines(updatedList);
                                Toast.makeText(this, "Đã cập nhật thuốc", Toast.LENGTH_SHORT).show();
                                resetForm();
                            });
                        } catch (Exception e) {
                            runOnUiThread(() ->
                                    Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );
                        }
                    }).start();
                } else {
                    Medicine newMedicine = new Medicine(name, quantity, expiryDate, price);

                    new Thread(() -> {
                        try {
                            db.medicineDao().insert(newMedicine);
                            List<Medicine> updatedList = db.medicineDao().getAllMedicines();
                            runOnUiThread(() -> {
                                adapter.updateMedicines(updatedList);
                                Toast.makeText(this, "Đã thêm thuốc", Toast.LENGTH_SHORT).show();
                                resetForm();
                            });
                        } catch (Exception e) {
                            runOnUiThread(() ->
                                    Toast.makeText(this, "Lỗi khi thêm thuốc: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );
                        }
                    }).start();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng hoặc giá không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMedicinesFromDatabase() {
        new Thread(() -> {
            List<Medicine> medicines = db.medicineDao().getAllMedicines();
            runOnUiThread(() -> {
                adapter = new MedicineAdapter(medicines,
                        medicine -> new Thread(() -> {
                            db.medicineDao().delete(medicine);
                            List<Medicine> updatedList = db.medicineDao().getAllMedicines();
                            runOnUiThread(() -> {
                                adapter.updateMedicines(updatedList);
                                Toast.makeText(this, "Đã xóa thuốc", Toast.LENGTH_SHORT).show();
                            });
                        }).start(),
                        medicine -> {
                            editingMedicine = medicine;
                            etName.setText(medicine.getName());
                            etQuantity.setText(String.valueOf(medicine.getQuantity()));
                            etExpiryDate.setText(medicine.getExpiryDate());
                            etPrice.setText(String.valueOf(medicine.getPrice()));
                            btnAddMedicine.setText("Cập nhật");
                        });
                rvMedicines.setAdapter(adapter);
            });
        }).start();
    }

    private void resetForm() {
        etName.setText("");
        etQuantity.setText("");
        etExpiryDate.setText("");
        etPrice.setText("");
        btnAddMedicine.setText("Thêm Thuốc");
        editingMedicine = null;
    }
}
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_medicines);
//
//        etName = findViewById(R.id.etName);
//        etQuantity = findViewById(R.id.etQuantity);
//        etExpiryDate = findViewById(R.id.etExpiryDate);
//        etPrice = findViewById(R.id.etPrice);
//        btnAddMedicine = findViewById(R.id.btnAddMedicine);
//        rvMedicines = findViewById(R.id.rvMedicines);
//        db = AppDatabase.getInstance(this);
//
//        rvMedicines.setLayoutManager(new LinearLayoutManager(this));
//        new Thread(() -> {
//            List<Medicine> medicines = db.medicineDao().getAllMedicines();
//            runOnUiThread(() -> {
//                adapter = new MedicineAdapter(medicines,
//                        medicine -> new Thread(() -> {
//                            db.medicineDao().delete(medicine);
//                            runOnUiThread(() -> {
//                                adapter.updateMedicines(db.medicineDao().getAllMedicines());
//                                Toast.makeText(this, "Đã xóa thuốc", Toast.LENGTH_SHORT).show();
//                            });
//                        }).start(),
//                        medicine -> {
//                            editingMedicine = medicine;
//                            etName.setText(medicine.getName());
//                            etQuantity.setText(String.valueOf(medicine.getQuantity()));
//                            etExpiryDate.setText(medicine.getExpiryDate());
//                            etPrice.setText(String.valueOf(medicine.getPrice()));
//                            btnAddMedicine.setText("Cập nhật");
//                        });
//                rvMedicines.setAdapter(adapter);
//            });
//        }).start();
//
//        btnAddMedicine.setOnClickListener(v -> {
//            String name = etName.getText().toString().trim();
//            String quantityStr = etQuantity.getText().toString().trim();
//            String expiryDate = etExpiryDate.getText().toString().trim();
//            String priceStr = etPrice.getText().toString().trim();
//
//            if (name.isEmpty() || quantityStr.isEmpty() || expiryDate.isEmpty() || priceStr.isEmpty()) {
//                Toast.makeText(this, "Vui lòng điền đầy đủ các trường", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
//                Toast.makeText(this, "Ngày hết hạn phải có định dạng YYYY-MM-DD", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            try {
//                int quantity = Integer.parseInt(quantityStr);
//                double price = Double.parseDouble(priceStr);
//
//                if (quantity <= 0 || price <= 0) {
//                    Toast.makeText(this, "Số lượng và giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (editingMedicine != null) {
//                    // Cập nhật thuốc
//                    editingMedicine.setName(name);
//                    editingMedicine.setQuantity(quantity);
//                    editingMedicine.setExpiryDate(expiryDate);
//                    editingMedicine.setPrice(price);
//                    new Thread(() -> {
//                        db.medicineDao().update(editingMedicine);
//                        runOnUiThread(() -> {
//                            adapter.updateMedicines(db.medicineDao().getAllMedicines());
//                            Toast.makeText(this, "Đã cập nhật thuốc", Toast.LENGTH_SHORT).show();
//                            resetForm();
//                        });
//                    }).start();
//                } else {
//                    // Thêm thuốc mới
//                    Medicine medicine = new Medicine(name, quantity, expiryDate, price);
//                    new Thread(() -> {
//                        db.medicineDao().insert(medicine);
//                        runOnUiThread(() -> {
//                            adapter.updateMedicines(db.medicineDao().getAllMedicines());
//                            Toast.makeText(this, "Đã thêm thuốc", Toast.LENGTH_SHORT).show();
//                            resetForm();
//                        });
//                    }).start();
//                }
//            } catch (NumberFormatException e) {
//                Toast.makeText(this, "Số lượng hoặc giá không hợp lệ", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void resetForm() {
//        etName.setText("");
//        etQuantity.setText("");
//        etExpiryDate.setText("");
//        etPrice.setText("");
//        btnAddMedicine.setText("Thêm Thuốc");
//        editingMedicine = null;
//    }
//}