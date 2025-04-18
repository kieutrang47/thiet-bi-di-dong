package com.example.nguyenthikieutrang_22642451_module2_bai18;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> items;

    public CustomAdapter(Context context, List<String> items) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        ImageView imgIcon = convertView.findViewById(R.id.imgIcon);
        TextView txtName = convertView.findViewById(R.id.txtName);

        String item = items.get(position);
        txtName.setText(item);

        // Xác định icon dựa vào độ dài của text
        if (item.length() <= 3) {
            imgIcon.setImageResource(R.drawable.star); // Thay bằng hình ngôi sao
        } else {
            imgIcon.setImageResource(R.drawable.earth); // Thay bằng hình địa cầu
        }

        return convertView;
    }
}
