package com.naiemul.mybazarlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    private TextInputEditText etName, etPhone;
        private MaterialButton btnSave;
        private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);

        // Toolbar setup
                Toolbar toolbar = findViewById(R.id.toolbarUpdate);
               setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }

                etName = findViewById(R.id.etUpdateName);
                etPhone = findViewById(R.id.etUpdatePhone);
                btnSave = findViewById(R.id.btnSaveUpdate);

                // SharedPreferences initialize (Data স্থায়ীভাবে রাখার জন্য)
                sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

                // আগের সেভ করা ডেটা লোড করা
                String savedName = sharedPreferences.getString("name", "");
                String savedPhone = sharedPreferences.getString("phone", "");
               etName.setText(savedName);
                etPhone.setText(savedPhone);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveData();
                    }
                });



    }



    private void saveData() {
                String name = Objects.requireNonNull(etName.getText()).toString().trim();
                String phone = Objects.requireNonNull(etPhone.getText()).toString().trim();

               if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // SharedPreferences এ ডেটা সেভ করা
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("phone", phone);
                editor.apply();

                Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                name.isEmpty();
                phone.isEmpty();

                // আপডেট হয়ে গেলে অটোমেটিক আগের স্ক্রিনে ফিরে যাবে
                finish();
            }

            @Override
            public boolean onSupportNavigateUp() {
                onBackPressed();
                return true;
            }
}