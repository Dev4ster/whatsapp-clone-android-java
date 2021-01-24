package com.victormenezes.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.adapter.TabAdapter;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth auth;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.viewPager);
        auth = ConfigFirebase.getFirebaseAuthentication();

        toolbar.setTitle("Meu APP");
        setSupportActionBar(toolbar);

        // config adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter( tabAdapter );
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }

    private void logOutUser(){
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
                return true;
            case R.id.action_logout:
                logOutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
