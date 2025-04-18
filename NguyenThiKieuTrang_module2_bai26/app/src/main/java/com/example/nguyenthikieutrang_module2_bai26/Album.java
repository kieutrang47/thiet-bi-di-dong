package com.example.nguyenthikieutrang_module2_bai26;


import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
    private int ma;
    private String ten;

    public Album(int ma, String ten) {
        this.ma = ma;
        this.ten = ten;
    }

    protected Album(Parcel in) {
        ma = in.readInt();
        ten = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public int getMa() { return ma; }
    public void setMa(int ma) { this.ma = ma; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ma);
        dest.writeString(ten);
    }

    @Override
    public int describeContents() { return 0; }
}