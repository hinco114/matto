package com.example.seonoh2.smarttoliet01.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.data.AitemResult;
import com.example.seonoh2.smarttoliet01.manager.NetworkManager;
import com.example.seonoh2.smarttoliet01.toilet.ToiletRecyclerViewAdapter;

import java.io.IOException;

import okhttp3.Request;

public class ToiletActivity extends AppCompatActivity {
	ToiletRecyclerViewAdapter toiletRecyclerViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toilet);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		toiletRecyclerViewAdapter = new ToiletRecyclerViewAdapter();

		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(toiletRecyclerViewAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

	}

	@Override
	protected void onResume() {
		super.onResume();
		setData();
	}

	public void setData(){
		NetworkManager.getInstance().getUserAitem( new NetworkManager.OnResultListener<AitemResult>() {
			@Override
			public void onSuccess(Request request, AitemResult result) {
				toiletRecyclerViewAdapter.clear();
				toiletRecyclerViewAdapter.addAll(result.getProducts());

				//추가 수정 부분


			}
			@Override
			public void onFail(Request request, IOException exception) {
				Toast.makeText(ToiletActivity.this, "전송 실패",Toast.LENGTH_LONG).show();

			}
		});
	}
	//이 부분
}
