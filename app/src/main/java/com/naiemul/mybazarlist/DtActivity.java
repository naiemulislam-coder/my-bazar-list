package com.naiemul.mybazarlist;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DtActivity extends AppCompatActivity {

        private String bazarName;
        private BazarDatabase database;
        private TextView tvTitle, tvTotalAmount;
        private RecyclerView recyclerView;
        private DetailAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dt);

        // ১. নাম রিসিভ করা
            bazarName = getIntent().getStringExtra("BAZAR_NAME");
                database = BazarDatabase.getInstance(this);

                // ২. ভিউ ইনিশিয়ালাইজ করা
               tvTitle = findViewById(R.id.tvBazarTitle);
                tvTotalAmount=findViewById(R.id.tvTotalPrice);
                recyclerView = findViewById(R.id.rvDtItems);

               tvTitle.setText(bazarName);

                // ৩. RecyclerView সেটআপ
               recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter = new DetailAdapter();
                recyclerView.setAdapter(adapter);

                // ৪. ডাটাবেস থেকে ওই নির্দিষ্ট বাজারের সব পণ্য লোড করা
                database.bazarDao().getItemsByBazarName(bazarName).observe(this, items -> {
                    if (items != null) {
                        adapter.setItems(items);
                        calculateTotal(items); // মোট দাম হিসাব করা
                    }
                });
    }



    // মোট দাম হিসাব করার মেথড
         private void calculateTotal(List<BazarItem> items) {
            int total = 0;
            for (BazarItem item : items) {
                try {
                   // দাম যদি খালি না থাকে তবেই যোগ করবে
                    if (item.price != null && !item.price.isEmpty()) {
                        String priceOnly = item.price.replaceAll("[^0-9]", "");
                        if (!priceOnly.isEmpty()){
                            total += Integer.parseInt(item.price);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tvTotalAmount.setText("৳ " + total);
        }


}