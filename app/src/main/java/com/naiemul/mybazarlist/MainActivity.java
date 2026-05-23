package com.naiemul.mybazarlist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavi=findViewById(R.id.bottomNavi);

        if (savedInstanceState==null){
            lodeFragment(new HomeFragment());
        }

        bottomNavi.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id=item.getItemId();

                    if (id == R.id.home_item) { // হোম আইটেমের আইডি (নিশ্চিত করুন মেনু ফাইলে এটা আছে)
                                        selectedFragment = new HomeFragment();
                                    } else if (id == R.id.note_item) {
                                        selectedFragment = new CrateNoteFragment();
                                    } else if (id == R.id.profile_item) {
                                        selectedFragment = new ProfileFragment();
                                    }else {
                                      selectedFragment=new DashBoardFragment();
                                      }

                       return lodeFragment(selectedFragment);
        });

    }

//===================================================================
    private boolean lodeFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}