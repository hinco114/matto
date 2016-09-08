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
    public EditText et_SecondPassword;


//    public final static String SEONOH_TAG = "SEONOH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        btn_SignUp = (Button) findViewById(R.id.btn_SignUp);
        btn_back = (Button) findViewById(R.id.btn_back);
        et_Id = (EditText) findViewById(R.id.et_Id);

        et_Sex = (EditText) findViewById(R.id.et_Sex); //gender
        et_CellPhone = (EditText) findViewById(R.id.et_CellPhone);
        et_Password = (EditText) findViewById(R.id.et_Password);
        et_SecondPassword= (EditText) findViewById(R.id.et_SecondPassword); //ndPwd 2차비밀번호


//        String Id = et_Id.getText().toString();
//        String Sex = et_Sex.getText().toString();
//        String CellPhone = et_CellPhone.getText().toString();
//        String Password = et_Password.getText().toString();
//        String PasswordConfirm = et_PasswordConfirm.getText().toString();

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

                NetworkManager.getInstance().getUserSignUp(et_Id.getText().toString(),et_Password.getText().toString(), et_SecondPassword.getText().toString(), et_Sex.getText().toString(), et_CellPhone.getText().toString(),  new NetworkManager.OnResultListener<SignUpResult>() {
                    @Override
                    public void onSuccess(Request request, SignUpResult result) {
                        SignUpResult data = result;
//                        Toast.makeText(SignUp.this, data.getStatus().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(SignUp.this, "Matto 회원가입을 축하드립니다\n"+  et_Id.getText().toString()+"님", Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onFail(Request request, IOException exception) {
                        Toast.makeText(SignUp.this,"전송 실패", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });


//                NetworkManager.getInstance().getUserSignUp(Id, Sex, CellPhone, Password, PasswordConfirm, new NetworkManager.OnResultListener<SignUpResult>() {
////                NetworkManager.getInstance().getUserSignUp(et_Id.getText().toString(), et_Sex.getText().toString(), et_CellPhone.getText().toString(), et_Password.getText().toString(), et_PasswordConfirm.getText().toString(), new NetworkManager.OnResultListener<SignUpResult>() {
//                    @Override
//                    public void onSuccess(Request request, SignUpResult result) {
//                        SignUpResult data = result;
//                        Toast.makeText(SignUp.this, "Matto에 가입하신 것을 환영합니다!", Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(getApplicationContext(), Login.class);
//
////                        finish();
//
//                    }
//
//                    @Override
//                    public void onFail(Request request, IOException exception) {
//
//                    }
//                });
//
//            }
//        });

        btn_back.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });
    }
}







