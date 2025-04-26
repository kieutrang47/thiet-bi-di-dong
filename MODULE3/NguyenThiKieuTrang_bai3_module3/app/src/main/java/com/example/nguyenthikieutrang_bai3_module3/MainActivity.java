package com.example.nguyenthikieutrang_bai3_module3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnDom, btnSax;
    Spinner spinner;
    ListView listView;
    TextView textView;
    ArrayList<Employee> employees;
    ArrayList<String> titles;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> listAdapter;
    boolean isUsingDom = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDom = findViewById(R.id.btnDom);
        btnSax = findViewById(R.id.btnSax);
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView2);

        btnDom.setOnClickListener(v -> loadXML(true));
        btnSax.setOnClickListener(v -> loadXML(false));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterEmployeesByTitle(titles.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        loadXML(true);
    }

    private void loadXML(boolean useDom) {
        isUsingDom = useDom;
        if (useDom) {
            employees = XMLDomParser.parseXML(this);
        } else {
            XMLSaxParser saxParser = new XMLSaxParser();
            employees = saxParser.parseXML(this);
        }
        titles = XMLDomParser.getUniqueTitles(employees);
        updateSpinner();
    }

    private void updateSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, titles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void filterEmployeesByTitle(String title) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getTitle().equals(title)) {
                filteredList.add(e.getName() + " - " + e.getPhone());
            }
        }
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        listView.setAdapter(listAdapter);
    }
}