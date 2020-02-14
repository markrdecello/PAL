package com.bignerdranch.android.suicidepreventionapp.Counselor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.suicidepreventionapp.Login;
import com.bignerdranch.android.suicidepreventionapp.R;
import com.bignerdranch.android.suicidepreventionapp.SharedPref;
import com.github.nkzawa.socketio.client.Socket;

public class  TabbedActivity extends AppCompatActivity {
    private Socket socket;
    String username;
    SharedPref sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.colorPrimaryDark));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new CounselorProfile(), "Profile");
        adapter.addFragment(new StudentListFragment(), "Students");
        adapter.addFragment(new ViewFormActivity(), "View Forms");
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
                Intent intent = new Intent(TabbedActivity.this, Login.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}