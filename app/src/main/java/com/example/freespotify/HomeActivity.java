package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

   private TextView username;
   private FirebaseAuth mAuth;
   private ShareViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        List<Fragment> fragments = new Vector<>();
        SongsFragment songsFragment = new SongsFragment();
        ParentPlaylist parentPlaylist = new ParentPlaylist();
        SettingsFragment settingsFragment = new SettingsFragment();

        fragments.add(songsFragment);
        fragments.add(parentPlaylist);
        fragments.add(settingsFragment);

        viewModel =  ViewModelProviders.of(this).get(ShareViewModel.class);
        viewModel.setParent(parentPlaylist);


        PagerAdapter adapter = new PageAdapter(getSupportFragmentManager(), fragments) {
       };

        final ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        username = findViewById(R.id.username);


        mAuth = FirebaseAuth.getInstance();


        username.setText(mAuth.getCurrentUser().getDisplayName());



    }



}
