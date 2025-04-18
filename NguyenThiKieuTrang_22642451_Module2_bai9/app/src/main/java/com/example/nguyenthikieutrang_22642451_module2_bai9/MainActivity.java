package com.example.nguyenthikieutrang_22642451_module2_bai9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText edtFahrenheit, edtCelsius;
    private Button btnToC, btnToF, btnClear;
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
        // Tìm view sau khi setContentView
        edtFahrenheit = findViewById(R.id.editTextText);
        edtCelsius = findViewById(R.id.editTextText2);
        btnToC = findViewById(R.id.btnToC);
        btnToF = findViewById(R.id.btnToF);
        btnClear = findViewById(R.id.btnClear);

        // Gán sự kiện bằng Explicit Listener Class
        btnToC.setOnClickListener(new ConvertTemperatureListener(edtFahrenheit, edtCelsius, 1));
        btnToF.setOnClickListener(new ConvertTemperatureListener(edtFahrenheit, edtCelsius, 2));
        btnClear.setOnClickListener(new ConvertTemperatureListener(edtFahrenheit, edtCelsius, 3));
    }
}