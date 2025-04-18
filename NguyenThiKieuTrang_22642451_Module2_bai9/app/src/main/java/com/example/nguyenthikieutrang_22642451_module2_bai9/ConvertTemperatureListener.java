package com.example.nguyenthikieutrang_22642451_module2_bai9;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ConvertTemperatureListener implements View.OnClickListener {
    private EditText edtFahrenheit, edtCelsius;
    private int type; // 1: F -> C, 2: C -> F, 3: Clear

    public ConvertTemperatureListener(EditText edtFahrenheit, EditText edtCelsius, int type) {
        this.edtFahrenheit = edtFahrenheit;
        this.edtCelsius = edtCelsius;
        this.type = type;
    }

    @Override
    public void onClick(View v) {
        try {
            if (type == 1) { // F -> C
                String value = edtFahrenheit.getText().toString();
                if (!value.isEmpty()) {
                    double fahrenheit = Double.parseDouble(value);
                    double celsius = (fahrenheit - 32) * 5 / 9;
                    edtCelsius.setText(String.format("%.2f", celsius));
                }
            } else if (type == 2) { // C -> F
                String value = edtCelsius.getText().toString();
                if (!value.isEmpty()) {
                    double celsius = Double.parseDouble(value);
                    double fahrenheit = (celsius * 9 / 5) + 32;
                    edtFahrenheit.setText(String.format("%.2f", fahrenheit));
                }
            } else if (type == 3) { // Clear
                edtFahrenheit.setText("");
                edtCelsius.setText("");
            }
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}
