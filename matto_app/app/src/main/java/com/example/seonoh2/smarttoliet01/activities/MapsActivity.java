package com.example.seonoh2.smarttoliet01.activities;

import static com.example.seonoh2.smarttoliet01.util.MyApplication.context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.toilets.Toilets;
import com.example.seonoh2.smarttoliet01.util.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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

    RetrofitManager.Toilets toilets = new RetrofitManager().getRetrofit().create(RetrofitManager.Toilets.class);
    Call<Toilets> requests = toilets.requests(new SharedPrefUtil(MapsActivity.this).getPreference("access_token"));
    requests.enqueue(new Callback<Toilets>() {
      @Override
      public void onResponse(Call<Toilets> call, Response<Toilets> response) {
        for (int i = 0; i < response.body().getToilets().length; i++) {
          map.addMarker(new MarkerOptions()
              .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
              .position(new LatLng(response.body().getToilets()[i].getLatitude(), response.body().getToilets()[i].getLongitude()))
              .title(response.body().getToilets()[i].getName())
          );
        }
      }

      @Override
      public void onFailure(Call<Toilets> call, Throwable t) {
        Toast.makeText(context, "사용자 인증 정보를 확인할 수 없습니다", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
      }
    });

    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }

    map.setMyLocationEnabled(true);

    MyLocationActivity.LocationResult locationResult = new MyLocationActivity.LocationResult() {
      @Override
      public void gotLocation(Location location) {

        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        Toast.makeText(getApplicationContext(), "오른쪽 상단의 버튼을 클릭하시면 현재 위치를 볼 수 있습니다.", Toast.LENGTH_LONG).show();

      }
    };

    MyLocationActivity myLocationActivity = new MyLocationActivity();
    myLocationActivity.getLocation(getApplicationContext(), locationResult);


  }


}