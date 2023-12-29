package com.example.quanlynhahang;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ThemMoiNhaHangActivity extends AppCompatActivity {
    ImageView imgAnhNhaHang;
    Button btnChonAnhNhaHang,btnHuyBo,btnXacNhan,btnThucDon,btnCacHinhAnhNhaHang;
    EditText edtTenNhaHang, edtDiaChiNhaHang, edtEmail , edtSoDienThoai, edtMoTaNhaHang;
    TimePicker tpGioMoCua,tpGioDongCua;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nhaHang = databaseReference.child("nhaHang");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    NhaHang moi = new NhaHang();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = nhaHang.push().getKey().toString();
        setContentView(R.layout.activity_them_moi_nha_hang);
        moi.setId(id);
        System.out.println(id);

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
                StorageReference fileCanXoa = storageReference.child("anhNhaHang")
                        .child(id);
                fileCanXoa.delete();
                StorageReference fileCanXoa1 = storageReference.child("anhDaiDien")
                        .child(id);
                fileCanXoa1.delete();
                StorageReference fileCanXoa2 = storageReference.child("anhMonAn")
                        .child(id);
                fileCanXoa2.delete();
                nhaHang.child(id).removeValue();
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
                    moi.setGioMoCua(gioMoCua);
                    moi.setTenNhaHang(tenNhaHang);
                    moi.setDiaChiNhaHang(diaChiNhaHang);
                    moi.setSoDienThoai(soDienThoai);
                    moi.setEmail(email);
                    moi.setMoTaNhaHang(moTaNhaHang);
                } else {
                    Toast.makeText(ThemMoiNhaHangActivity.this, "Vui lòng nhập đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút thêm các ảnh cho nhà hàng.
        btnCacHinhAnhNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent themMoiNhaHang = new Intent(ThemMoiNhaHangActivity.this, ThemAnhNhaHang.class);
                Bundle data = new Bundle();
                data.putSerializable("nhahang",moi);
                themMoiNhaHang.putExtras(data);
                // Doan nay phai them bundle de gui sang ben kia.
                startActivityForResult(themMoiNhaHang,130);
            }
        });

        // Nút xem thực đơn
        btnThucDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent themMoiNhaHang = new Intent(ThemMoiNhaHangActivity.this, XemThucDonActivity.class);
                Bundle data = new Bundle();
                data.putSerializable("nhahang",moi);
                themMoiNhaHang.putExtras(data);
                // Doan nay phai them bundle de gui sang ben kia.
                startActivityForResult(themMoiNhaHang,129);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 130 && resultCode == 103){
            Bundle dulieu = data.getExtras();
            NhaHang a = (NhaHang) dulieu.getSerializable("nhahang");
            moi.setListHinhAnh(a.getListHinhAnh());
            moi.setId(a.getId());
        }

        if (requestCode == 129 && requestCode == 102){
            Bundle dulieu = data.getExtras();
            NhaHang a = (NhaHang) dulieu.getSerializable("nhahang");
            moi.setListMonAn(a.getListMonAn());
            System.out.println(a.getListMonAn());
            Log.d("monan", a.getListMonAn().toString());
            moi.setId(a.getId());
        }
    }
}