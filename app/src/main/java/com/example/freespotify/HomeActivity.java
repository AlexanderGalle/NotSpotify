package com.example.freespotify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.tabs.TabLayout;
import java.util.List;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView username;
        FirebaseAuth auth;
        ShareViewModel viewModel;

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


        auth = FirebaseAuth.getInstance();


        username.setText(auth.getCurrentUser().getDisplayName());



    }



}
