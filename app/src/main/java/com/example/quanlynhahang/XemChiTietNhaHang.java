package com.example.quanlynhahang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class XemChiTietNhaHang extends AppCompatActivity {
    TextView tvTenNhaHang, tvDiaChiNhaHang, tvEmail, tvSoDienThoai, tvGioMoCua, tvMoTaNhaHang,textrating;
    Button btnTroLai,btnXemAnhNhaHang,btnXemThucDon,btnSuaNhaHang;
    ImageButton GuiEmail,phone,sms,btnShowMap;
    ImageView ivAnhNhaHang;
    RatingBar rating;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nhaHang = databaseReference.child("nhaHang");
    NhaHang a = new NhaHang();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_nha_hang);
        // Ánh xạ
        tvTenNhaHang = findViewById(R.id.tvTenNhaHang);
        tvDiaChiNhaHang = findViewById(R.id.tvDiaChiNhaHang);
        tvEmail = findViewById(R.id.tvEmail);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvGioMoCua = findViewById(R.id.tvGioMoCua);
        tvMoTaNhaHang = findViewById(R.id.tvMoTaNhaHang);
        btnTroLai = findViewById(R.id.btnTroLai);
        btnXemAnhNhaHang = findViewById(R.id.btnXemAnhNhaHang);
        btnXemThucDon = findViewById(R.id.btnXemThucDon);
        btnSuaNhaHang = findViewById(R.id.btnSuaNhaHang);
        ivAnhNhaHang = findViewById(R.id.ivAnhNhaHang);
        btnShowMap = findViewById(R.id.btnShowMap);

        GuiEmail = findViewById(R.id.mail);
        phone = findViewById(R.id.phone);
        sms= findViewById(R.id.sms);
        rating = findViewById(R.id.rating);
        textrating = findViewById(R.id.textrating);



        //hiện dánh giá
        Intent intent = getIntent();
        Bundle data =  intent.getExtras();
        a = (NhaHang) data.getSerializable("nhahang");

        Float rate = (float) Math.ceil(a.getTb() * 10) / 10;
        rating.setRating(rate);
        textrating.setText(rate+"/5");

        // Phan code

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XemChiTietNhaHang.this,MapsActivity.class);
                intent.putExtra("nhahang_id", a.getId());
                startActivity(intent);
            }
        });
        btnTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(100);
                finish();
            }
        });



        tvTenNhaHang.setText(a.getTenNhaHang());
        tvDiaChiNhaHang.setText("Địa chỉ : " + a.getDiaChiNhaHang());
        tvEmail.setText("Email : " + a.getEmail());
        tvSoDienThoai.setText("Số điện thoại : " + a.getSoDienThoai());
        tvGioMoCua.setText("Giờ mở cửa : " + a.getGioMoCua());
        tvMoTaNhaHang.setText("Mô tả nhà hàng : " + a.getMoTaNhaHang());
        Glide.with(XemChiTietNhaHang.this).load(a.getAnhNhaHang()).into(ivAnhNhaHang);

        ivAnhNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XemChiTietNhaHang.this,PhongtoAnh.class);
                Bundle data = new Bundle();
                data.putString("anh", a.getAnhNhaHang());
                intent.putExtras(data);
                startActivity(intent);
            }
        });


        btnXemAnhNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putSerializable("nhahang",a);
                Intent xemAnhNhaHang = new Intent(XemChiTietNhaHang.this,ThemAnhNhaHang.class);
                xemAnhNhaHang.putExtras(data);
                startActivityForResult(xemAnhNhaHang,103);
            }
        });

        btnXemThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putSerializable("nhahang",a);
                Intent xemThucDon = new Intent(XemChiTietNhaHang.this,XemThucDonActivity.class);
                xemThucDon.putExtras(data);
                startActivityForResult(xemThucDon,102);
            }
        });

        btnSuaNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent suaNhaHang = new Intent(XemChiTietNhaHang.this, SuaNhaHang.class);
                Bundle data = new Bundle();
                data.putSerializable("nhahang",a);
                suaNhaHang.putExtras(data);
                startActivityForResult(suaNhaHang,104);
            }
        });

        GuiEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guiemail = new Intent( XemChiTietNhaHang.this, GuiEmail.class);
                Bundle data= new Bundle();
                data.putSerializable("nhahang",a);
                guiemail.putExtras(data);
                startActivityForResult(guiemail, 105);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri phoneuri = Uri.parse("tel: " +a.getSoDienThoai());
                Intent goiDienIntent = new Intent(Intent.ACTION_DIAL,phoneuri); // chỉ chuyển sang màn hình gọi điện
                startActivity(goiDienIntent);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri smsuri = Uri.parse("smsto: " +a.getSoDienThoai());
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO,smsuri);
                startActivity(smsIntent);
            }
        });
    }



    // Doan set result code duh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == 102){
            Bundle dulieu = data.getExtras();
            NhaHang moi = (NhaHang) dulieu.getSerializable("nhahang");
            a.setListMonAn(moi.getListMonAn());
        }

        if (requestCode == 103 && resultCode == 103){
            Bundle dulieu = data.getExtras();
            NhaHang moi = (NhaHang) dulieu.getSerializable("nhahang");
            a.setListHinhAnh(moi.getListHinhAnh());
        }

        if (resultCode == 104 && requestCode == 104){
            Bundle dulieu = data.getExtras();
            NhaHang moi = (NhaHang) dulieu.getSerializable("nhahang");
            tvTenNhaHang.setText(moi.getTenNhaHang());
            tvDiaChiNhaHang.setText("Địa chỉ : " + moi.getDiaChiNhaHang());
            tvEmail.setText("Email : " + moi.getEmail());
            tvSoDienThoai.setText("Số điện thoại : " + moi.getSoDienThoai());
            tvGioMoCua.setText("Giờ mở cửa : " + moi.getGioMoCua());
            tvMoTaNhaHang.setText("Mô tả nhà hàng : " + moi.getMoTaNhaHang());
            Glide.with(XemChiTietNhaHang.this).load(moi.getAnhNhaHang()).into(ivAnhNhaHang);
        }
    }
}