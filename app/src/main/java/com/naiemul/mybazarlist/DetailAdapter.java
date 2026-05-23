package com.naiemul.mybazarlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<BazarItem> items = new ArrayList<>();

    public void setItems(List<BazarItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // এখানে আমরা একটি সাধারণ রো লেআউট ব্যবহার করছি
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BazarItem item = items.get(position);

        holder.tvSl.setText(String.valueOf(position + 1));
        holder.tvName.setText(item.itemName);
        holder.tvQty.setText(item.quantity);
        holder.tvPrice.setText("৳ " + item.price);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSl, tvName, tvQty, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSl = itemView.findViewById(R.id.tvSlDetail);
            tvName = itemView.findViewById(R.id.tvNameDetail);
            tvQty = itemView.findViewById(R.id.tvQtyDetail);
            tvPrice = itemView.findViewById(R.id.tvPriceDetail);
        }
    }
}
