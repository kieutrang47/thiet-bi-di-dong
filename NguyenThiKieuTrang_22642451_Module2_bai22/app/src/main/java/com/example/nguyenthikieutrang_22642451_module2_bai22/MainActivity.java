package com.example.nguyenthikieutrang_22642451_module2_bai22;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    int[] imageIds = {
            R.drawable.doan, R.drawable.bike, R.drawable.baby3,
            R.drawable.sheep, R.drawable.cherry, R.drawable.android_logo
    };

    String[] imageNames = {
            "Earth", "Bike", "Star", "Sheep", "Cherry", "Android"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        ImageAdapter adapter = new ImageAdapter(this, imageIds);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("imageId", imageIds[position]);
            intent.putExtra("imageName", imageNames[position]);
            startActivity(intent);
        });
    }
}
