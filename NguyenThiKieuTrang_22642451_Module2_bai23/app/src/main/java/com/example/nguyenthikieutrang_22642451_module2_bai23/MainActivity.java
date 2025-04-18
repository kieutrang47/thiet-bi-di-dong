package com.example.nguyenthikieutrang_22642451_module2_bai23;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    EditText edtTask, edtContent;
    TextView txtDate, txtTime;
    Button btnDate, btnTime, btnAddTask;
    ListView lvTasks;
    ArrayList<String> listTasks;
    ArrayAdapter<String> adapter;
    private List<Task> taskList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Log.d("MENU", "onCreate chạy xong");
        // Ánh xạ view
        edtTask = findViewById(R.id.edtTask);
        edtContent = findViewById(R.id.edtContent);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnAddTask = findViewById(R.id.btnAddTask);
        lvTasks = findViewById(R.id.lvTasks);

        registerForContextMenu(lvTasks);



        // Khởi tạo danh sách công việc
        listTasks = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTasks);
        lvTasks.setAdapter(adapter);

        // Xử lý chọn ngày
        btnDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, yearSelected, monthSelected, daySelected) ->
                            txtDate.setText(daySelected + "/" + (monthSelected + 1) + "/" + yearSelected),
                    year, month, day);
            datePickerDialog.show();
        });

        // Xử lý chọn giờ
        btnTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    MainActivity.this,
                    (view, hourOfDay, minuteSelected) -> {
                        String formattedTime = String.format("%02d:%02d", hourOfDay, minuteSelected);
                        txtTime.setText(formattedTime);
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });


        btnAddTask.setOnClickListener(v -> {
            String task = edtTask.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            String date = txtDate.getText().toString();
            String time = txtTime.getText().toString();

            if (!task.isEmpty() && !date.equals("Chọn ngày") && !time.equals("Chọn giờ")) {
                // Tạo chuỗi công việc
                String taskInfo = task + " - " + content + " - " + date + " - " + time;

                // Kiểm tra danh sách đã tồn tại chưa
                if (listTasks == null) {
                    listTasks = new ArrayList<>();
                }

                // Thêm vào danh sách và cập nhật ListView
                listTasks.add(taskInfo);
                adapter.notifyDataSetChanged();

                // Xóa nội dung nhập sau khi thêm
                edtTask.setText("");
                edtContent.setText("");
                txtDate.setText("Chọn ngày");
                txtTime.setText("Chọn giờ");
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (info == null) {
            Toast.makeText(this, "Lỗi: Không thể lấy thông tin công việc!", Toast.LENGTH_SHORT).show();
            return super.onContextItemSelected(item);
        }

        int position = info.position; // Lấy vị trí công việc được chọn
        if (position < 0 || position >= listTasks.size()) {
            Toast.makeText(this, "Lỗi: Công việc không tồn tại!", Toast.LENGTH_SHORT).show();
            return super.onContextItemSelected(item);
        }

        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            editTask(position);
        } else if (id == R.id.menu_delete) {
            deleteTask(position);
        } else if (id == R.id.menu_count) {
            countTasks();
        } else {
            return super.onContextItemSelected(item);
        }
        return true;
    }


    private void deleteTask(int position) {
        if (position >= 0 && position < listTasks.size()) {
            listTasks.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã xóa công việc!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi: Không thể xóa công việc!", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Log.d("MENU", "Menu đã được tạo");
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu(); // Cập nhật lại menu
    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); // Lấy id của item được chọn

        if (id == R.id.menu_completed) {
            // Xử lý khi chọn "Completed"
            return true;
        } else if (id == R.id.menu_uncompleted) {
            // Xử lý khi chọn "Uncompleted"
            return true;
        } else if (id == R.id.menu_clear_all) {
            // Xử lý khi chọn "Clear All"
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCompletedTasks() {
        ArrayList<String> completedTasks = new ArrayList<>();
        for (Task task : taskList) {  // Duyệt qua từng Task, không phải String
            if (task.isCompleted()) {  // Giả sử có phương thức kiểm tra hoàn thành
                completedTasks.add(task.getTitle());  // Lấy tên công việc
            }
        }
        Toast.makeText(this, "Hoàn thành: " + completedTasks.size() + " công việc", Toast.LENGTH_SHORT).show();
    }


    private void showUncompletedTasks() {
        ArrayList<String> uncompletedTasks = new ArrayList<>();
        for (Task task : taskList) {
            if (!task.isCompleted()) {
                uncompletedTasks.add(task.getTitle());
            }
        }
        Toast.makeText(this, "Chưa hoàn thành: " + uncompletedTasks.size() + " công việc", Toast.LENGTH_SHORT).show();
    }


    private void clearAllTasks() {
        taskList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Đã xóa toàn bộ công việc!", Toast.LENGTH_SHORT).show();
    }


    private void editTask(int position) {
        if (position >= 0 && position < listTasks.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chỉnh sửa công việc");

            final EditText input = new EditText(this);
            input.setText(listTasks.get(position));  // Lấy từ listTasks
            builder.setView(input);

            builder.setPositiveButton("Lưu", (dialog, which) -> {
                listTasks.set(position, input.getText().toString()); // Cập nhật listTasks
                adapter.notifyDataSetChanged();
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

            builder.show();
        } else {
            Toast.makeText(this, "Lỗi: Không thể chỉnh sửa!", Toast.LENGTH_SHORT).show();
        }
    }



    private void countTasks() {
        Toast.makeText(this, "Tổng số công việc: " + taskList.size(), Toast.LENGTH_SHORT).show();
    }

}