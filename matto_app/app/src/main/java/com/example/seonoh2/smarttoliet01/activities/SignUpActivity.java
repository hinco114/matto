package com.example.seonoh2.smarttoliet01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;

public class SignUpActivity extends AppCompatActivity {


    public LinearLayout done;
    //    public Button btn_back;
    public AppCompatEditText et_Id;
    public AppCompatEditText et_Password;
    public AppCompatSpinner et_Sex;
    public AppCompatEditText et_CellPhone;
    public AppCompatEditText et_SecondPassword;


//    public final static String SEONOH_TAG = "SEONOH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        done = (LinearLayout) findViewById(R.id.done);
        et_Id = (AppCompatEditText) findViewById(R.id.et_Id);

        et_Sex = (AppCompatSpinner) findViewById(R.id.et_Sex); //gender
        et_CellPhone = (AppCompatEditText) findViewById(R.id.et_CellPhone);
        et_Password = (AppCompatEditText) findViewById(R.id.et_Password);
        et_SecondPassword = (AppCompatEditText) findViewById(R.id.et_SecondPassword); //ndPwd 2차비밀번호

        done.setOnClickListener(v -> {

                Intent intent = new Intent(getApplicationContext(), SecurePswActivity.class);

                if (et_Id.getText().toString().equals("") || et_Password.getText().toString().equals("") || et_SecondPassword.getText().toString().equals("") || et_CellPhone.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "정보를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                } else {

                    if (et_Password.getText().toString().equals(et_SecondPassword.getText().toString())) {
                        intent.putExtra("id", et_Id.getText().toString());
                        intent.putExtra("pwd", et_Password.getText().toString());
                        intent.putExtra("gender", et_Sex.getSelectedItem().toString());
                        intent.putExtra("phoneNum", et_CellPhone.getText().toString());
                    } else {
                        Toast.makeText(SignUpActivity.this, "확인 비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                    }

                    startActivity(intent);

                }
        });
    }
}







