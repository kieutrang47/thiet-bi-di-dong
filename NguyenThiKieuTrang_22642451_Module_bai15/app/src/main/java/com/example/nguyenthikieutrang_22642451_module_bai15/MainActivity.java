package com.example.nguyenthikieutrang_22642451_module_bai15;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText txtTen, txtSoLuong;
    CheckBox chkIsVIP;
    TextView txtThanhTien, txtTongKH, txtTongVIP, txtTongDoanhThu;
    Button btnTinhTT, btnTiep, btnThongKe;
    ImageButton btnThoat;
    ArrayList<HoaDon> danhSachHoaDon = new ArrayList<>();
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
        txtTen = findViewById(R.id.txtTen);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        chkIsVIP = findViewById(R.id.chkIsVIP);
        txtThanhTien = findViewById(R.id.ThanhTien);
        txtTongKH = findViewById(R.id.txtTongKH);
        txtTongVIP = findViewById(R.id.txtTongVIP);
        txtTongDoanhThu = findViewById(R.id.txtTongDoanhThu);
        btnTinhTT = findViewById(R.id.btnTinhTT);
        btnTiep = findViewById(R.id.btnTiep);
        btnThongKe = findViewById(R.id.btnThongKe);
        btnThoat = findViewById(R.id.btnThoat);

        btnTinhTT.setOnClickListener(v -> tinhThanhTien());
        btnTiep.setOnClickListener(v -> luuHoaDon());
        btnThongKe.setOnClickListener(v -> thongKe());
        btnThoat.setOnClickListener(v -> thoatChuongTrinh());
    }
    private void tinhThanhTien() {
        String tenKH = txtTen.getText().toString().trim();
        String soLuongStr = txtSoLuong.getText().toString().trim();

        if (tenKH.isEmpty() || soLuongStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int soLuong = Integer.parseInt(soLuongStr);
        int donGia = 20000;
        double thanhTien = soLuong * donGia;

        if (chkIsVIP.isChecked()) {
            thanhTien *= 0.9; // Giảm 10% nếu là VIP
        }

        txtThanhTien.setText(String.format("%,.0f VND", thanhTien));
    }

    private void luuHoaDon() {
        String tenKH = txtTen.getText().toString().trim();
        String soLuongStr = txtSoLuong.getText().toString().trim();

        if (tenKH.isEmpty() || soLuongStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int soLuong = Integer.parseInt(soLuongStr);
        int donGia = 20000;
        double thanhTien = soLuong * donGia;
        boolean isVIP = chkIsVIP.isChecked();

        if (isVIP) {
            thanhTien *= 0.9;
        }

        danhSachHoaDon.add(new HoaDon(tenKH, soLuong, isVIP, thanhTien));

        // Xóa dữ liệu nhập vào và focus lại EditText tên khách hàng
        txtTen.setText("");
        txtSoLuong.setText("");
        chkIsVIP.setChecked(false);
        txtThanhTien.setText("");
        txtTen.requestFocus();
    }

    private void thongKe() {
        if (danhSachHoaDon.isEmpty()) {
            Toast.makeText(this, "Chưa có hóa đơn nào để thống kê!", Toast.LENGTH_SHORT).show();
            return;
        }

        int tongKH = danhSachHoaDon.size();
        int tongVIP = 0;
        double tongDoanhThu = 0;

        for (HoaDon hd : danhSachHoaDon) {
            if (hd.isVIP) tongVIP++;
            tongDoanhThu += hd.thanhTien;
        }

        // Set giá trị đúng cho TextView
        txtTongKH.setText(String.format("Tổng số KH: %d", tongKH));
        txtTongVIP.setText(String.format("KH VIP: %d", tongVIP));
        txtTongDoanhThu.setText(String.format("Tổng DT: %,.0f VND", tongDoanhThu));
    }


    private void thoatChuongTrinh() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thoát")
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setPositiveButton("Có", (dialog, which) -> finish())
                .setNegativeButton("Không", null)
                .show();
    }
}
class HoaDon {
    String tenKH;
    int soLuong;
    boolean isVIP;
    double thanhTien;

    public HoaDon(String tenKH, int soLuong, boolean isVIP, double thanhTien) {
        this.tenKH = tenKH;
        this.soLuong = soLuong;
        this.isVIP = isVIP;
        this.thanhTien = thanhTien;
    }
}