package com.naiemul.mybazarlist;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BazarAdapter extends RecyclerView.Adapter<BazarAdapter.ViewHolder> {

    private List<BazarItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(BazarItem item);
        void onCheckClick(BazarItem item);
    }

    public BazarAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<BazarItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // নিশ্চিত করুন যে Layout ফাইলের নাম item_row.xml
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(myView);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BazarItem current = items.get(position);

        // ১. সিরিয়াল নম্বর সেট করা
        holder.tvSl.setText(String.valueOf(position + 1));

        // ২. প্রোডাক্টের নাম এবং পরিমাণ সেট করা (সঠিকভাবে)
        holder.tvItemName.setText(current.itemName + " (" + current.quantity + ")");

        // ৩. তারিখ এবং সময় সেট করা
        holder.tvDateTime.setText("📅 " + current.date + " | " + current.time);

        // ৪. দাম সেট করা (Price)
        holder.tvPrices.setText("৳ " + current.price + " taka");

        // ৫. চেক বক্সের অবস্থা সেট করা
        holder.checkBox.setChecked(current.isChecked);

        // ৬. স্ট্রাইক থ্রু (কাটা দাগ) ইফেক্ট হ্যান্ডেল করা
        if (current.isChecked) {
            holder.tvItemName.setPaintFlags(holder.tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvItemName.setTextColor(Color.GRAY); // চেক করা থাকলে রঙ হালকা হবে
        } else {
            holder.tvItemName.setPaintFlags(holder.tvItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvItemName.setTextColor(Color.BLACK); // না থাকলে কালো হবে
        }

        // ৭. ডিলিট বাটন ক্লিক
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(current);
        });

        // ৮. চেক বক্স ক্লিক
        holder.checkBox.setOnClickListener(v -> {
            current.isChecked = holder.checkBox.isChecked();
            if (listener != null) listener.onCheckClick(current);
            notifyItemChanged(position); // ভিউ আপডেট করার জন্য
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvItemName, tvDateTime, tvPrices, tvSl;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkDone);
            tvItemName = itemView.findViewById(R.id.tvItemNameAndQty);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvPrices = itemView.findViewById(R.id.tvPrice);
            tvSl = itemView.findViewById(R.id.tvSL);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}