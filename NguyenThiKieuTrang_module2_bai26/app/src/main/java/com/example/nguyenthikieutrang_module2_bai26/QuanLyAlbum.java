package com.example.nguyenthikieutrang_module2_bai26;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class QuanLyAlbum extends AppCompatActivity {
    ListView lvAlbums;
    ArrayList<Album> albumList;
    ArrayAdapter<String> albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_album);

        lvAlbums = findViewById(R.id.lvAlbum);

        // Nhận danh sách album từ Intent
        albumList = getIntent().getParcelableArrayListExtra("albums");

        // Chuyển danh sách album thành danh sách chuỗi để hiển thị
        ArrayList<String> albumNames = new ArrayList<>();
        if (albumList != null) {
            for (Album album : albumList) {
                albumNames.add(album.getMa() + " - " + album.getTen());
            }
        }

        // Adapter hiển thị dữ liệu lên ListView
        albumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, albumNames);
        lvAlbums.setAdapter(albumAdapter);


    }
}