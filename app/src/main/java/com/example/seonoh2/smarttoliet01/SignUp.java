package com.example.seonoh2.smarttoliet01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.data.SignUpResult;
import com.example.seonoh2.smarttoliet01.manager.NetworkManager;

import java.io.IOException;

import okhttp3.Request;

public class SignUp extends AppCompatActivity {


    public Button btn_SignUp;
    public Button btn_back;
    public EditText et_Id;
    public EditText et_Password;
    public EditText et_Sex;
    public EditText et_CellPhone;
    public EditText et_PasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        btn_SignUp = (Button)findViewById(R.id.btn_SignUp);
        btn_back = (Button)findViewById(R.id.btn_back);
        et_Id = (EditText) findViewById(R.id.et_id);
        et_Sex = (EditText) findViewById(R.id.et_Sex);
        et_CellPhone = (EditText) findViewById(R.id.et_CellPhone);
        et_Password = (EditText) findViewById(R.id.et_Password);
        et_PasswordConfirm = (EditText) findViewById(R.id.et_PasswordConfirm);

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
//                Toast.makeText(SignUp.this, "회원 가입이 완료 되었습니다!", Toast.LENGTH_LONG).show();
//                startActivity(intent);
                finish();

                NetworkManager.getInstance().getUserSignUp(et_Id.getText().toString(), et_Sex.getText().toString(), et_CellPhone.getText().toString(), et_Password.getText().toString(), et_PasswordConfirm.getText().toString(), new NetworkManager.OnResultListener<SignUpResult>() {
                    @Override
                    public void onSuccess(Request request, SignUpResult result) {
                        SignUpResult data = result;
                        Toast.makeText(SignUp.this, "Matto에 가입하신 것을 환영합니다!", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(), Login.class);

//                        finish();

                    }

                    @Override
                    public void onFail(Request request, IOException exception) {

                    }
                });

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });


    }
}
