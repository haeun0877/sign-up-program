package com.example.project1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ThreeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    String getId, send, receive;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        send = "send";
        receive = "receive";

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent getintent = getIntent();
        getId = getintent.getStringExtra("PersonIn");
        bundle = new Bundle();
        bundle.putString("id", getId);

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ThreeFragment threefragment = new ThreeFragment();
        threefragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment, threefragment);
        fragmentTransaction.commit();

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment).commit();
        toolbar.setTitle("메시지 읽기");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        int id = item.getItemId();
        if(id==R.id.write){
            FourActivity fourfragment = new FourActivity();
            fourfragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment, fourfragment);
            fragmentTransaction.commit();
            toolbar.setTitle("메시지 작성");
        }else if(id==R.id.receive){
            ThreeFragment threefragment = new ThreeFragment();
            bundle.putString("or",receive);
            threefragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment, threefragment);
            fragmentTransaction.commit();
            toolbar.setTitle("받은 메시지 함");
        }else if(id ==R.id.send) {
            ThreeFragment threefragment = new ThreeFragment();
            bundle.putString("or",send);
            threefragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment, threefragment);
            fragmentTransaction.commit();
            toolbar.setTitle("보낸 메시지 함");
        }
        else if(id==R.id.setting){
            FiveActivity fivefragment = new FiveActivity();
            fivefragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment, fivefragment);
            fragmentTransaction.commit();
            toolbar.setTitle("개인 정보 설정");
        }else if(id==R.id.logout){

            moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
