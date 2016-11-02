package com.example.seonoh2.smarttoliet01.toilet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.data.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tylenol on 2016. 9. 17..
 */
public class ToiletRecyclerViewAdapter extends RecyclerView.Adapter<ToiletRecyclerViewHolder> {

	private Context context;
	List<Product> items = new ArrayList<>();


	public void clear(){
		items.clear();
		notifyDataSetChanged();
	}
	public void addAll(List<Product> i){
		items.addAll(i);
		notifyDataSetChanged();
	}

	@Override
	public ToiletRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view, parent, false);
		return  new ToiletRecyclerViewHolder(view);
	}
	@Override
	public void onBindViewHolder(final ToiletRecyclerViewHolder holder, final int position) {

		holder.setData(items.get(position));
		holder.purchase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(holder.itemView.getContext(),  (position+1)+ "번째\n 화장실 정보 조회 성공", Toast.LENGTH_SHORT).show();
			}
		});
	}
	@Override
	public int getItemCount() {
		return items.size();
	}
}