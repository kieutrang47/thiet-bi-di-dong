package com.example.nguyenthikieutrang_bai7_module3;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CallLogActivity extends AppCompatActivity {

    private ListView listContacts;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_activity);

        setTitle("CallLogActivity");

        listContacts = findViewById(R.id.listContacts);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadCallLogs();
    }

    private void loadCallLogs() {
        ArrayList<String> callLogList = new ArrayList<>();
        int count = 1;

        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
        };

        Cursor cursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );

        if (cursor != null && cursor.getCount() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
            int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                if (name == null || name.isEmpty()) {
                    name = "Unknown";
                }
                String phoneNumber = cursor.getString(numberIndex);
                String callDate = dateFormat.format(new Date(Long.parseLong(cursor.getString(dateIndex))));
                String callType = getCallTypeString(cursor.getInt(typeIndex));

                callLogList.add(count + " - " + name + " (" + phoneNumber + ") " + callType + " - " + callDate);
                count++;
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, callLogList);
        listContacts.setAdapter(adapter);
    }

    private String getCallTypeString(int callType) {
        String type = "";
        switch (callType) {
            case CallLog.Calls.INCOMING_TYPE:
                type = "Incoming";
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                type = "Outgoing";
                break;
            case CallLog.Calls.MISSED_TYPE:
                type = "Missed";
                break;
            default:
                type = "Unknown";
                break;
        }
        return type;
    }
}