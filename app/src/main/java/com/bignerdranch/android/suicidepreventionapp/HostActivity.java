package com.bignerdranch.android.suicidepreventionapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.bignerdranch.android.suicidepreventionapp.Counselor.SectionsPageAdapter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;

public class HostActivity extends AppCompatActivity {

    SharedPref sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        Socket socket;
        try {
            socket = IO.socket("http://pal.njcuacm.org:443");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket.connect();

        ViewPager myViewPager = findViewById(R.id.container_host);
        setupViewPager(myViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(myViewPager);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.colorText));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentProfile(), "Profile");
        adapter.addFragment(new FormListActivity(), "Forms");
        adapter.addFragment(new JoinChatRoom(), "Chat");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(HostActivity.this, Login.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}