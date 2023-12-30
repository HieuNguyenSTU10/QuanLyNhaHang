package com.example.quanlynhahang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabThemNhaHang;
    ListView lvNhaHang;
    ArrayList<NhaHang> listNhaHang;
    NhaHangAdapter nhaHangAdapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference nhaHang = databaseReference.child("nhaHang");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        fabThemNhaHang = findViewById(R.id.fabThemNhaHang);
        lvNhaHang = findViewById(R.id.lvNhaHang);
        listNhaHang = new ArrayList<>();
        nhaHangAdapter = new NhaHangAdapter(MainActivity.this,R.layout.lv_item_nha_hang,listNhaHang);
        lvNhaHang.setAdapter(nhaHangAdapter);

        fabThemNhaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent themMoiNhaHang = new Intent(MainActivity.this, ThemMoiNhaHangActivity.class);
                startActivity(themMoiNhaHang);
            }
        });
        docDuLieu();
    }


    // Bắt kết quả của các activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        nhaHangAdapter.notifyDataSetChanged();
    }

    // Đọc dữ liệu từ FireBase rồi lưu vào list nhà hàng
    private void docDuLieu(){
        nhaHang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNhaHang.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    NhaHang nh = data.getValue(NhaHang.class);
                    DataSnapshot data4 = data.child("danhGia");
                    float sumda=0;
                    float arrda=0;

                    for (DataSnapshot a : data4.getChildren())
                    {
                        sumda+=Float.parseFloat(a.getValue().toString());
                    }
                    arrda = sumda / data4.getChildrenCount();
                    nh.setTb(arrda);

                    ArrayList<monAn> listMonAn = new ArrayList<>();
                    DataSnapshot data1 = data.child("thucDon");

                    for (DataSnapshot data2: data1.getChildren()){
                        monAn ma = data2.getValue(monAn.class);
                        if (ma != null){
                            listMonAn.add(ma);
                        }
                    }
                    DataSnapshot dataCacAnhNhaHang = data.child("cacAnhNhaHang");
                    ArrayList<String> hinhanhs = new ArrayList<>();
                    for (DataSnapshot data3 : dataCacAnhNhaHang.getChildren()){
                        hinhanhs.add(data3.getValue().toString());
                    }

                    nh.setListHinhAnh(hinhanhs);
                    nh.setListMonAn(listMonAn);
                    if (nh != null){
                        listNhaHang.add(nh);
                    }
                }
                nhaHangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}