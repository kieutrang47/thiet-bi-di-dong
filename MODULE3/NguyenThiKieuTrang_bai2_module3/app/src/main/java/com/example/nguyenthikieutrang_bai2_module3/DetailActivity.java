package com.example.nguyenthikieutrang_bai2_module3;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewContent = findViewById(R.id.textViewContent);

        String fileName = getIntent().getStringExtra("fileName");
        if (fileName != null) {
            readFileContent(fileName);
        }
    }

    private void readFileContent(String fileName) {
        File file = new File(getCacheDir(), fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String content = new String(data, "UTF-8");
            textViewContent.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
            textViewContent.setText("Không thể đọc file");
        }
    }
}
