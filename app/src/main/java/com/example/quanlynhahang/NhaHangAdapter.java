package com.example.quanlynhahang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NhaHangAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<NhaHang> listNhaHang;

    public NhaHangAdapter(Activity context, int resource, ArrayList<NhaHang> listNhaHang){
        super(context,resource);
        this.context = context;
        this.resource = resource;
        this.listNhaHang = listNhaHang;
    }

    @Override
    public int getCount() {
        return listNhaHang.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(resource,null);

        ImageView ivAnhNhaHang = customView.findViewById(R.id.ivAnhNhaHang);
        TextView tvTenNhaHang = customView.findViewById(R.id.tvTenNhaHang);
        TextView tvSoDienThoai = customView.findViewById(R.id.tvSoDienThoai);
        TextView tvGioMoCua = customView.findViewById(R.id.tvGioMoCua);
        TextView tvDiaChi = customView.findViewById(R.id.tvDiaChi);

        NhaHang nhaHang = listNhaHang.get(position);

        tvTenNhaHang.setText(nhaHang.getTenNhaHang());
        tvSoDienThoai.setText("Số điện thoại : " + nhaHang.getSoDienThoai());
        tvGioMoCua.setText(nhaHang.getGioMoCua());
        tvDiaChi.setText(nhaHang.getDiaChiNhaHang());

        if (nhaHang.getAnhNhaHang().trim().length() >0){
            Glide.with(context.getBaseContext()).load(nhaHang.getAnhNhaHang()).into(ivAnhNhaHang);
        }

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("nhahang",nhaHang);
                Intent xemchitiet = new Intent(context, XemChiTietNhaHang.class);
                xemchitiet.putExtras(data);
                context.startActivityForResult(xemchitiet,100);
            }
        });

        return customView;
    }
}
