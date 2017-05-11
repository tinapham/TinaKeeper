package com.example.tina.doanmang_tinakeeper;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.adapter.BackupData;
import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.adapter.Permission;
import com.example.tina.doanmang_tinakeeper.model.Expense;
import com.example.tina.doanmang_tinakeeper.adapter.GsonFileTxt;
import com.example.tina.doanmang_tinakeeper.adapter.ReceiveMail;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment();

        Permission.verifyStoragePermissions(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Creating SendMail object
            ReceiveMail rm = new ReceiveMail(this);
            rm.execute();
            BackupData bd = new BackupData(this);
            bd.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_day) {
            replaceFragmentContent(new DayListFragment());
        } else if (id == R.id.nav_month) {
            replaceFragmentContent(new MonthListFragment());
        } else if (id == R.id.nav_year) {
            replaceFragmentContent(new YearListFragment());
        } else if (id == R.id.nav_send) {
            replaceFragmentContent(new SendMailFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //đặt fragment DayListFragment làm mặc định
    private void initFragment() {
        DayListFragment firstFragment = new DayListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_main, firstFragment);
        ft.commit();
    }

    //thay thế giữa những fragment
    protected void replaceFragmentContent(Fragment fragment) {

        if (fragment != null) {
            FragmentManager fmgr = getSupportFragmentManager();
            FragmentTransaction ft = fmgr.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

    }

    // Khi người dùng Click vào button Cancel.
    public void buttonDataSyncClicked(View view)  {
        Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
    }

}
