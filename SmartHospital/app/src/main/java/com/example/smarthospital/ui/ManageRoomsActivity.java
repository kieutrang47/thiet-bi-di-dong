package com.example.smarthospital.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.adapter.RoomAdapter;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Room;

import java.util.List;

public class ManageRoomsActivity extends AppCompatActivity {
    private EditText etName;
    private Spinner spinnerType, spinnerStatus;
    private Button btnAddRoom;
    private RecyclerView rvRooms;
    private AppDatabase db;
    private RoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);

        etName = findViewById(R.id.etName);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnAddRoom = findViewById(R.id.btnAddRoom);
        rvRooms = findViewById(R.id.rvRooms);
        db = AppDatabase.getInstance(this);

        // Setup spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Khám bệnh", "Nội trú"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Còn trống", "Đang sử dụng"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        rvRooms.setLayoutManager(new LinearLayoutManager(this));

        loadRoomsFromDatabase();

        btnAddRoom.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            String status = spinnerStatus.getSelectedItem().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên phòng", Toast.LENGTH_SHORT).show();
                return;
            }

            Room room = new Room(name, type, status);

            new Thread(() -> {
                try {
                    db.roomDao().insert(room);
                    List<Room> updatedRooms = db.roomDao().getAllRooms();
                    runOnUiThread(() -> {
                        adapter.updateRooms(updatedRooms);
                        Toast.makeText(this, "Đã thêm phòng", Toast.LENGTH_SHORT).show();
                        etName.setText("");
                    });
                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Lỗi khi thêm phòng: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });
    }

    private void loadRoomsFromDatabase() {
        new Thread(() -> {
            List<Room> rooms = db.roomDao().getAllRooms();
            runOnUiThread(() -> {
                adapter = new RoomAdapter(rooms);
                rvRooms.setAdapter(adapter);
            });
        }).start();
    }
}
