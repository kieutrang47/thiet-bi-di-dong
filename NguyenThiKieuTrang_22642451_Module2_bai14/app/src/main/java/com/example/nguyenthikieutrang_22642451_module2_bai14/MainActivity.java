package com.example.nguyenthikieutrang_22642451_module2_bai14;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText txtTen, txtCMND, txtThongTinBoSung;
    private RadioGroup radioGroupBangCap;
    private CheckBox chkDocBao, chkDocSach, chkDocCode;
    private Button btnGuiThongTin;
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
        // Ánh xạ view
        txtTen = findViewById(R.id.txtTen);
        txtCMND = findViewById(R.id.txtCMND);
        txtThongTinBoSung = findViewById(R.id.editTextText3);
        radioGroupBangCap = findViewById(R.id.group);
        chkDocBao = findViewById(R.id.chkDocBao);
        chkDocSach = findViewById(R.id.chkDocSach);
        chkDocCode = findViewById(R.id.chkDocCode);
        btnGuiThongTin = findViewById(R.id.button);

        // Mặc định chọn "Đại học"
        RadioButton rdDaiHoc = findViewById(R.id.rdDaiHoc);
        rdDaiHoc.setChecked(true);

        // Xử lý sự kiện khi nhấn nút gửi thông tin
        btnGuiThongTin.setOnClickListener(view -> xuLyGuiThongTin());

    }
    private void xuLyGuiThongTin() {
        String ten = txtTen.getText().toString().trim();
        String cmnd = txtCMND.getText().toString().trim();
        String thongTinBoSung = txtThongTinBoSung.getText().toString().trim();

        if (ten.isEmpty() || ten.length() < 3) {
            Toast.makeText(this, "Tên phải có ít nhất 3 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cmnd.matches("\\d{9}")) {
            Toast.makeText(this, "CMND phải có đúng 9 chữ số!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupBangCap.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String bangCap = selectedRadioButton.getText().toString();

        StringBuilder soThich = new StringBuilder();
        if (chkDocBao.isChecked()) soThich.append("Đọc báo - ");
        if (chkDocSach.isChecked()) soThich.append("Đọc sách - ");
        if (chkDocCode.isChecked()) soThich.append("Đọc coding - ");

        if (soThich.length() == 0) {
            Toast.makeText(this, "Bạn phải chọn ít nhất một sở thích!", Toast.LENGTH_SHORT).show();
            return;
        }
        soThich.setLength(soThich.length() - 3); // Xóa dấu '-' cuối

        // Tạo nội dung với định dạng
        String thongTin = "Họ tên: " + ten + "\n" +
                "CMND: " + cmnd + "\n" +
                "Bằng cấp: " + bangCap + "\n" +
                "Sở thích: " + soThich + "\n" +
                "-----------------------------\n" +
                "Thông tin bổ sung:\n" +
                (thongTinBoSung.isEmpty() ? "Không có" : thongTinBoSung) + "\n" +
                "-----------------------------";

        // **1. Tạo TextView để hiển thị nội dung**
        TextView messageView = new TextView(this);
        messageView.setText(thongTin);
        messageView.setPadding(40, 20, 40, 20);
        messageView.setTextSize(20);
        messageView.setTextColor(Color.BLACK);

        // **2. Tạo TextView làm title có màu xanh dương**
        TextView titleView = new TextView(this);
        titleView.setText("Thông tin cá nhân");
        titleView.setPadding(40, 30, 40, 30);
        titleView.setTextSize(30);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(Color.rgb(123,208,239));
        titleView.setGravity(Gravity.CENTER);

        // **3. Tạo AlertDialog**
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCustomTitle(titleView) // Đặt title với màu xanh
                .setView(messageView)
                .setPositiveButton("Đóng", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            // **4. Đổi màu nút Đóng
            Button btnDong = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnDong.setBackgroundColor(Color.rgb(123,208,239));
            btnDong.setGravity(Gravity.LEFT);

        });

        dialog.show();
    }


}