package com.example.seonoh2.smarttoliet01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.activities.MainActivity;

/**
 * Created by 선오 on 2016-09-19.
 */
public class IntroduceActivity extends AppCompatActivity {
    public Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        btn_back = (Button) findViewById(R.id.btn_back);

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
