package com.example.quanlynhahang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PhongtoAnh extends AppCompatActivity {
    ImageView imgAnh;
    Button TroLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phongto_anh);

        imgAnh = findViewById(R.id.imgAnh);
        TroLai = findViewById(R.id.TroLai);

        Intent intent = getIntent();
        Bundle  data = intent.getExtras();

        String anh = data.getString("anh");

        Glide.with(PhongtoAnh.this).load(anh).into(imgAnh);


        TroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}