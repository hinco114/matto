package com.example.seonoh2.smarttoliet01.toilet;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.seonoh2.smarttoliet01.R;
import com.example.seonoh2.smarttoliet01.data.Product;

/**
 * Created by 선오 on 2016-09-25.
 */
public class ToiletRecyclerViewHolder extends RecyclerView.ViewHolder {
    Product item;
    TextView title;
    TextView sub_title;
    TextView price;
    TextView count;
    AppCompatButton purchase;

    public ToiletRecyclerViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        purchase = (AppCompatButton) itemView.findViewById(R.id.purchase);
        price = (TextView) itemView.findViewById(R.id.price);
        sub_title = (TextView) itemView.findViewById(R.id.sub_title);
        count = (TextView) itemView.findViewById(R.id.count);
    }

    public void setData(Product i) {
        item = i;
        title.setText(item.getName());
        price.setText(item.getPrice());
        sub_title.setText(item.getImgSrc());
        count.setText(item.getDescription());




    }


}
