package com.naiemul.mybazarlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


public class HomeFragment extends Fragment {

    private BazarDatabase database;
        private MainBazarAdapter adapter;
        private RecyclerView recyclerView;
        private FloatingActionButton btnAdd;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =  inflater.inflate(R.layout.fragment_home, container, false);

        database = BazarDatabase.getInstance(requireContext());
                recyclerView = view.findViewById(R.id.recyclerView);
                btnAdd = view.findViewById(R.id.btnAdd);

                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                // অ্যাডাপ্টার সেটআপ
                adapter = new MainBazarAdapter(new MainBazarAdapter.OnBazarClickListener() {
                    @Override
                    public void onBazarClick(BazarItem item) {
                        // কার্ডে ক্লিক করলে ভিউ মোড
                        Intent intent = new Intent(getActivity(), DtActivity.class);
                        intent.putExtra("BAZAR_NAME", item.bazarName);
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(BazarItem item) {
                        // ডিলিট লজিক
                        new AlertDialog.Builder(requireContext())
                                .setTitle("ডিলিট!")
                                .setMessage("আপনি কি এই বাজারের সব তথ্য মুছে ফেলতে চান?")
                                .setPositiveButton("হ্যাঁ", (dialog, which) -> {
                                    new Thread(() -> database.bazarDao().deleteBazarByName(item.bazarName)).start();
                                })
                                .setNegativeButton("না", null)
                                .show();
                    }

                    @Override
                    public void onAddMoreClick(BazarItem item) {
                        Intent intent = new Intent(getActivity(), ItemScreenActivity.class);
                        intent.putExtra("BAZAR_NAME", item.bazarName);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);

                // ডাটা অবজার্ভ করা (LiveData)
                database.bazarDao().getUniqueBazarLists().observe(getViewLifecycleOwner(), items -> {
                    adapter.setList(items);
                });

                // নতুন বাজার লিস্ট তৈরির বাটন ক্লিক
                btnAdd.setOnClickListener(v -> showCreateListDialog());
        return view;

    }

    private void showCreateListDialog() {
                // কাস্টম লেআউট ইনফ্লেট করা (add_item.xml)
                View view = LayoutInflater.from(requireContext()).inflate(R.layout.add_item, null);
                TextInputEditText edBazarName = view.findViewById(R.id.edItem);

                new AlertDialog.Builder(requireContext())
                        .setTitle("নতুন বাজার লিস্ট")
                        .setMessage("বাজারের নাম লিখুন ")
                        .setView(view)
                        .setPositiveButton("তৈরি করুন", (dialog, which) -> {
                            String name = edBazarName.getText().toString().trim();

                            if (!name.isEmpty()) {
                                // সরাসরি ItemScreenActivity-তে পাঠিয়ে দেওয়া
                                Intent intent = new Intent(getActivity(), ItemScreenActivity.class);
                                intent.putExtra("BAZAR_NAME", name);
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "নাম খালি রাখা যাবে না!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("বাতিল", null)
                        .show();
            }

}