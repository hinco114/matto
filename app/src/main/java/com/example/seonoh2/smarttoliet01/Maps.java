package com.example.seonoh2.smarttoliet01;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Maps extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(final GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng seoul = new LatLng(37.5172360, 127.0473250);
        map.addMarker(new MarkerOptions().position(seoul).title("Marker in Seoul 강남"));


        map.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 17));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {



//                String msg = "현재 설정된 위치 정보 lon: "+location.getLongitude()+" -- lat: "+location.getLatitude();
                String msg = "현재 설정된 위치 정보 \n 위도 :  "+location.getLatitude()+"\n"+"경도 : "+location.getLongitude();
//
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "오른쪽 상단의 버튼을 클릭하시면 현재 위치를 볼 수 있습니다.", Toast.LENGTH_LONG).show();

                drawMarker(location,map);

            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);



    }
    public void drawMarker(Location location, GoogleMap map) {

        //기존 마커 지우기
//        map.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());



        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom( currentPosition, 17));
//        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        //마커 추가

        map.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("현재 위치"));
    }




}