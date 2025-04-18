package com.example.nguyenthikieutrang_22642451_module2_bai13;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText edtYear;
    private TextView tvResult;
    private Button btnConvert;

    private final String[] canArray = {"Canh", "Tân", "Nhâm", "Quý", "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ"};
    private final String[] chiArray = {"Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mẹo", "Thìn", "Tỵ", "Ngọ", "Mùi"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ⚠️ Đưa các lệnh này vào đúng chỗ
        edtYear = findViewById(R.id.txtDuong);
        tvResult = findViewById(R.id.txtAm);
        btnConvert = findViewById(R.id.btnChuyen);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertYear();
            }
        });
    }

    private void convertYear() {
        String yearInput = edtYear.getText().toString();

        if (yearInput.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập năm dương lịch!", Toast.LENGTH_SHORT).show();
            return;
        }

        int namDuong = Integer.parseInt(yearInput);
        if (namDuong < 1900) {
            Toast.makeText(this, "Năm phải >= 1900!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tính Can & Chi
        String can = canArray[namDuong % 10];
        String chi = chiArray[namDuong % 12];
        String namAmLich = can + " " + chi;

        // Hiển thị kết quả
        tvResult.setText("Năm Âm lịch: " + namAmLich);
    }
}