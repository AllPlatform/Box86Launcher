package com.allplatform.box86;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.allplatform.box86.main.About;
import com.allplatform.box86.main.Home;
import com.allplatform.box86.main.Library;
import com.allplatform.box86.main.Setting;
import com.allplatform.box86.main.wine;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Context context;
    DrawerLayout drawerLayout;
    final Activity activity = this;
    public static SharedPreferences setting;
    public static SharedPreferences winescr;
    public static SharedPreferences data;
    String[] WRITEANDREAD = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        CheckDevice();
        Permission();
        DataSetting();
        fragment(new Home());
        Drawer_Navigation();
    }

    private void DataSetting() {
        setting = getSharedPreferences("DataSetting", Context.MODE_PRIVATE);
        winescr = getSharedPreferences("WineScreen", Context.MODE_PRIVATE);
        data    = getSharedPreferences("Data", Context.MODE_PRIVATE);
    }

    private void CheckDevice() {
        if (Build.VERSION.SDK_INT < 28 || Build.VERSION.SDK_INT > 29){
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
            alertdialog.setTitle("You Device Not Support");
            alertdialog.setMessage("Your Device Is Not On The Supported List, Please Follow Me On Youtube Or Github");
            alertdialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                    System.exit(0);
                }
            });

        }
    }

    private void Permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(WRITEANDREAD,10);
        }
    }

    private void Drawer_Navigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.DrOpen, R.string.DrClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home1){
            fragment(new Home());

        }else if (id == R.id.wine) {
            fragment(new wine());

        }else if (id == R.id.library){
            fragment(new Library());

        }else if (id == R.id.setting){
            fragment(new Setting());

        }else if (id == R.id.about){
            fragment(new About());

        }else if (id == R.id.youtube) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/c/AkiraYuki_Official")));
        }else if (id == R.id.discord) {

        }else if (id == R.id.github) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/c/AkiraYuki_Official")));
        } else if (id == R.id.github1) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/termux")));
        }else if (id == R.id.github2) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/ptitSeb/box86")));
        }else if (id == R.id.github3) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/wine-mirror/wine")));
        }else if (id == R.id.github4) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/pelya/xserver-xsdl")));
        }
        drawerLayout.closeDrawer(Gravity.LEFT, true);
        return true;
    }

    private void fragment(Fragment fragments) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.frame, fragments);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{
                activity.finish();
                System.exit(0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, com.termux.app.TermuxService.class));
    }
}
