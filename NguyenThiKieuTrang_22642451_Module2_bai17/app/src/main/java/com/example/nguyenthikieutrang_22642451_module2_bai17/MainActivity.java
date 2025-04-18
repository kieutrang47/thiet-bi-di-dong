package com.example.nguyenthikieutrang_22642451_module2_bai17;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText txtTen;
    private Button btnNhap;
    private TextView textView3;
    private ListView listView;
    private ArrayList<String> nameList;
    private ArrayAdapter<String> adapter;
    private View selectedView = null; // View đang được chọn
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
        // Ánh xạ ID từ layout
        txtTen = findViewById(R.id.txtTen);
        btnNhap = findViewById(R.id.btnNhap);
        textView3 = findViewById(R.id.textView3);
        listView = findViewById(R.id.listView);

        // Khởi tạo danh sách
        nameList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapter);

        // Sự kiện khi nhấn nút "Nhập"
        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = txtTen.getText().toString().trim();
                if (!ten.isEmpty()) {
                    nameList.add(ten); // Thêm vào danh sách
                    adapter.notifyDataSetChanged(); // Cập nhật lại ListView
                    txtTen.setText(""); // Xóa nội dung EditText sau khi nhập
                }
            }
        });

        // Xử lý sự kiện click vào từng phần tử trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Nếu có item cũ đang chọn, reset về màu mặc định
                if (selectedView != null) {
                    selectedView.setBackgroundColor(Color.TRANSPARENT);
                }

                // Đổi màu item mới được chọn
                view.setBackgroundColor(Color.rgb(104,185,215));
                selectedView = view; // Lưu lại item đang được chọn

                // Cập nhật TextView màu xanh với vị trí + giá trị item
                String selectedItem = nameList.get(position);
                textView3.setText("Position: " + position + "; Value = " + selectedItem);
            }
        });
    }

}