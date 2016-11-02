package com.example.seonoh2.smarttoliet01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;

/**
 * Created by 선오 on 2016-07-26.
 */
public class SettingsActivity extends AppCompatActivity {
    public Button btn_back;
    public Switch alram_switch;
    public Switch autologin_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st);

        btn_back = (Button) findViewById(R.id.btn_back);
        alram_switch = (Switch)findViewById(R.id.switch2);
        autologin_switch = (Switch)findViewById(R.id.switch3);



        alram_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(SettingsActivity.this, "알람 스위치-ON", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SettingsActivity.this, "알람 스위치-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        autologin_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(SettingsActivity.this, "자동로그인 스위치-ON", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SettingsActivity.this, "자동로그인 스위치-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
