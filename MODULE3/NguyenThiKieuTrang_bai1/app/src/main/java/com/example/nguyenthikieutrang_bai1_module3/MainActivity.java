package com.example.nguyenthikieutrang_bai1_module3;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;


import android.graphics.Matrix;

public class MainActivity extends AppCompatActivity {
    private ImageView imgView;
    private Button btnChoose, btnRotate, btnCrop, btnSave, btnClear;
    private Bitmap currentBitmap;
    private Uri imageUri;
    private Uri croppedImageUri;


    private static final String IMAGE_FILE_NAME = "saved_image.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.imgView);
        btnChoose = findViewById(R.id.btnChoose);
        btnRotate = findViewById(R.id.btnRotate);
        btnCrop = findViewById(R.id.btnCrop);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);

        btnChoose.setOnClickListener(v -> pickImage());
        btnRotate.setOnClickListener(v -> rotateImage());
        btnCrop.setOnClickListener(v -> cropImage());
        btnSave.setOnClickListener(v -> saveImageToGallery());
        btnClear.setOnClickListener(v -> clearImage());
        cropImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (croppedImageUri != null) {
                    try {
                        currentBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedImageUri));
                        imgView.setImageBitmap(currentBitmap);
                        imageUri = croppedImageUri; // Cập nhật Uri ảnh mới để lưu
                    } catch (IOException e) {
                        Toast.makeText(this, "Lỗi tải ảnh đã cắt!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cắt ảnh thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            }
//        }

        loadSavedImage();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6 - 12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    try {
                        currentBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imgView.setImageBitmap(currentBitmap);
                    } catch (IOException e) {
                        Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void rotateImage() {
        if (currentBitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            currentBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentBitmap.getWidth(), currentBitmap.getHeight(), matrix, true);
            imgView.setImageBitmap(currentBitmap);
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void cropImage() {
        if (imageUri == null) {
            Toast.makeText(this, "Chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Tạo Uri lưu ảnh cắt bằng MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Cropped Image");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            croppedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imageUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 500);
            cropIntent.putExtra("outputY", 500);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            if (cropIntent.resolveActivity(getPackageManager()) != null) {
                cropImageLauncher.launch(cropIntent);
            } else {
                Toast.makeText(this, "Thiết bị không hỗ trợ cắt ảnh!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi cắt ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<Intent> cropImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && croppedImageUri != null) {
                    try {
                        currentBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedImageUri));
                        imgView.setImageBitmap(currentBitmap);
                        imageUri = croppedImageUri; // Cập nhật Uri ảnh mới để lưu
                    } catch (IOException e) {
                        Toast.makeText(this, "Lỗi tải ảnh đã cắt!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    private void saveImage() {
        if (currentBitmap != null) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "saved_image.jpg");

            try (FileOutputStream out = new FileOutputStream(file)) {
                currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Toast.makeText(this, "Image saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No image to save!", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadSavedImage() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGE_FILE_NAME);
        if (file.exists()) {
            currentBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imgView.setImageBitmap(currentBitmap);
        }
    }

    private void clearImage() {
        imgView.setImageDrawable(null);
        currentBitmap = null;
        imageUri = null;
        Toast.makeText(this, "Image cleared", Toast.LENGTH_SHORT).show();
    }

    private void saveImageToGallery() {
        if (currentBitmap != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "edited_" + System.currentTimeMillis() + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp");

            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try (OutputStream out = getContentResolver().openOutputStream(imageUri)) {
                currentBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Toast.makeText(this, "Ảnh đã lưu vào thư viện!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Lỗi lưu ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không có ảnh để lưu!", Toast.LENGTH_SHORT).show();
        }
    }
}