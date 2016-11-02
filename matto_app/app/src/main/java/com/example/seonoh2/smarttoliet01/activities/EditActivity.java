package com.example.seonoh2.smarttoliet01.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.widget.LinearLayout;

import com.example.seonoh2.smarttoliet01.R;

public class EditActivity extends AppCompatActivity {


    public LinearLayout done;
    //    public Button btn_back;
    public AppCompatEditText et_Id;
    public AppCompatEditText et_newPassword, et_currentPassword;
    public AppCompatEditText et_CellPhone;
    public AppCompatEditText et_SecondPassword;


//    public final static String SEONOH_TAG = "SEONOH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        done = (LinearLayout) findViewById(R.id.done);
        et_CellPhone = (AppCompatEditText) findViewById(R.id.et_CellPhone);

        et_currentPassword = (AppCompatEditText) findViewById(R.id.et_currentPassword);
        et_newPassword = (AppCompatEditText) findViewById(R.id.et_newPassword);
        et_SecondPassword = (AppCompatEditText) findViewById(R.id.et_SecondPassword); //ndPwd 2차비밀번호

        done.setOnClickListener(v -> {

//            RetrofitManager.MemberEdit memberEdit = new RetrofitManager().getRetrofit().create(RetrofitManager.MemberEdit.class);
//            Call<EditUser> requests = memberEdit.requests(
//                new SharedPrefUtil(this).getPreference("access_token"),
//                et_currentPassword.getText().toString(), et_newPassword.getText().toString());
//            requests.enqueue(new Callback<EditUser>() {
//                @Override
//                public void onResponse(Call<EditUser> call, Response<EditUser> response) {
//                    finish();
//                }
//
//                @Override
//                public void onFailure(Call<EditUser> call, Throwable t) {
//                    Toast.makeText(context, "정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                    finish();
//                    t.printStackTrace();
//                }
//            });
        });
    }
}







