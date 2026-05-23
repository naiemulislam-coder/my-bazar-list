package com.naiemul.mybazarlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;


public class ProfileFragment extends Fragment {

        private TextView tvName, tvEmail; // tvTotalBazar যোগ করা হয়েছে
        private MaterialButton btnEdit;
        private SharedPreferences sharedPreferences;


    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_profile, container, false);

                tvName = view.findViewById(R.id.tvProfileName);
                tvEmail = view.findViewById(R.id.tvProfilePhone);

                btnEdit = view.findViewById(R.id.btnGoToUpdate);

        sharedPreferences = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);

               loadUserData();

        btnEdit.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                       startActivity(intent);
                    });


        return  view;

    }
 //=======
    @Override
        public void onResume() {
            super.onResume();
            loadUserData();
        }

        private void loadUserData() {
            String name = sharedPreferences.getString("name", "Naiemul islam");
            String email = sharedPreferences.getString("email", "naiemul@gmail.com");

            // বাজারের সংখ্যা দেখানোর জন্য (যদি ডাটাবেস থেকে আনতে চান তবে এখানে লজিক লিখবেন)
            // আপাতত একটি ডিফল্ট ভ্যালু দেখাচ্ছি


            tvName.setText(name);
            tvEmail.setText(email);

        }
//=========
}