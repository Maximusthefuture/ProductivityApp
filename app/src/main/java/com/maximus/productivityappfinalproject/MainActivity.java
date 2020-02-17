package com.maximus.productivityappfinalproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        NavigationUI.setupWithNavController(toolbar,navController);
//        NavigationUI.setupActionBarWithNavController(this, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.fragment_add_app) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.app_detail_fragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.ignore_list_fragment){
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

//        checkForPermission(this);
//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
}
