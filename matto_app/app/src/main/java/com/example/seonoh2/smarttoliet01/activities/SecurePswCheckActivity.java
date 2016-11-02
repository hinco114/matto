package com.example.seonoh2.smarttoliet01.activities;

import static com.example.seonoh2.smarttoliet01.util.MyApplication.context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.hardware.OpenDoor;
import com.example.seonoh2.smarttoliet01.util.SharedPrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurePswCheckActivity extends AppCompatActivity {

    GridLayout passcode_container;
    LinearLayout passcode_input;

    String passcode = "", passcode_check = "";
    String id, pwd, gender, phoneNum;


    boolean isSecond = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_psw);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        pwd = intent.getStringExtra("pwd");
        gender = intent.getStringExtra("gender");
        phoneNum = intent.getStringExtra("phoneNum");

        passcode_container = (GridLayout) findViewById(R.id.passcode_container);
        passcode_input = (LinearLayout) findViewById(R.id.passcode_input);

        final LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < 12; i++) {
            View view = inflater.inflate(R.layout.passcode_layout, null);

            final TextView textView = (TextView) view.findViewById(R.id.passcode_item);
            ImageView imageView = (ImageView) view.findViewById(R.id.backspace);

            if (i != 11) {
                if (i != 10) {
                    if (i != 9) {
                        textView.setText(String.valueOf(i + 1));
                    } else {
                        imageView.setImageResource(R.drawable.ic_done_black_24dp);
                        imageView.setVisibility(View.VISIBLE);
                    }
                } else {
                    textView.setText(String.valueOf(0));
                }
            } else {
                imageView.setVisibility(View.VISIBLE);
            }

            final int j = i;

            view.setOnClickListener(view_btn -> {
                    if (j != 11) {
                        if (j != 9) {
                            if (passcode_input.getChildCount() != 4) {
                                View passcode_input_item = inflater.inflate(R.layout.passcode_input_layout, null);
                                passcode_input.addView(passcode_input_item);

                                if (isSecond) passcode_check += textView.getText().toString();
                                else passcode += textView.getText().toString();
                            }
                        } else {
                            if (passcode != null) {
                                if (passcode_input.getChildCount() != 4) {
                                    Toast.makeText(SecurePswCheckActivity.this, "4자리를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    RetrofitManager.OpenDoor openDoor = new RetrofitManager().getRetrofit().create(RetrofitManager.OpenDoor.class);

                                    Call<OpenDoor> openDoorCall = openDoor.requests(new SharedPrefUtil(this).getPreference("access_token"), "41231222", Integer.parseInt(passcode));
                                    openDoorCall.enqueue(new Callback<OpenDoor>() {
                                        @Override
                                        public void onResponse(Call<OpenDoor> call, Response<OpenDoor> response) {
                                            if (response.code() == 200) {
                                                if (response.body().getResultData().getStatus().equals("S")) {
                                                    Toast.makeText(context, "문이 열렸습니다.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(context, "문열기에 실패했습니다. : " + response.body().getReason(), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(context, "문열기에 실패했습니다. : " + response.code(), Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<OpenDoor> call, Throwable t) {
                                            Toast.makeText(context, "문열기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        if (passcode_input.getChildCount() != 0) {
                            passcode_input.removeAllViews();
                            passcode = "";
                        }
                    }
            });
            passcode_container.addView(view);
        }
    }
}
