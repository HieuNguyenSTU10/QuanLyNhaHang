package com.example.quanlynhahang;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ThemAnhNhaHang extends AppCompatActivity {
    ImageView ivChonAnh;
    Button btnChonAnh, btnThemAnh, btnTroLai;
    ListView lvCacAnhNhaHang;
    ArrayList<String> CacAnhNhaHang;
    CacAnhNhaHangAdapter cacAnhNhaHangAdapter;

    // Firebase + Storage
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nhaHang = databaseReference.child("nhaHang");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_anh_nha_hang);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnThemAnh = findViewById(R.id.btnThemAnh);
        btnTroLai = findViewById(R.id.btnTroLai);
        lvCacAnhNhaHang = findViewById(R.id.lvCacAnhNhaHang);
        ivChonAnh = findViewById(R.id.ivChonAnh);

        CacAnhNhaHang = new ArrayList<>();
        cacAnhNhaHangAdapter = new CacAnhNhaHangAdapter(ThemAnhNhaHang.this,R.layout.lv_cac_anh_nha_hang,CacAnhNhaHang);
        lvCacAnhNhaHang.setAdapter(cacAnhNhaHangAdapter);

        ActivityResultLauncher chonAnhLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        ivChonAnh.setImageURI(o);
                    }
                }
        );

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        NhaHang a = (NhaHang) data.getSerializable("nhahang");
        if (a.getListHinhAnh() != null){
            for (String src : a.getListHinhAnh()){
                CacAnhNhaHang.add(src);
            }
        }
        cacAnhNhaHangAdapter.notifyDataSetChanged();

        // Đoạn xử lý list view
//        lvCacAnhNhaHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ThemAnhNhaHang.this, position + "", Toast.LENGTH_SHORT).show();
//            }
//        });
        lvCacAnhNhaHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder b = new AlertDialog.Builder(ThemAnhNhaHang.this);
                b.setTitle("Xóa ảnh nhà hàng");
                b.setMessage("Bạn có chắc là muốn ảnh khỏi các ảnh mô tả nhà hàng ?");
                b.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                b.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        a.getListHinhAnh().remove(position);
                        CacAnhNhaHang.clear();
                        CacAnhNhaHang.addAll(a.getListHinhAnh());
                        cacAnhNhaHangAdapter.notifyDataSetChanged();
                        nhaHang.child(a.getId().toString()).child("cacAnhNhaHang")
                                .setValue(a.getListHinhAnh()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ThemAnhNhaHang.this,
                                                "Xóa ảnh thành công !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                b.create().show();
                return false;
            }
        });

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonAnhLauncher.launch("image/*");
            }
        });

        btnThemAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = nhaHang.child(a.getId().toString()).child("cacAnhNhaHang").push().getKey().toString();
                StorageReference anhNhaHang  =
                        storageReference.child("anhNhaHang").child(a.getId()).child(id + ".jpg");
                BitmapDrawable bitmapDrawable = (BitmapDrawable) ivChonAnh.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baoStream);
                byte[] imgData = baoStream.toByteArray();
                anhNhaHang.putBytes(imgData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        anhNhaHang.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String linkAnhMonAn = uri.toString();
                                CacAnhNhaHang.add(linkAnhMonAn);
                                ivChonAnh.setImageResource(R.drawable.img);
                                // Set value cho hinh anh
                                nhaHang.child(a.getId().toString())
                                        .child("cacAnhNhaHang").child(id).setValue(linkAnhMonAn).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(ThemAnhNhaHang.this,
                                                            "Thêm ảnh thành công", Toast.LENGTH_SHORT).show();
                                                    CacAnhNhaHang.clear();
                                                    CacAnhNhaHang.add(linkAnhMonAn);
                                                    CacAnhNhaHang.addAll(a.getListHinhAnh());
                                                    cacAnhNhaHangAdapter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(ThemAnhNhaHang.this,
                                                            "Thêm ảnh thất bại, lỗi : " + task.getException().toString(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });
        
        btnTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                Bundle data = new Bundle();
                a.setListHinhAnh(CacAnhNhaHang);
                data.putSerializable("nhahang",a);
                intent1.putExtras(data);
                setResult(103,intent1);
                finish();
            }
        });
    }
}