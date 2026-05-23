package com.naiemul.mybazarlist;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemScreenActivity extends AppCompatActivity {

    private String bazarName;
    private BazarDatabase database;
    private BazarAdapter adapter;
    private TextInputEditText etItem, etQty, etPrice;
    private RecyclerView recyclerView;
    private MaterialButton btnItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_screen);

        // ১. ডেটা এবং ডাটাবেস সেটআপ
        bazarName = getIntent().getStringExtra("BAZAR_NAME");
        database = BazarDatabase.getInstance(this);

        // ২. )
        TextView tvTitle = findViewById(R.id.tvDetailsTitle);
        tvTitle.setText(bazarName);

        etItem = findViewById(R.id.etItemName);
        etQty = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        recyclerView = findViewById(R.id.rvDetailsItems);
        btnItem = findViewById(R.id.btnAddItem);

        // ৩. অ্যাডাপ্টার সেটআপ
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BazarAdapter(new BazarAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(BazarItem item) {
                new Thread(() -> database.bazarDao().delete(item)).start();
            }

            @Override
            public void onCheckClick(BazarItem item) {
                new Thread(() -> database.bazarDao().update(item)).start();
            }
        });
        recyclerView.setAdapter(adapter);

        // ৪.  (Live Data)
        database.bazarDao().getItemsByBazarName(bazarName).observe(this, items -> {
            adapter.setItems(items);
        });


        // ৫. আইটেম যোগ করার বাটন ক্লিক লজিক
        btnItem.setOnClickListener(v -> {
            String name = etItem.getText().toString().trim();
            String qty = etQty.getText().toString().trim();
            String priceVal = etPrice.getText().toString().trim();

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            if (name.isEmpty()) {
                etItem.setError("পণ্যর নাম দিন");
                return;
            }

            if (qty.isEmpty()){
                etQty.setError("পণ্যর নাম দিন");
                return;
            }

            if (priceVal.isEmpty()){
                etPrice.setError("পণ্যর নাম দিন");
                return;
            }

            new Thread(() -> {


                BazarItem item = new BazarItem(name, bazarName, qty, priceVal, date, time, false);
                database.bazarDao().insert(item);

                runOnUiThread(() -> {
                    etItem.setText("");
                    etQty.setText("");
                    etPrice.setText("");
                    Toast.makeText(ItemScreenActivity.this, "পণ্য যোগ করা হয়েছে", Toast.LENGTH_SHORT).show();
                });
            }).start();
        });

    }
}