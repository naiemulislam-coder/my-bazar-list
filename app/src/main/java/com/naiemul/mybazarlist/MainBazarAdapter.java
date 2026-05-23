package com.naiemul.mybazarlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainBazarAdapter extends RecyclerView.Adapter<MainBazarAdapter.MyViewHolder> {

    private List<BazarItem> bazarList = new ArrayList<>();
    private OnBazarClickListener listener;

    public interface OnBazarClickListener {
        void onBazarClick(BazarItem item);


        void onDeleteClick(BazarItem item);
        void onAddMoreClick(BazarItem item);
    }

    public MainBazarAdapter(OnBazarClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<BazarItem> list) {
        this.bazarList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bazar_name_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BazarItem item = bazarList.get(position);

        holder.tvTitle.setText(item.bazarName);
        holder.tvDate.setText("📅 " + item.date);
        holder.tvTime.setText("⏰ " + item.time);

        holder.itemView.setOnClickListener(v -> listener.onBazarClick(item));

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));

        holder.btnAddMore.setOnClickListener(v -> listener.onAddMoreClick(item));
    }

    @Override
    public int getItemCount() {
        return bazarList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvTime;
        ImageButton btnDelete;
        ImageView btnAddMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBazarName);
            tvDate = itemView.findViewById(R.id.tvBazarDate);
            tvTime = itemView.findViewById(R.id.tvBazarTime);
            btnDelete = itemView.findViewById(R.id.btnDeleteBazar);
            btnAddMore = itemView.findViewById(R.id.btnAddMore);
        }
    }
}
