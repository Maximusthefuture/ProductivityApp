package com.maximus.productivityappfinalproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maximus.productivityappfinalproject.data.ScreenReceiver;
import com.maximus.productivityappfinalproject.presentation.ui.MinutesLimitDialogFragment;
import com.maximus.productivityappfinalproject.service.CheckAppLaunchService;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;
import com.maximus.productivityappfinalproject.utils.Utils;

public class MainActivity extends AppCompatActivity  {

    private NavController navController;
    private LinearLayout mLinearLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mLinearLayout = findViewById(R.id.linear_lvl);

//        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        toolbar = findViewById(R.id.toolbar);
//        NavigationUI.setupWithNavController(toolbar,navController);
//        NavigationUI.setupActionBarWithNavController(this, navController);

//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            if (destination.getId() == R.id.apps_dest) {
//               toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
//               toolbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0));
//            } else if (destination.getId() == R.id.settings_dest) {
//                toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
//                toolbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0));
//            } else {
//                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
//                toolbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            }
//        });

        checkPermissionForUsageStats();
//        startServiceFromPrefs();
        startService();
    }

    private void checkPermissionForUsageStats() {
        if (!Utils.hasPermission(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private void startService() {
        Intent intent = new Intent(this, CheckAppLaunchService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    //todo Добавить добавление звезд за выполнение норм?
    private void addStartByLvL() {
        //todo через коллбэк?
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_star_24px);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        mLinearLayout.addView(imageView);
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        startServiceFromPrefs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
