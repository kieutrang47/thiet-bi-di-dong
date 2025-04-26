package com.example.nguyenthikieutrang_bai7_module3;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookmarksActivity extends AppCompatActivity {

    private ListView listContacts;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_activity);

        setTitle("BookmarksActivity");

        listContacts = findViewById(R.id.listContacts);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadBookmarks();
    }

    private void loadBookmarks() {
        ArrayList<String> bookmarkList = new ArrayList<>();
        int count = 1;

        try {
            // Sử dụng Uri trực tiếp thay vì Browser.BOOKMARKS_URI
            Uri bookmarksUri = Uri.parse("content://browser/bookmarks");

            String[] projection = new String[]{
                    "title",  // Thay vì Browser.BookmarkColumns.TITLE
                    "url"     // Thay vì Browser.BookmarkColumns.URL
            };

            Cursor cursor = getContentResolver().query(
                    bookmarksUri,
                    projection,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.getCount() > 0) {
                int titleIndex = cursor.getColumnIndex("title");
                int urlIndex = cursor.getColumnIndex("url");

                while (cursor.moveToNext()) {
                    String title = cursor.getString(titleIndex);
                    String url = cursor.getString(urlIndex);
                    bookmarkList.add(count + " - " + title + " (" + url + ")");
                    count++;
                }
                cursor.close();
            } else if (cursor != null) {
                cursor.close();
                bookmarkList.add("Không có bookmark nào");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Không thể truy cập bookmarks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            bookmarkList.add("Không thể truy cập bookmarks");
        }

        if (bookmarkList.isEmpty()) {
            bookmarkList.add("Không có bookmark nào");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookmarkList);
        listContacts.setAdapter(adapter);
    }
}