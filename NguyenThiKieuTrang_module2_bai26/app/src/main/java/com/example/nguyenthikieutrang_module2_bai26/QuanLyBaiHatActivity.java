package com.example.nguyenthikieutrang_module2_bai26;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class QuanLyBaiHatActivity extends AppCompatActivity {
    Spinner spnAlbums;
    ListView lvBaiHat;
    ArrayList<Album> albumList;
    ArrayAdapter<String> albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_bai_hat);
        lvBaiHat = findViewById(R.id.lvBaiHat);

        // Nhận danh sách album từ Intent
        albumList = getIntent().getParcelableArrayListExtra("albums");

        // Chuyển danh sách album thành danh sách chuỗi để hiển thị trong Spinner
        ArrayList<String> albumNames = new ArrayList<>();
        if (albumList != null) {
            for (Album album : albumList) {
                albumNames.add(album.getTen());
            }
        }

        // Adapter hiển thị danh sách album trong Spinner
        albumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, albumNames);
        spnAlbums.setAdapter(albumAdapter);
    }
}
