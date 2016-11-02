package com.example.seonoh2.smarttoliet01.activities;

import static com.example.seonoh2.smarttoliet01.util.MyApplication.context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.beacon.RecoActivity;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.login.LoginCheck;
import com.example.seonoh2.smarttoliet01.model.toilets.NearbyToilets;
import com.example.seonoh2.smarttoliet01.util.SharedPrefUtil;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends RecoActivity implements RECORangingListener,
    NavigationView.OnNavigationItemSelectedListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  private GoogleApiClient googleApiClient;
  ArrayList<RECOBeacon> mRangedBeacons;
  public static final boolean DISCONTINUOUS_SCAN = false;
  private final long FINISH_INTERVAL_TIME = 3000;
  private long backPressedTime = 0;
  public CardView btn_open;
  public CardView btn_buy;
  public CardView btn_report;
  public CardView btn_setting;
  public Button btn_map;
  public ImageView open_icon;
  public TextView open_text;
  public TextView nearby_toilet_name;
  public TextView nearby_toilet_distance;
  public ImageView store_icon;
  public TextView store_text;

  private BluetoothManager mBluetoothManager;
  private BluetoothAdapter mBluetoothAdapter;

  private static final int REQUEST_ENABLE_BT = 1;
  private static final int REQUEST_LOCATION = 10;

  TextView name, id;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    buildGoogleApiClient();

    String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

      new MaterialDialog.Builder(this)
          .title("MATTO 위치 정보 사용 확인")
          .content("MATTO App을 사용하시려면 고객님의 위치 정보 확인이 필요합니다..\n위치 설정 후 다시 로그인해 주세요. \n\n위치 정보 사용을 위해 설정창으로 가시겠습니까?")
          .positiveText("위치 설정 가기")
          .negativeText("취소")
          .onPositive((dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);})
          .onNegative((dialog, which) -> {finish(); })
          .show();
    }

    nearby_toilet_name = (TextView) findViewById(R.id.toilet_name);
    nearby_toilet_distance = (TextView) findViewById(R.id.toilet_distance);

    open_text = (TextView) findViewById(R.id.open_text);
    open_icon = (ImageView) findViewById(R.id.open_icon);
    store_text = (TextView) findViewById(R.id.store_text);
    store_icon = (ImageView) findViewById(R.id.store_icon);

    btn_open = (CardView) findViewById(R.id.btn_open);
    btn_report = (CardView) findViewById(R.id.btn_report);
    btn_buy = (CardView) findViewById(R.id.btn_buy);
    btn_setting = (CardView) findViewById(R.id.btn_setting);
    btn_map = (Button) findViewById(R.id.btn_map);

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    View header = navigationView.getHeaderView(0);
    name = (TextView) header.findViewById(R.id.name);
    id = (TextView) header.findViewById(R.id.id);

    mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    mBluetoothAdapter = mBluetoothManager.getAdapter();

    if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
      Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Log.i("MainActivity", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is not granted.");
        this.requestLocationPermission();
      } else {
        Log.i("MainActivity", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is already granted.");
      }
    }

    if (!canOpen) {
      btn_open.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
      open_icon.setColorFilter(ContextCompat.getColor(this, R.color.colorLight));
      open_text.setTextColor(ContextCompat.getColor(this, R.color.colorLight));

      btn_buy.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
      store_icon.setColorFilter(ContextCompat.getColor(this, R.color.colorLight));
      store_text.setTextColor(ContextCompat.getColor(this, R.color.colorLight));
    } else {
      btn_open.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorLight));
      open_icon.setColorFilter(ContextCompat.getColor(this, R.color.colorSeon));
      open_text.setTextColor(Color.WHITE);

      btn_buy.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorLight));
      store_icon.setColorFilter(ContextCompat.getColor(this, R.color.colorSeon2));
      store_text.setTextColor(Color.WHITE);
    }

    btn_open.setOnClickListener(v -> startActivity(new Intent(this, SecurePswCheckActivity.class)));

    btn_map.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
      startActivity(intent);
    });

    btn_report.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
      startActivity(intent);
    });

    btn_buy.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
      startActivity(intent);
    });

    btn_setting.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
      startActivity(intent);
    });


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
      //If the request to turn on bluetooth is denied, the app will be finished.
      //사용자가 블루투스 요청을 허용하지 않았을 경우, 어플리케이션은 종료됩니다.
      finish();
      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case REQUEST_LOCATION: {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
        }
      }
      default:
        break;
    }
  }

  private void requestLocationPermission() {
    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
      return;
    }
  }

  @Override
  public void onBackPressed() {
    long tempTime = System.currentTimeMillis();
    long intervalTime = tempTime - backPressedTime;

    if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
      super.onBackPressed();
    } else {
      backPressedTime = tempTime;
      Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 버튼을 누르시면 \nMATTO App이 종료 됩니다.", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    RetrofitManager.NearbyToilets login = new RetrofitManager().getRetrofit().create(RetrofitManager.NearbyToilets.class);
    Call<NearbyToilets> requests = login.requests(new SharedPrefUtil(this).getPreference("access_token"), (float) location.getLatitude(),(float)  location.getLongitude());
    requests.enqueue(new Callback<NearbyToilets>() {
      @Override
      public void onResponse(Call<NearbyToilets> call, Response<NearbyToilets> response) {
        Log.e("Location Code", "onResponse: " +  response.code());
        if (response.code() == 200) {

          Log.d("Check", "onResponse: " + response.body().getStatus());

          if (response.body().getStatus().equals("S")) {
//            if (response.body().getResultData().getToilets().length > 0) {
//              nearby_toilet_name.setText(response.body().getResultData().getToilets()[0].getName());
//              nearby_toilet_distance.setText((response.body().getResultData().getToilets()[0].getDistance() * 1000) + "m");
//            } else {
//              nearby_toilet_name.setText("검색중...");
//              nearby_toilet_distance.setText("0");
//            }
          }
        } else {
          Toast.makeText(context, "가까운 화장실 불러오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<NearbyToilets> call, Throwable t) {
        Toast.makeText(context, "가까운 화장실 불러오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  protected synchronized void buildGoogleApiClient() {
    googleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
    googleApiClient.connect();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    requestLocation();
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  LocationRequest locationRequest;
  public void requestLocation() {
    locationRequest = new LocationRequest();
    locationRequest.setInterval(1000); // 위치 갱신 속도 1초
    locationRequest.setFastestInterval(1000); // 위치 최소 갱신 속도 1초
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  //

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_info_edit) {

      startActivity(new Intent(this, EditActivity.class));

      // 회원 정보 수정

    } else if (id == R.id.nav_logout) {

      new SharedPrefUtil(this).removePreference("access_token");
      startActivity(new Intent(this, MainActivity.class));
      finish();

    } else if (id == R.id.nav_notice) {

      startActivity(new Intent(this, NoticeActivity.class));
      // 공지사항

    } else if (id == R.id.nav_members) {

      final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
        @Override
        public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {

        }
      });

      adapter.add(new MaterialSimpleListItem.Builder(this)
          .content("박미화 멘토님")
          .iconPadding(20)
          .icon(getResources().getDrawable(R.drawable.ic_person))
          .backgroundColor(getResources().getColor(R.color.colorSeon3))
          .build());
      adapter.add(new MaterialSimpleListItem.Builder(this)
          .content("윤상현")
          .iconPadding(20)
          .icon(getResources().getDrawable(R.drawable.ic_memory_black_24dp))
          .backgroundColor(getResources().getColor(R.color.colorSeon4))
          .build());
      adapter.add(new MaterialSimpleListItem.Builder(this)
          .content("최한규")
          .iconPadding(20)
          .icon(getResources().getDrawable(R.drawable.ic_device_hub_black_24dp))
          .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
          .build());
      adapter.add(new MaterialSimpleListItem.Builder(this)
          .content("박현식")
          .iconPadding(20)
          .icon(getResources().getDrawable(R.drawable.ic_device_hub_black_24dp))
          .backgroundColor(getResources().getColor(R.color.colorPrimaryDark))
          .build());
      adapter.add(new MaterialSimpleListItem.Builder(this)
          .content("엄선오")
          .iconPadding(20)
          .icon(getResources().getDrawable(R.drawable.ic_android_black_24dp))
          .backgroundColor(getResources().getColor(R.color.colorSeon))
          .build());
      adapter.add(new MaterialSimpleListItem.Builder(this)
          .content("박현기")
          .iconPadding(20)
          .icon(getResources().getDrawable(R.drawable.ic_palette_black_24dp))
          .backgroundColor(getResources().getColor(R.color.colorAccent))
          .build());
      new MaterialDialog.Builder(this)
          .title("참가 멤버")
          .adapter(adapter, null)
          .show();
      // 참가 멤버 소개

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    RetrofitManager.LoginCheck loginCheck = new RetrofitManager().getRetrofit().create(RetrofitManager.LoginCheck.class);
    Call<LoginCheck> check = loginCheck.requests(new SharedPrefUtil(this).getPreference("access_token"));
    check.enqueue(new Callback<LoginCheck>() {
      @Override
      public void onResponse(Call<LoginCheck> call, Response<LoginCheck> response) {
        if (response.code() == 200) {
          name.setText(response.body().getInfo().getId());
          id.setText(response.body().getInfo().getName());
        } else {
          startActivity(new Intent(MainActivity.this, LoginActivity.class));
          finish();
        }
      }

      @Override
      public void onFailure(Call<LoginCheck> call, Throwable t) {
        Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
      }
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (locationRequest != null) {
      locationRequest = null;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.stop(mRegions);
    this.unbind();
    if (locationRequest != null) {
      locationRequest = null;
    }
  }

  boolean canOpen = true;

  public void onRangeBeacons(Collection<RECOBeacon> recoBeacons) {
    if (mRecoManager.isBound()) {

      updateAllBeacons(mRangedBeacons);

      mRangedBeacons = new ArrayList<>(recoBeacons);

      Log.e("SharedPrefUtil", "onRangeBacons: " + mRangedBeacons.size());

      if (mRangedBeacons.size() == 2) {

        RECOBeacon recoBeacon1 = mRangedBeacons.get(0);
        RECOBeacon recoBeacon2 = mRangedBeacons.get(1);

        String proximityUuid1 = recoBeacon1.getProximityUuid();
        String proximityUuid2 = recoBeacon2.getProximityUuid();

        if (proximityUuid1.equals(/* 서버에서 가져온 UUID */ "") && proximityUuid2.equals(/* 서버에서 가져온 UUID */"")) {
          canOpen = true;
          btn_open.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorLight));
          open_icon.setColorFilter(ContextCompat.getColor(this, R.color.colorSeon));
          open_text.setTextColor(Color.WHITE);
        }

      }
    }
  }

  public void updateAllBeacons(Collection<RECOBeacon> beacons) {
    synchronized (beacons) {
      mRangedBeacons = new ArrayList<>(beacons);
    }
  }

  public void updateBeacon(RECOBeacon beacon) {
    synchronized (mRangedBeacons) {
      if (mRangedBeacons.contains(beacon)) {
        mRangedBeacons.remove(beacon);
      }
      mRangedBeacons.add(beacon);
    }
  }

  private void unbind() {
    try {
      mRecoManager.unbind();
    } catch (RemoteException e) {
      Log.i("RECORangingActivity", "Remote Exception");
      e.printStackTrace();
    }
  }

  @Override
  public void onServiceConnect() {
    Log.e("RECORangingActivity", "onServiceConnect()");
    mRecoManager.setDiscontinuousScan(MainActivity.DISCONTINUOUS_SCAN);
    this.start(mRegions);
  }

  @Override
  public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
    Log.e("RECORangingActivity", "didRangeBeaconsInRegion() region: " + recoRegion.getUniqueIdentifier() + ", number of beacons ranged: " + recoBeacons.size());

    updateAllBeacons(recoBeacons);
    //Write the code when the beacons in the region is received
    onRangeBeacons(recoBeacons);
  }

  @Override
  protected void start(ArrayList<RECOBeaconRegion> regions) {

    for (RECOBeaconRegion region : regions) {
      try {
        mRecoManager.startRangingBeaconsInRegion(region);
      } catch (RemoteException e) {
        Log.e("RECORangingActivity", "Remote Exception");
        e.printStackTrace();
      } catch (NullPointerException e) {
        Log.e("RECORangingActivity", "Null Pointer Exception");
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void stop(ArrayList<RECOBeaconRegion> regions) {
    for (RECOBeaconRegion region : regions) {
      try {
        mRecoManager.stopRangingBeaconsInRegion(region);
      } catch (RemoteException e) {
        Log.i("RECORangingActivity", "Remote Exception");
        e.printStackTrace();
      } catch (NullPointerException e) {
        Log.i("RECORangingActivity", "Null Pointer Exception");
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onServiceFail(RECOErrorCode errorCode) {
    //Write the code when the RECOBeaconService is failed.
    //See the RECOErrorCode in the documents.
    return;
  }

  @Override
  public void rangingBeaconsDidFailForRegion(RECOBeaconRegion region, RECOErrorCode errorCode) {
    //Write the code when the RECOBeaconService is failed to range beacons in the region.
    //See the RECOErrorCode in the documents.
    return;
  }
}
