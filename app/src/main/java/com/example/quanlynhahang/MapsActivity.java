package com.example.quanlynhahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);

        // Khởi tạo Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        // Lấy địa chỉ nhà hàng từ Intent
        String diaChi = getIntent().getStringExtra("diaChi");

        if (diaChi != null) {
            LatLng nhaHangLatLng = chuyenDiaChiThanhLatLng(diaChi);
            // Thêm đánh dấu cho địa điểm đích
            gMap.addMarker(new MarkerOptions().position(nhaHangLatLng).title("Nhà hàng"));

            // Di chuyển camera đến địa điểm đích
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nhaHangLatLng, 15));
      } else
        {
            Log.e("MapsActivity", "Không tìm thấy địa chỉ nhà hàng");
        }
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
}
