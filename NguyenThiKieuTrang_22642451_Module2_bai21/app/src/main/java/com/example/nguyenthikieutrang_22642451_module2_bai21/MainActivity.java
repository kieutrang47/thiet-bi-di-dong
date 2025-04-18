package com.example.nguyenthikieutrang_22642451_module2_bai21;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    private AutoCompleteTextView autoCompleteProduct;
    private Button btnAddProduct;
    private GridView gridViewProducts;

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> productAdapter;
    private ArrayAdapter<String> autoCompleteAdapter;

    private HashMap<String, List<String>> categoryProducts;
    private String currentCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Thiết lập giao diện nằm ngang
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần giao diện
        spinnerCategory = findViewById(R.id.spinnerCategory);
        autoCompleteProduct = findViewById(R.id.autoCompleteProduct);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        gridViewProducts = findViewById(R.id.gridViewProducts);

        // Khởi tạo danh sách danh mục sản phẩm
        categoryProducts = new HashMap<>();
        categoryProducts.put("Điện thoại", new ArrayList<String>() {{
            add("Iphone 4");
            add("Iphone 5");
        }});
        categoryProducts.put("Máy tính bảng", new ArrayList<String>() {{
            add("Ipad");
            add("New Ipad");
        }});
        categoryProducts.put("Laptop", new ArrayList<String>() {{
            add("MacBook");
            add("Dell");
        }});

        // Tạo adapter cho Spinner
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(categoryProducts.keySet()));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Tạo adapter cho AutoCompleteTextView
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        autoCompleteProduct.setAdapter(autoCompleteAdapter);

        // Tạo adapter cho GridView
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        gridViewProducts.setAdapter(productAdapter);

        // Xử lý khi chọn danh mục
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = parent.getItemAtPosition(position).toString();
                updateProductList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Xử lý khi nhấn nút "Nhập"
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newProduct = autoCompleteProduct.getText().toString().trim();
                if (!newProduct.isEmpty() && currentCategory != null) {
                    // Thêm sản phẩm mới vào danh sách danh mục
                    categoryProducts.get(currentCategory).add(newProduct);
                    autoCompleteProduct.setText("");

                    // Cập nhật dữ liệu
                    updateProductList();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý khi chọn một sản phẩm trong GridView
        gridViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProduct = parent.getItemAtPosition(position).toString();
                showProductDetails(selectedProduct);
            }
        });
        autoCompleteProduct.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String newProduct = autoCompleteProduct.getText().toString().trim();
                if (!newProduct.isEmpty() && currentCategory != null) {
                    // Thêm sản phẩm vào danh mục hiện tại
                    categoryProducts.get(currentCategory).add(newProduct);
                    autoCompleteProduct.setText("");

                    // Cập nhật dữ liệu
                    updateProductList();

                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(autoCompleteProduct.getWindowToken(), 0);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập sản phẩm", Toast.LENGTH_SHORT).show();
                }
                return true; // Trả về true để báo đã xử lý sự kiện
            }
            return false; // Nếu không phải IME_ACTION_DONE thì không xử lý
        });


    }

    // Cập nhật danh sách sản phẩm cho GridView và AutoCompleteTextView
    private void updateProductList() {
        if (currentCategory != null) {
            List<String> products = categoryProducts.get(currentCategory);

            // Cập nhật GridView
            productAdapter.clear();
            productAdapter.addAll(products);
            productAdapter.notifyDataSetChanged();

            // Cập nhật AutoCompleteTextView
            autoCompleteAdapter.clear();
            autoCompleteAdapter.addAll(products);
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    // Hiển thị AlertDialog thông tin sản phẩm
    private void showProductDetails(String productName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin sản phẩm");
        builder.setMessage("Tên sản phẩm: " + productName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}