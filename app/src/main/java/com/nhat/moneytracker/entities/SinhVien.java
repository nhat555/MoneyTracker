package com.nhat.moneytracker.entities;

public class SinhVien {
    private String masv;
    private String ten;


    private String email;

    public SinhVien() {
    }

    public SinhVien(String masv, String ten, String email) {
        this.masv = masv;
        this.ten = ten;
        this.email = email;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SinhVien{" +
                "masv='" + masv + '\'' +
                ", ten='" + ten + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
