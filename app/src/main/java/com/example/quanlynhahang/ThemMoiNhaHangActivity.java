package com.example.quanlynhahang;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThemMoiNhaHangActivity extends AppCompatActivity {
    ImageView imgAnhNhaHang;
    Button btnChonAnhNhaHang,btnHuyBo,btnXacNhan,btnThucDon,btnCacHinhAnhNhaHang;
    EditText edtTenNhaHang, edtDiaChiNhaHang, edtEmail , edtSoDienThoai, edtMoTaNhaHang;
    TimePicker tpGioMoCua,tpGioDongCua;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nhaHang = databaseReference.child("NhaHang");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    NhaHang moi = new NhaHang();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_moi_nha_hang);

        // Ánh xạ
        imgAnhNhaHang = findViewById(R.id.imgAnhNhaHang);
        btnChonAnhNhaHang = findViewById(R.id.btnChonAnhNhaHang);
        btnHuyBo = findViewById(R.id.btnHuyBo);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnThucDon = findViewById(R.id.btnThucDon);
        btnCacHinhAnhNhaHang = findViewById(R.id.btnCacHinhAnhNhaHang);
        edtTenNhaHang = findViewById(R.id.edtTenNhaHang);
        edtDiaChiNhaHang = findViewById(R.id.edtDiaChiNhaHang);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtMoTaNhaHang = findViewById(R.id.edtMoTaNhaHang);
        tpGioMoCua = findViewById(R.id.tpGioMoCua);
        tpGioDongCua = findViewById(R.id.tpGioDongCua);

        // Chọn ảnh
        ActivityResultLauncher chonAnhLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        imgAnhNhaHang.setImageURI(o);
                    }
                }
        );

        btnChonAnhNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLauncher.launch("image/*");
            }
        });

        btnHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gioMoCua = tpGioMoCua.getHour() + ":" + tpGioMoCua.getMinute() + " - " + tpGioDongCua.getHour() + ":" + tpGioDongCua.getMinute();
                String tenNhaHang = edtTenNhaHang.getText().toString().trim();
                String diaChiNhaHang = edtDiaChiNhaHang.getText().toString().trim();
                String soDienThoai = edtSoDienThoai.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String moTaNhaHang = edtMoTaNhaHang.getText().toString().trim();
                if (tenNhaHang.length() > 0 && diaChiNhaHang.length() > 0 && soDienThoai.length() > 0 && email.length() > 0){
                    String id = nhaHang.push().getKey().toString();
                }
            }
        });

        // Nút thêm các ảnh cho nhà hàng.
        btnCacHinhAnhNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent themMoiNhaHang = new Intent(ThemMoiNhaHangActivity.this, ThemAnhNhaHang.class);
                NhaHang a = new NhaHang();
                Bundle data = new Bundle();
                data.putSerializable("nhahang",a);
                themMoiNhaHang.putExtras(data);
                // Doan nay phai them bundle de gui sang ben kia.
                startActivityForResult(themMoiNhaHang,106);
            }
        });

        // Nút xem thực đơn
        btnThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 106 && resultCode == 103){
            Bundle dulieu = data.getExtras();
            NhaHang a = (NhaHang) dulieu.getSerializable("nhahang");
            moi.setListHinhAnh(a.getListHinhAnh());
        }
    }
}