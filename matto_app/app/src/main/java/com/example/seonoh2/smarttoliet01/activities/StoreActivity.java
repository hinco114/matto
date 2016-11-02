package com.example.seonoh2.smarttoliet01.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.products.Request;
import com.example.seonoh2.smarttoliet01.store.StoreRecyclerViewAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {
	StoreRecyclerViewAdapter storeRecyclerViewAdapter;
	RecyclerView recyclerView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

		RetrofitManager.Products products = new RetrofitManager().getRetrofit().create(RetrofitManager.Products.class);
		Call<Request> requests = products.requests(6);
		requests.enqueue(new Callback<Request>() {
			@Override
			public void onResponse(Call<Request> call, Response<Request> response) {
				if (response.code() == 200) {
					storeRecyclerViewAdapter = new StoreRecyclerViewAdapter(StoreActivity.this, response.body().getResultData().getProducts());
					recyclerView.setHasFixedSize(true);
					recyclerView.setAdapter(storeRecyclerViewAdapter);
					recyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));
				} else {
					Log.e("Response", "onResponse: " + response.code());
				}
			}

			@Override
			public void onFailure(Call<Request> call, Throwable t) {
				t.printStackTrace();
			}

		});

	}

	public void onRefreshRecyclerView() {
		RetrofitManager.Products products = new RetrofitManager().getRetrofit().create(RetrofitManager.Products.class);
		Call<Request> requests = products.requests(6);
		requests.enqueue(new Callback<Request>() {
			@Override
			public void onResponse(Call<Request> call, Response<Request> response) {
				if (response.code() == 200) {
					storeRecyclerViewAdapter = new StoreRecyclerViewAdapter(StoreActivity.this, response.body().getResultData().getProducts());
					recyclerView.setHasFixedSize(true);
					recyclerView.setAdapter(storeRecyclerViewAdapter);
					recyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));
				} else {
					Log.e("Response", "onResponse: " + response.code());
				}
			}

			@Override
			public void onFailure(Call<Request> call, Throwable t) {
				t.printStackTrace();
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
