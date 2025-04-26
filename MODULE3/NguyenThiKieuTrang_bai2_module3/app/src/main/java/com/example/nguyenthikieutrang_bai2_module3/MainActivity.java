package com.example.nguyenthikieutrang_bai2_module3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnDeleteOne, btnDeleteAll;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> fileNames;
    private File[] listFiles;
    private int selectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewCache);
        btnDeleteOne = findViewById(R.id.btnDeleteOne);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);

        createSampleCacheFiles(); // Tạo file mẫu nếu cần
        loadCacheFiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("fileName", fileNames.get(position));
                startActivity(intent);
            }
        });

        btnDeleteOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem == -1) {
                    Toast.makeText(MainActivity.this, "Vui lòng chọn 1 file để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmDeleteOne(selectedItem);
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteAll();
            }
        });
    }

    private void createSampleCacheFiles() {
        File cacheDir = getCacheDir();
        File[] files = cacheDir.listFiles();
        if (files != null && files.length > 0) {
            return;
        }
        try {
            for (int i = 1; i <= 3; i++) {
                File sampleFile = new File(cacheDir, "sample_file_" + i + ".txt");
                if (!sampleFile.exists()) {
                    sampleFile.createNewFile();
                    FileWriter writer = new FileWriter(sampleFile);
                    writer.write("Đây là nội dung của file mẫu số " + i);
                    writer.close();
                }
            }
            Toast.makeText(this, "Đã tạo file mẫu", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCacheFiles() {
        File cacheDir = getCacheDir();
        listFiles = cacheDir.listFiles();
        fileNames = new ArrayList<>();
        if (listFiles != null) {
            for (File file : listFiles) {
                fileNames.add(file.getName());
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, fileNames);
        listView.setAdapter(adapter);
    }

    private void confirmDeleteOne(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa file này không?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listFiles[position].delete();
                        Toast.makeText(MainActivity.this, "Đã xóa file", Toast.LENGTH_SHORT).show();
                        selectedItem = -1;
                        loadCacheFiles();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDeleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa toàn bộ file cache?")
                .setPositiveButton("Xóa hết", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (File file : listFiles) {
                            file.delete();
                        }
                        Toast.makeText(MainActivity.this, "Đã xóa toàn bộ cache", Toast.LENGTH_SHORT).show();
                        selectedItem = -1;
                        loadCacheFiles();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
