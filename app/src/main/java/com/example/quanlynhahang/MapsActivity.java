package com.example.quanlynhahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    private DatabaseReference nhaHangRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        isConnected();
        map = findViewById(R.id.map);

        // Lấy ID nhà hàng từ Intent
        String nhahangId = getIntent().getStringExtra("nhahang_id");

        // Khởi tạo DatabaseReference cho nhaHang
        nhaHangRef = FirebaseDatabase.getInstance().getReference().child("nhaHang");

        // Khởi tạo Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        // Lấy ID nhà hàng từ Intent
        String nhahangId = getIntent().getStringExtra("nhahang_id");

        // Thực hiện truy vấn đến Firebase để lấy địa chỉ theo ID
        nhaHangRef.child(nhahangId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NhaHang nhaHang = dataSnapshot.getValue(NhaHang.class);

                    if (nhaHang != null) {
                       LatLng nhaHangLatLng = chuyenDiaChiThanhLatLng(nhaHang.diaChiNhaHang);
                        // Thêm đánh dấu cho địa điểm đích
                        gMap.addMarker(new MarkerOptions().position(nhaHangLatLng).title("Nhà hàng"));

                        // Di chuyển camera đến địa điểm đích
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nhaHangLatLng, 15));
                    }
                } else {
                    Log.e("MapsActivity", "Không tìm thấy nhà hàng với ID đã cho");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi từ cơ sở dữ liệu
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
    }

    private LatLng chuyenDiaChiThanhLatLng(String diaChi) {
        Geocoder geocoder = new Geocoder(this);
        try {
            Address address = geocoder.getFromLocationName(diaChi, 1).get(0);
            if (address != null) {
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void isConnected() {
        ConnectivityManager cm
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        cm.registerNetworkCallback
                (
                        builder.build(),
                        new ConnectivityManager.NetworkCallback() {
                            @Override
                            public void onLost(Network network) {
                                Intent intent = new Intent(MapsActivity.this,CheckInternet.class);
                                startActivity(intent);
                            }
                        }

                );
    }
}
