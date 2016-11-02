package com.example.seonoh2.smarttoliet01.activities;

import static com.example.seonoh2.smarttoliet01.util.MyApplication.context;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.example.seonoh2.smarttoliet01.model.TaskSignUp;
import com.example.seonoh2.smarttoliet01.model.user.SignUp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurePswActivity extends AppCompatActivity {

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
                                    Toast.makeText(SecurePswActivity.this, "4자리를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (isSecond) {
                                        if (passcode_check.equals(passcode)) {
                                            RetrofitManager.MemberSignUp memberSignUp = new RetrofitManager().getRetrofit().create(RetrofitManager.MemberSignUp.class);
                                            TaskSignUp taskSignUp = new TaskSignUp(id, pwd, passcode_check, phoneNum, gender, "C");
                                            Call<SignUp> signUp = memberSignUp.requests(taskSignUp);
                                            signUp.enqueue(new Callback<SignUp>() {
                                                @Override
                                                public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                                                    if (response.code() == 200) {
                                                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent2);
                                                        Toast.makeText(context, "환영합니다.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "회원가입에 실패했습니다. : " + response.code(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<SignUp> call, Throwable t) {
                                                    Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                    t.printStackTrace();
                                                }
                                            });
                                        }
                                        else Snackbar.make(view, "다시 입력해 주세요.", Snackbar.LENGTH_SHORT).show();

                                    } else {
                                        Snackbar.make(view, "다시 한번 더 입력해 주세요.", Snackbar.LENGTH_SHORT).show();
                                        isSecond = true;
                                        passcode_input.removeAllViews();
                                    }
                                }
                            }
                        }
                    } else {
                        if (passcode_input.getChildCount() != 0) {
                            passcode_input.removeAllViews();

                            if (isSecond) passcode_check = "";
                            else passcode = "";
                        }
                    }
            });
            passcode_container.addView(view);
        }
    }
}
