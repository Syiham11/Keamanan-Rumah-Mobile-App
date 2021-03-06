package com.keamanan_rumah.sistemkeamananrumah;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SiblingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    Dialog dialBox;

    public static boolean isInFront;
    public static Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;

        setContentView(R.layout.activity_sibling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        String redirect = "";
        Intent i = getIntent();
        redirect = i.getStringExtra("redirect");
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        if(redirect.equals("monitoring")){
            tx.replace(R.id.FrameSibling, new FragmentMonitoring());
        }else
        if(redirect.equals("dashboard")){
            tx.replace(R.id.FrameSibling, new FragmentDashboard());
        }
        tx.commit();
        dialBox = createDialogBox();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dialBox.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sibling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        jalankanFragment(id);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        jalankanFragment(item.getItemId());
        return true;
    }

    public void jalankanFragment(int id){
        fragment = null;
        if (id == R.id.nav_dashboard) {
            fragment = new FragmentDashboard();
        }else if (id == R.id.nav_profile) {
            fragment = new FragmentProfil();
        }else if (id == R.id.nav_ubah_password) {
            fragment = new FragmentUbahPassword();
        }else if (id == R.id.nav_tambah_pengguna) {
            fragment = new FragmentTambahPengguna();
        }else if (id == R.id.nav_daftar_pengguna) {
            fragment = new FragmentDaftarPengguna();
        }else if (id == R.id.nav_monitoring) {
            fragment = new FragmentMonitoring();
        }else if (id == R.id.nav_kelola_perangkat) {
            fragment = new FragmentKelolaPerangkat();
        }else if (id == R.id.nav_open_request) {
            fragment = new FragmentRequestOpen();
        }else if (id == R.id.nav_download_laporan) {
            fragment = new FragmentDownloadReport();
        }else if (id == R.id.actionTentang){
            fragment = new FragmentTentang();
        }else if (id == R.id.actionBantuan){
            fragment = new FragmentBantuan();
        }else if (id == R.id.actionLogout){
            stopService(new Intent(getBaseContext(), BackgroundService.class));
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("KEAMANAN_RUMAH", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(SiblingActivity.this,Login.class);
            startActivity(i);
            finish();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.FrameSibling, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private Dialog createDialogBox(){
        dialBox = new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin akan keluar dari aplikasi ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialBox.dismiss();
                    }
                })
                .create();
        return dialBox;
    }
}
