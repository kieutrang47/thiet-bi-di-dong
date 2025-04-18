package com.example.nguyenthikieutrang_22642451_module2_bai20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SanPhamAdapter extends BaseAdapter {
    private Context context;
    private List<SanPham> sanPhamList;

    public SanPhamAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanPhamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        SanPham sanPham = sanPhamList.get(position);

        ImageView imgSanPham = convertView.findViewById(R.id.imgIcon);
        TextView txtTenSanPham = convertView.findViewById(R.id.txtName);

        imgSanPham.setImageResource(sanPham.getHinhAnh());
        txtTenSanPham.setText(sanPham.getTenSanPham());

        return convertView;
    }
}
