package com.example.nguyenthikieutrang_22642451_module2_bai22;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.imageViewDetail);
        TextView textView = findViewById(R.id.imageTitle);
        Button btnBack = findViewById(R.id.btnBack);

        int imageId = getIntent().getIntExtra("imageId", 0);
        String name = getIntent().getStringExtra("imageName");

        imageView.setImageResource(imageId);
        textView.setText(name);

        btnBack.setOnClickListener(v -> finish());
    }
}
