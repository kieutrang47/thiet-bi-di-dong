package com.example.nguyenthikieutrang_22642451_module2_bai18;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private TextView textView3;
    private ListView listView;
    private List<String> locations = Arrays.asList("Hà nội", "huế", "spa", "côn sơn", "vũng tàu", "đà nẵng");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView3 = findViewById(R.id.textView3);
        listView = findViewById(R.id.listView);

        CustomAdapter adapter = new CustomAdapter(this, locations);
        listView.setAdapter(adapter);

        // Sự kiện click vào item
        listView.setOnItemClickListener((parent, view, position, id) -> {
            textView3.setText("Position: " + position + "; Value = " + locations.get(position));
        });
    }
}