package com.example.nguyenthikieutrang_bai2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    // Mã yêu cầu quyền truy cập bộ nhớ
    private static final int MA_YEU_CAU_QUYEN_BO_NHO = 100;
    // Ô nhập văn bản
    private EditText oNhapVanBan;
    // Tên file để lưu dữ liệu
    private static final String TEN_FILE = "vidu.txt";
    // Tag cho logging
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liên kết các thành phần giao diện
        oNhapVanBan = findViewById(R.id.editText);
        Button nutGhiNoiBo = findViewById(R.id.btnWriteInternal);
        Button nutDocNoiBo = findViewById(R.id.btnReadInternal);
        Button nutGhiNgoaiBo = findViewById(R.id.btnWriteExternal);
        Button nutDocNgoaiBo = findViewById(R.id.btnReadExternal);
        Button nutXoa = findViewById(R.id.btnClear);

        // Sự kiện khi nhấn nút Ghi vào bộ nhớ trong
        nutGhiNoiBo.setOnClickListener(v -> ghiVaoBoNhoTrong());

        // Sự kiện khi nhấn nút Đọc từ bộ nhớ trong
        nutDocNoiBo.setOnClickListener(v -> docTuBoNhoTrong());

        // Sự kiện khi nhấn nút Ghi vào bộ nhớ ngoài (SD Card)
        nutGhiNgoaiBo.setOnClickListener(v -> {
            if (kiemTraQuyenBoNho()) {
                ghiVaoBoNhoNgoai();
            } else {
                yeuCauQuyenBoNho();
            }
        });

        // Sự kiện khi nhấn nút Đọc từ bộ nhớ ngoài (SD Card)
        nutDocNgoaiBo.setOnClickListener(v -> {
            if (kiemTraQuyenBoNho()) {
                docTuBoNhoNgoai();
            } else {
                yeuCauQuyenBoNho();
            }
        });

        // Sự kiện khi nhấn nút Xóa nội dung
        nutXoa.setOnClickListener(v -> {
            oNhapVanBan.setText("");
            Toast.makeText(this, "Đã xóa nội dung trong EditText", Toast.LENGTH_SHORT).show();
        });
    }

    // Kiểm tra xem đã có quyền truy cập bộ nhớ ngoài chưa
    private boolean kiemTraQuyenBoNho() {
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        if (!granted) {
            Toast.makeText(this, "Chưa có quyền truy cập bộ nhớ ngoài", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Quyền truy cập bộ nhớ ngoài chưa được cấp");
        }
        return granted;
    }

    // Yêu cầu quyền truy cập bộ nhớ ngoài
    private void yeuCauQuyenBoNho() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                MA_YEU_CAU_QUYEN_BO_NHO);
        Log.d(TAG, "Yêu cầu quyền truy cập bộ nhớ ngoài");
    }

    // Ghi dữ liệu vào bộ nhớ trong
    private void ghiVaoBoNhoTrong() {
        String duLieu = oNhapVanBan.getText().toString();
        if (duLieu.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung trước khi ghi", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileOutputStream fos = openFileOutput(TEN_FILE, MODE_PRIVATE)) {
            fos.write(duLieu.getBytes());
            String duongDan = getFilesDir().getAbsolutePath() + "/" + TEN_FILE;
            Toast.makeText(this, "Đã ghi vào bộ nhớ trong: " + duongDan, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Ghi dữ liệu vào bộ nhớ trong thành công: " + duongDan);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi ghi vào bộ nhớ trong: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Lỗi ghi bộ nhớ trong: " + e.getMessage());
        }
    }

    // Đọc dữ liệu từ bộ nhớ trong
    private void docTuBoNhoTrong() {
        File file = new File(getFilesDir(), TEN_FILE);
        if (!file.exists()) {
            Toast.makeText(this, "File " + TEN_FILE + " không tồn tại trong bộ nhớ trong", Toast.LENGTH_LONG).show();
            Log.e(TAG, "File không tồn tại: " + file.getAbsolutePath());
            return;
        }

        try (FileInputStream fis = openFileInput(TEN_FILE);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder chuoiDuLieu = new StringBuilder();
            String dong;
            while ((dong = br.readLine()) != null) {
                chuoiDuLieu.append(dong).append("\n");
            }
            oNhapVanBan.setText(chuoiDuLieu.toString());
            Toast.makeText(this, "Đã đọc từ bộ nhớ trong: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Đọc dữ liệu từ bộ nhớ trong thành công: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi đọc từ bộ nhớ trong: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Lỗi đọc bộ nhớ trong: " + e.getMessage());
        }
    }

    // Ghi dữ liệu vào bộ nhớ ngoài (SD Card)
    private void ghiVaoBoNhoNgoai() {
        // Kiểm tra trạng thái bộ nhớ ngoài
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Bộ nhớ ngoài không khả dụng. Trạng thái: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Bộ nhớ ngoài không khả dụng: " + Environment.getExternalStorageState());
            return;
        }

        String duLieu = oNhapVanBan.getText().toString();
        if (duLieu.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung trước khi ghi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Trỏ đến thư mục Documents
        File thuMucNgoai = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        // Tạo thư mục nếu chưa tồn tại
        if (!thuMucNgoai.exists()) {
            if (thuMucNgoai.mkdirs()) {
                Log.d(TAG, "Tạo thư mục thành công: " + thuMucNgoai.getAbsolutePath());
            } else {
                Toast.makeText(this, "Không thể tạo thư mục: " + thuMucNgoai.getAbsolutePath(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Không thể tạo thư mục: " + thuMucNgoai.getAbsolutePath());
                return;
            }
        }

        // Trỏ đến file vidu.txt trong thư mục Documents
        File file = new File(thuMucNgoai, TEN_FILE);
        Log.d(TAG, "Đường dẫn file: " + file.getAbsolutePath());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(duLieu.getBytes());
            Toast.makeText(this, "Đã ghi vào bộ nhớ ngoài: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Ghi dữ liệu vào bộ nhớ ngoài thành công: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi ghi vào bộ nhớ ngoài: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Lỗi ghi bộ nhớ ngoài: " + e.getMessage());
        }
    }

    // Đọc dữ liệu từ bộ nhớ ngoài (SD Card)
    private void docTuBoNhoNgoai() {
        // Kiểm tra trạng thái bộ nhớ ngoài
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Bộ nhớ ngoài không khả dụng. Trạng thái: " + Environment.getExternalStorageState(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Bộ nhớ ngoài không khả dụng: " + Environment.getExternalStorageState());
            return;
        }

        // Trỏ đến thư mục Documents
        File thuMucNgoai = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        // Trỏ đến file vidu.txt trong thư mục Documents
        File file = new File(thuMucNgoai, TEN_FILE);
        Log.d(TAG, "Đường dẫn file: " + file.getAbsolutePath());

        // Kiểm tra file có tồn tại không
        if (!file.exists()) {
            Toast.makeText(this, "File " + TEN_FILE + " không tồn tại trong: " + thuMucNgoai.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "File không tồn tại: " + file.getAbsolutePath());
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder chuoiDuLieu = new StringBuilder();
            String dong;
            while ((dong = br.readLine()) != null) {
                chuoiDuLieu.append(dong).append("\n");
            }
            oNhapVanBan.setText(chuoiDuLieu.toString());
            Toast.makeText(this, "Đã đọc từ bộ nhớ ngoài: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Đọc dữ liệu từ bộ nhớ ngoài thành công: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi đọc từ bộ nhớ ngoài: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Lỗi đọc bộ nhớ ngoài: " + e.getMessage());
        }
    }

    // Xử lý kết quả yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MA_YEU_CAU_QUYEN_BO_NHO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền truy cập bộ nhớ", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Quyền truy cập bộ nhớ đã được cấp");
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Quyền truy cập bộ nhớ bị từ chối");
            }
        }
    }
}