package com.example.seonoh2.smarttoliet01.activities;

import static com.example.seonoh2.smarttoliet01.R.id.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.login.Login;
import com.example.seonoh2.smarttoliet01.model.login.LoginCheck;
import com.example.seonoh2.smarttoliet01.util.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


  private final long FINISH_INTERVAL_TIME = 3000;
  private long backPressedTime = 0;


  AppCompatEditText editText_email, editText_psw;
  LinearLayout btn_login, btn_signup;
  Context context = LoginActivity.this;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    RetrofitManager.LoginCheck loginCheck = new RetrofitManager().getRetrofit().create(RetrofitManager.LoginCheck.class);
    Call<LoginCheck> check = loginCheck.requests(new SharedPrefUtil(this).getPreference("access_token"));
    check.enqueue(new Callback<LoginCheck>() {
      @Override
      public void onResponse(Call<LoginCheck> call, Response<LoginCheck> response) {
        if (response.code() == 200) {
          startActivity(new Intent(context, MainActivity.class));
          finish();
        }
      }

      @Override
      public void onFailure(Call<LoginCheck> call, Throwable t) {
        Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
      }
    });

    editText_email = (AppCompatEditText) findViewById(R.id.type_email);
    editText_psw = (AppCompatEditText) findViewById(R.id.type_psw);
    btn_login = (LinearLayout) findViewById(login);
    btn_signup = (LinearLayout) findViewById(R.id.signup);
    btn_login.setOnClickListener(view -> {

      RetrofitManager.Login login = new RetrofitManager().getRetrofit().create(RetrofitManager.Login.class);
      Call<Login> requests = login.requests(editText_email.getText().toString(), editText_psw.getText().toString());
      requests.enqueue(new Callback<Login>() {
        @Override
        public void onResponse(Call<Login> call, Response<Login> response) {
          if (response.code() == 200) {
            Toast.makeText(context, "로그인에 성공했습니다 : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
            new SharedPrefUtil(LoginActivity.this).setPreference("access_token", response.body().getResultData().getAccess_token());
            startActivity(new Intent(context, MainActivity.class));
            finish();
          } else {
            Toast.makeText(context, "로그인에 실패했습니다. 다시한번 입력해주세요", Toast.LENGTH_SHORT).show();
          }
        }

        @Override
        public void onFailure(Call<Login> call, Throwable t) {
          Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
          t.printStackTrace();
        }
      });
    });

    btn_signup.setOnClickListener(view -> startActivity(new Intent(context, SignUpActivity.class)));

  }

  @Override
  public void onBackPressed() {
    long tempTime = System.currentTimeMillis();
    long intervalTime = tempTime - backPressedTime;

    if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
      super.onBackPressed();
    } else {
      backPressedTime = tempTime;
      Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 버튼을 누르시면 \n MATTO App이 종료 됩니다.", Toast.LENGTH_SHORT).show();
    }
  }
}
