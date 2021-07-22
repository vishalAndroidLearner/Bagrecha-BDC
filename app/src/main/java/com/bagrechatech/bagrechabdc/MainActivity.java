package com.bagrechatech.bagrechabdc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean calledAlready = false;

    private DrawerLayout drawer;
    private NavigationView navigationView;

//    private Spinner stationSpinner;
//    private Button sendButton;
//
//    private ArrayList<String> stationsList;
//    private ArrayAdapter stationListAdapter;
//
//    //Firebase Variables
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference stationsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_BagrechaBDC);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.contentFrame, new HomeFragment());
        fragmentTransaction.commit();


//
//        stationSpinner = findViewById(R.id.station_spinner);
//        sendButton = findViewById(R.id.sendButton);
//
//        // Initialize firebase variables
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        stationsReference = firebaseDatabase.getReference().child("Stations");
//
//        stationsList = new ArrayList<>();
//        stationListAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,stationsList);
//        stationSpinner.setAdapter(stationListAdapter);
//        stationListAdapter.notifyDataSetChanged();
//
//        stationsReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                stationsList.clear();
//
//                for (DataSnapshot childSnapshot :
//                        snapshot.getChildren()) {
//                    stationsList.add(childSnapshot.getValue(String.class));
//                }
//
//                stationListAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String stationName = stationSpinner.getSelectedItem().toString();
//                Toast.makeText(MainActivity.this,stationName, Toast.LENGTH_SHORT).show();
//            }
//        });


    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_customer_list:
                fragment = new CustomerListFragment();
                break;
            default:
                fragment = new CustomizeFormFragment();
        }
        setFragment(fragment);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    OnBackPressed
//    @Override
//    public void onBackPressed() {
//        if(drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompat.START);
//        }else{
//            if(currentFragment == HOME_FRAGMENT){
//                super.onBackPressed();
//            }else{
//                appLogo.setVisibility(View.VISIBLE);
//                invalidateOptionsMenu();
//                setFragment(new HomeFragment(), HOME_FRAGMENT);
//                navigationView.getMenu().getItem(0).setChecked(true);
//            }
//        }
//    }
}