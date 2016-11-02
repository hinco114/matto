package com.example.seonoh2.smarttoliet01.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.Report;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_report);

    RetrofitManager.Report report = new RetrofitManager().getRetrofit().create(RetrofitManager.Report.class);
    Call<Report> requests = report.requests(6, "");
    requests.enqueue(new Callback<Report>() {
      @Override
      public void onResponse(Call<Report> call, Response<Report> response) {

      }

      @Override
      public void onFailure(Call<Report> call, Throwable t) {

      }
    });

    AppCompatButton appCompatButton = (AppCompatButton) findViewById(R.id.report_button);
    appCompatButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "신고가 접수되었습니다.", Snackbar.LENGTH_SHORT).show();
      }
    });
  }

}
