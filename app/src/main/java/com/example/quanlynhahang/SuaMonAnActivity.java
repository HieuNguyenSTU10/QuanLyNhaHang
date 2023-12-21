package com.example.quanlynhahang;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SuaMonAnActivity extends AppCompatActivity {
    ImageView ivAnhMonAn;
    EditText edtTenMonAn, edtGiaMonAn, edtMoTaMonAn;
    Button btnXacNhan, btnHuyBo,btnChonAnh;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nhaHang = databaseReference.child("nhaHang");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_mon_an);

        // Ánh xạ
        edtTenMonAn = findViewById(R.id.edtTenMonAn);
        edtGiaMonAn = findViewById(R.id.edtGiaMonAn);
        edtMoTaMonAn = findViewById(R.id.edtMoTaMonAn);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnHuyBo = findViewById(R.id.btnHuyBo);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        ivAnhMonAn = findViewById(R.id.ivAnhMonAn);

        // Get gia tri tu itent
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        monAn ma = (monAn) data.getSerializable("monan");
        NhaHang a = (NhaHang) data.getSerializable("nhahang");

        // Activity lay anh

        ActivityResultLauncher chonAnhLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        ivAnhMonAn.setImageURI(o);
                    }
                }
        );


        // Set gia tri
        edtTenMonAn.setText(ma.getTenMonAn());
        int giamonan = (int) ma.getGiaMonAn();
        edtGiaMonAn.setText(giamonan + "");
        edtMoTaMonAn.setText(ma.getMoTaMonAn());
        Glide.with(SuaMonAnActivity.this).load(ma.getHinhAnhMinhHoa()).into(ivAnhMonAn);

        // btn chon anh
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonAnhLauncher.launch("image/*");
            }
        });

//         btnXacNhan sửa
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMonAn = edtTenMonAn.getText().toString();
                Float giaMonAn = Float.parseFloat(edtGiaMonAn.getText().toString());
                String moTaMonAn = edtMoTaMonAn.getText().toString();
                String id = ma.getId();
                monAn moi = new monAn(id,tenMonAn,"",giaMonAn,moTaMonAn);

                StorageReference anhMonAn  =
                        storageReference.child("anhMonAn").child(a.getId().toString())
                                .child( ma.getId().toString() + ".jpg");

                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivAnhMonAn.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baoStream);
                byte[] imgData = baoStream.toByteArray();
                anhMonAn.putBytes(imgData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        anhMonAn.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String linkHinhAnh = uri.toString();
                                moi.setHinhAnhMinhHoa(linkHinhAnh);
                                // Set value anh cho mon an
                                nhaHang.child(a.getId().toString())
                                        .child("thucDon").child(id).setValue(moi).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(SuaMonAnActivity.this,
                                                            "Sửa món ăn thành công", Toast.LENGTH_SHORT).show();
                                                    Intent intent1 = new Intent();
                                                    Bundle data = new Bundle();
                                                    data.putSerializable("monan",moi);
                                                    intent1.putExtras(data);
                                                    setResult(105,intent1);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SuaMonAnActivity.this,
                                                            "Sửa món ăn thất bại, lỗi : " + task.getException().toString(),
                                                            Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        // btnHuyBo
        btnHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}