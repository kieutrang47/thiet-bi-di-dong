package com.example.nguyenthikieutrang_22642451_module2_bai20;


public class SanPham {
    private String tenSanPham;
    private int hinhAnh;

    public SanPham(String tenSanPham, int hinhAnh) {
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }
}