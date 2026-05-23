package com.naiemul.mybazarlist;

import android.view.LayoutInflater;
import android.view.View; // এই ইমপোর্টটি নিশ্চিত করুন
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<BazarItem> list;
    private OnItemCheckedListener listener;

    public interface OnItemCheckedListener {
        void onChecked(BazarItem item);
    }

    public NoteAdapter(List<BazarItem> list, OnItemCheckedListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // নিশ্চিত করুন note_item নামে আপনার একটি Layout ফাইল আছে
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BazarItem item = list.get(position);
        holder.tvName.setText(item.itemName);

        // ১. প্রথমে লিসেনার নাল (Null) করে দিন যাতে রিসাইকেল হওয়ার সময় আগের আইটেমের লজিক কাজ না করে
        holder.checkBox.setOnCheckedChangeListener(null);

        // ২. ডাটাবেস অনুযায়ী চেক করা থাকলে টিক চিহ্ন দেখাবে, নাহলে চেক বক্স
        if (item.isChecked) {
            holder.checkBox.setVisibility(View.GONE);
            holder.ivDone.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.ivDone.setVisibility(View.GONE);
            holder.checkBox.setChecked(false); // আইটেমটি কেনা হয়নি
        }

        // ৩. চেক বক্সের ক্লিক লজিক
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                item.isChecked = true;
                holder.checkBox.setVisibility(View.GONE);
                holder.ivDone.setVisibility(View.VISIBLE);

                if (listener != null) {
                    listener.onChecked(item); // ডাটাবেস আপডেট করবে
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox checkBox;
        ImageView ivDone;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvNoteItemName);
            checkBox = v.findViewById(R.id.checkBoxNote);
            ivDone = v.findViewById(R.id.ivDone);
        }
    }
}