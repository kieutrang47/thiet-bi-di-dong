package com.example.nguyenthikieutrang_22642451_module2_bai8;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private EditText txtA, txtB;
    private TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtA = findViewById(R.id.txtA);
        txtB = findViewById(R.id.txtB);
        textView4 = findViewById(R.id.textView4);

        MaterialButton btnTong = findViewById(R.id.btnTong);
        MaterialButton btnHieu = findViewById(R.id.btnHieu);
        MaterialButton btnTich = findViewById(R.id.btnTich);
        MaterialButton btnThuong = findViewById(R.id.btnThuong);
        MaterialButton btnUCLN = findViewById(R.id.btnUCLN);
        MaterialButton btnThoat = findViewById(R.id.btnThoat);

        btnTong.setOnClickListener(view -> tinhToan('+'));
        btnHieu.setOnClickListener(view -> tinhToan('-'));
        btnTich.setOnClickListener(view -> tinhToan('*'));
        btnThuong.setOnClickListener(view -> tinhToan('/'));
        btnUCLN.setOnClickListener(view -> tinhUCLN());
        btnThoat.setOnClickListener(view -> finish());
    }

    private void tinhToan(char phepToan) {
        try {
            int a = Integer.parseInt(txtA.getText().toString());
            int b = Integer.parseInt(txtB.getText().toString());
            int ketQua = 0;

            switch (phepToan) {
                case '+': ketQua = a + b; break;
                case '-': ketQua = a - b; break;
                case '*': ketQua = a * b; break;
                case '/':
                    if (b == 0) {
                        Toast.makeText(this, "Không thể chia cho 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ketQua = a / b;
                    break;
            }
            textView4.setText(String.valueOf(ketQua));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void tinhUCLN() {
        try {
            int a = Integer.parseInt(txtA.getText().toString());
            int b = Integer.parseInt(txtB.getText().toString());

            int ucln = UCLN(a, b);
            textView4.setText(String.valueOf(ucln));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private int UCLN(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
