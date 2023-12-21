package com.example.quanlynhahang;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class NhaHang implements Serializable {
//    ArrayList<String> binhLuan;
//    ArrayList<Double> danhGia;
    String id;
    String diaChiNhaHang;
    String gioMoCua;
    String email;
    String moTaNhaHang;
    String anhNhaHang;
    String soDienThoai;
    String tenNhaHang;
    ArrayList<monAn> listMonAn;
    ArrayList<String> listHinhAnh;

    public NhaHang(){};
//    ArrayList<String> binhLuan, ArrayList<Double> danhGia,
    public NhaHang(String diaChiNhaHang, String gioMoCua, String anhNhaHang, ArrayList<String> listHinhAnh, String id,
                   String email, String moTaNhaHang, String soDienThoai, String tenNhaHang, ArrayList<monAn> listMonAn) {
//        this.binhLuan = binhLuan;
//        this.danhGia = danhGia;
        this.diaChiNhaHang = diaChiNhaHang;
        this.email = email;
        this.moTaNhaHang = moTaNhaHang;
        this.soDienThoai = soDienThoai;
        this.tenNhaHang = tenNhaHang;
        this.listMonAn = listMonAn;
        this.gioMoCua = gioMoCua;
        this.anhNhaHang = anhNhaHang;
        this.listHinhAnh = listHinhAnh;
        this.id = id;
    }

//    public ArrayList<String> getBinhLuan() {
//        return binhLuan;
//    }
//
//    public void setBinhLuan(ArrayList<String> binhLuan) {
//        this.binhLuan = binhLuan;
//    }
//
//    public ArrayList<Double> getDanhGia() {
//        return danhGia;
//    }
//
//    public void setDanhGia(ArrayList<Double> danhGia) {
//        this.danhGia = danhGia;
//    }

    public String getDiaChiNhaHang() {
        return diaChiNhaHang;
    }

    public void setDiaChiNhaHang(String diaChiNhaHang) {
        this.diaChiNhaHang = diaChiNhaHang;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoTaNhaHang() {
        return moTaNhaHang;
    }

    public void setMoTaNhaHang(String moTaNhaHang) {
        this.moTaNhaHang = moTaNhaHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenNhaHang() {
        return tenNhaHang;
    }

    public void setTenNhaHang(String tenNhaHang) {
        this.tenNhaHang = tenNhaHang;
    }

    public ArrayList<monAn> getListMonAn() {
        return listMonAn;
    }

    public void setListMonAn(ArrayList<monAn> listMonAn) {
        this.listMonAn = listMonAn;
    }

    public String getGioMoCua() {
        return gioMoCua;
    }

    public void setGioMoCua(String gioMoCua) {
        this.gioMoCua = gioMoCua;
    }

    public String getAnhNhaHang() {
        return anhNhaHang;
    }

    public void setAnhNhaHang(String anhNhaHang) {
        this.anhNhaHang = anhNhaHang;
    }

    public ArrayList<String> getListHinhAnh() {
        return listHinhAnh;
    }

    public void setListHinhAnh(ArrayList<String> listHinhAnh) {
        this.listHinhAnh = listHinhAnh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
