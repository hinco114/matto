package com.example.seonoh2.smarttoliet01.store;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seonoh2.smarttoliet01.R;

/**
 * Created by 선오 on 2016-09-25.
 */
public class StoreRecyclerViewHolder extends RecyclerView.ViewHolder {
  TextView title;
  TextView sub_title;
  TextView price;
  TextView count;
  ImageView imageView;
  AppCompatButton purchase;

  public StoreRecyclerViewHolder(View itemView) {
    super(itemView);
    imageView = (ImageView) itemView.findViewById(R.id.img_Src);
    title = (TextView) itemView.findViewById(R.id.title);
    purchase = (AppCompatButton) itemView.findViewById(R.id.purchase);
    price = (TextView) itemView.findViewById(R.id.price);
    sub_title = (TextView) itemView.findViewById(R.id.sub_title);
    count = (TextView) itemView.findViewById(R.id.count);
  }

}