package com.example.nguyenthikieutrang_22642451_module2_bai10;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView tvResult;
    private String currentInput = ""; // Chuỗi chứa biểu thức nhập vào
    private String operator = ""; // Lưu toán tử
    private double firstNumber = 0; // Lưu số thứ nhất

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
        tvResult = findViewById(R.id.tvResult);

        // Gán sự kiện cho các nút
        setNumberClickListener(R.id.btn0, "0");
        setNumberClickListener(R.id.btn1, "1");
        setNumberClickListener(R.id.btn2, "2");
        setNumberClickListener(R.id.btn3, "3");
        setNumberClickListener(R.id.btn4, "4");
        setNumberClickListener(R.id.btn5, "5");
        setNumberClickListener(R.id.btn6, "6");
        setNumberClickListener(R.id.btn7, "7");
        setNumberClickListener(R.id.btn8, "8");
        setNumberClickListener(R.id.btn9, "9");
        setNumberClickListener(R.id.btnDot, ".");

        setOperatorClickListener(R.id.btnPlus, "+");
        setOperatorClickListener(R.id.btnSub, "-");
        setOperatorClickListener(R.id.btnMul, "*");
        setOperatorClickListener(R.id.btnDivide, "/");

        // Nút xóa
        findViewById(R.id.btnDelete).setOnClickListener(view -> {
            currentInput = "";
            firstNumber = 0;
            operator = "";
            tvResult.setText("0");
        });

        // Nút "=" để tính toán kết quả
        findViewById(R.id.btnEqual).setOnClickListener(view -> calculateResult());
    }

    // Hàm gán sự kiện cho các nút số
    private void setNumberClickListener(int buttonId, final String value) {
        findViewById(buttonId).setOnClickListener(view -> {
            if (currentInput.equals("0")) {
                currentInput = value;
            } else {
                currentInput += value;
            }

            if (tvResult.getText().toString().equals("0")) {
                tvResult.setText(value);
            } else {
                tvResult.setText(tvResult.getText().toString() + value);
            }
        });
    }



    // Hàm gán sự kiện cho các nút phép toán
    private void setOperatorClickListener(int buttonId, final String op) {
        findViewById(buttonId).setOnClickListener(view -> {
            if (!currentInput.isEmpty()) {
                firstNumber = Double.parseDouble(currentInput); // Lưu số thứ nhất
                operator = op; // Lưu toán tử
                tvResult.setText(tvResult.getText().toString() + " " + op + " ");
                currentInput = "";
            }
        });
    }




    // Hàm thực hiện phép toán
    private void calculateResult() {
        if (!currentInput.isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(currentInput);
            double result = 0;

            switch (operator) {
                case "+": result = firstNumber + secondNumber; break;
                case "-": result = firstNumber - secondNumber; break;
                case "*": result = firstNumber * secondNumber; break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        tvResult.setText("Lỗi!");
                        return;
                    }
                    break;
            }

            tvResult.setText(String.valueOf(result)); // Chỉ hiển thị kết quả
            currentInput = String.valueOf(result); // Lưu lại kết quả để tính tiếp
            operator = ""; // Reset toán tử
        }
    }



}