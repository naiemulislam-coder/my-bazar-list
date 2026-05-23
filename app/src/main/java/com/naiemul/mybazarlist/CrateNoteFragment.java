package com.naiemul.mybazarlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CrateNoteFragment extends Fragment {

        private EditText etItemName;
        private Button btnAddItem;
        private RecyclerView rvNoteList;
        private NoteAdapter adapter;
        private BazarDatabase db;
    private List<BazarItem> noteList = new ArrayList<>();

    public CrateNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_crate_note, container, false);

                etItemName = v.findViewById(R.id.etItemName);
                rvNoteList = v.findViewById(R.id.rvNoteList);
                btnAddItem = v.findViewById(R.id.btnAddItem);

                db = BazarDatabase.getInstance(getContext());
               rvNoteList.setLayoutManager(new LinearLayoutManager(getContext()));

                // ডাটাবেস থেকে নোটগুলো লোড করা (শুধুমাত্র আজকের বা নির্দিষ্ট লিস্টের জন্য)
                adapter=new NoteAdapter(noteList,item -> {
                    new Thread(() -> db.bazarDao().update(item)).start();
                });

                rvNoteList.setAdapter(adapter);

        // পরিবর্তন এখানে: শুধু 'Note' নামের আইটেমগুলো লোড করবে
        db.bazarDao().getOnlyNotes().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                noteList.clear();
                noteList.addAll(items);
                adapter.notifyDataSetChanged();
            }
        });


        // ৩. নতুন আইটেম সেভ করা
        btnAddItem.setOnClickListener(view -> {
            String name = etItemName.getText().toString().trim();
            if (!name.isEmpty()) {
                // এখানে bazarName হিসেবে "Note" দিচ্ছি যাতে এটি অন্য লিস্টের সাথে না মিশে যায়
                BazarItem newItem = new BazarItem(name, "Note", "", "0", "", "", false);
                new Thread(() -> db.bazarDao().insert(newItem)).start();
                etItemName.setText("");
            } else {
                Toast.makeText(getContext(), "আইটেমের নাম লিখুন", Toast.LENGTH_SHORT).show();
            }
        });


        // ৪. সাইড সোয়াইপ করে ডিলিট করার কোড (Swipe to Delete)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                BazarItem itemToDelete = noteList.get(position);

                // ডাটাবেস থেকে ডিলিট করা
                new Thread(() -> {
                    db.bazarDao().delete(itemToDelete);
                    getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "আইটেমটি ডিলিট করা হয়েছে", Toast.LENGTH_SHORT).show()
                    );
                }).start();
            }
        }).attachToRecyclerView(rvNoteList);

        return v;
    }
}