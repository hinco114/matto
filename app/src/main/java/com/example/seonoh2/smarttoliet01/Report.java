package com.example.seonoh2.smarttoliet01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 선오 on 2016-07-26.
 */
public class Report extends AppCompatActivity {
    public Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btn_send = (Button) findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(Report.this,"불편사항이 전송되었습니다.",Toast.LENGTH_SHORT).show();
                startActivity(intent);

                finish();
            }
        });
    }
}
