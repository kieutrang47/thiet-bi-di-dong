package com.example.nguyenthikieutrang_bai7_module3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowAllContact = findViewById(R.id.btnShowAllContact);
        Button btnAccessCallLog = findViewById(R.id.btnAccessCallLog);
        Button btnAccessMediaStore = findViewById(R.id.btnAccessMediaStore);
        Button btnAccessBookmarks = findViewById(R.id.btnAccessBookmarks);

        btnShowAllContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    Intent intent = new Intent(MainActivity.this, ShowAllContactActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions();
                }
            }
        });

        btnAccessCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    Intent intent = new Intent(MainActivity.this, CallLogActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions();
                }
            }
        });

        btnAccessMediaStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    Intent intent = new Intent(MainActivity.this, MediaStoreActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions();
                }
            }
        });

        btnAccessBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookmarksActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Cần cấp quyền để sử dụng ứng dụng", Toast.LENGTH_SHORT).show();
            }
        }
    }
}