package com.example.nguyenthikieutrang_22642451_module2_bai16;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private ListView listView;
    private View selectedView;
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
        textView = findViewById(R.id.textView3);
        listView = findViewById(R.id.listView);

        // Lấy dữ liệu từ strings.xml
        String[] items = getResources().getStringArray(R.array.list_items);

        // Adapter để đổ dữ liệu vào ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // Bắt sự kiện khi click vào item trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedView != null) {
                    selectedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                String value = items[position];
                view.setBackgroundColor(Color.rgb(128,208,235));
                selectedView = view;

                textView.setText("position: " + position + "; value = " + value);
            }
        });
    }
}