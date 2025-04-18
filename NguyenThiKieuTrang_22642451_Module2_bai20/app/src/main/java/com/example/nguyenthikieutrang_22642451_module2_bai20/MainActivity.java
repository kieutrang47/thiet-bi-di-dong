package com.example.nguyenthikieutrang_22642451_module2_bai20;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerDanhMuc;
    private ListView listViewSanPham;
    private List<SanPham> sanPhamList;
    private SanPhamAdapter sanPhamAdapter;
    private Map<String, List<SanPham>> dataSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        listViewSanPham = findViewById(R.id.listView);

        // Danh sách danh mục
        List<String> danhMucList = new ArrayList<>();
        danhMucList.add("Điện Thoại");
        danhMucList.add("Máy Tính");
        danhMucList.add("Đồng Hồ");

        // Adapter cho Spinner
        ArrayAdapter<String> danhMucAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, danhMucList);
        spinnerDanhMuc.setAdapter(danhMucAdapter);

        // Dữ liệu sản phẩm theo danh mục
        dataSanPham = new HashMap<>();

        dataSanPham.put("Điện Thoại", new ArrayList<SanPham>() {{
            add(new SanPham("iPhone 5", R.drawable.star));
            add(new SanPham("SamSung S III", R.drawable.earth));
            add(new SanPham("HTC", R.drawable.star));
            add(new SanPham("Windows Phone Surface", R.drawable.earth));
            add(new SanPham("Nokia 1100", R.drawable.earth));
            add(new SanPham("Q-Mobile", R.drawable.earth));
        }});
        dataSanPham.put("Máy Tính", new ArrayList<SanPham>() {{
            add(new SanPham("MacBook Air", R.drawable.star));
            add(new SanPham("Dell XPS", R.drawable.earth));
            add(new SanPham("Asus ROG", R.drawable.star));
        }});
        dataSanPham.put("Đồng Hồ", new ArrayList<SanPham>() {{
            add(new SanPham("Rolex", R.drawable.star));
            add(new SanPham("Casio G-Shock", R.drawable.earth));
            add(new SanPham("Apple Watch", R.drawable.star));
        }});

        // Khởi tạo danh sách sản phẩm rỗng và adapter
        sanPhamList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(this, sanPhamList);
        listViewSanPham.setAdapter(sanPhamAdapter);

        // Mặc định hiển thị sản phẩm của danh mục đầu tiên
        String danhMucMacDinh = danhMucList.get(0);
        List<SanPham> sanPhamMacDinh = dataSanPham.get(danhMucMacDinh);
        if (sanPhamMacDinh != null) {
            sanPhamList.addAll(sanPhamMacDinh);
            sanPhamAdapter.notifyDataSetChanged();
        }

        // Xử lý khi chọn danh mục mới
        spinnerDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String danhMuc = danhMucList.get(position);
                List<SanPham> danhSachMoi = dataSanPham.get(danhMuc);

                if (danhSachMoi != null) {
                    sanPhamList.clear();
                    sanPhamList.addAll(danhSachMoi);
                    sanPhamAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Không có sản phẩm trong danh mục này!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý sự kiện click vào sản phẩm
        listViewSanPham.setOnItemClickListener((parent, view, position, id) -> {
            String tenSP = sanPhamList.get(position).getTenSanPham();
            Toast.makeText(MainActivity.this, "Bạn đã chọn: " + tenSP, Toast.LENGTH_SHORT).show();
        });
    }
}
