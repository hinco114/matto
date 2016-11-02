package com.example.seonoh2.smarttoliet01.store;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.activities.StoreActivity;
import com.example.seonoh2.smarttoliet01.manager.RetrofitManager;
import com.example.seonoh2.smarttoliet01.model.products.Buy;
import com.example.seonoh2.smarttoliet01.model.products.RequestResultDataProducts;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tylenol on 2016. 9. 17..
 */
public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewHolder> {

	private StoreActivity context;
	private RequestResultDataProducts[] requestResultDataProductses;

	public StoreRecyclerViewAdapter(StoreActivity context, RequestResultDataProducts[] requestResultDataProductses) {
		this.context = context;
		this.requestResultDataProductses = requestResultDataProductses;
	}

	@Override
	public StoreRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view, parent, false);
		return  new StoreRecyclerViewHolder(view);
	}
	@Override
	public void onBindViewHolder(final StoreRecyclerViewHolder holder, final int position) {

		holder.title.setText(requestResultDataProductses[position].getName());
		holder.price.setText("￦ " + requestResultDataProductses[position].getPrice());
		holder.sub_title.setText(requestResultDataProductses[position].getDescription());
		holder.count.setText(requestResultDataProductses[position].getStock() + "개 남음");
		Glide.with(context).load(requestResultDataProductses[position].getImgSrc()).into(holder.imageView);


		holder.purchase.setOnClickListener(view -> {
			RetrofitManager.Buy buy = new RetrofitManager().getRetrofit().create(RetrofitManager.Buy.class);
			Call<Buy> requests = buy.requests(6, requestResultDataProductses[position].getProductIdx());
			requests.enqueue(new Callback<Buy>() {
				@Override
				public void onResponse(Call<Buy> call, Response<Buy> response) {
					if (response.code() == 200) {
						if (response.body().getStatus().equals("F")) {
							Toast.makeText(context, "상품 재고가 없습니다. : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();

						} else {
							Toast.makeText(context, "구매했습니다 : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
						}

						context.onRefreshRecyclerView();

					} else {
						Toast.makeText(context, "구매에 실패했습니다. 다시한번 시도해주세요", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Call<Buy> call, Throwable t) {
					Toast.makeText(context, "구매에 실패했습니다.", Toast.LENGTH_SHORT).show();
					t.printStackTrace();
				}
			});
		});
	}
	@Override
	public int getItemCount() {
		return requestResultDataProductses.length;
	}
}