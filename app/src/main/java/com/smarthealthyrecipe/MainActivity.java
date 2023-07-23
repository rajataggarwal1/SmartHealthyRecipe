package com.smarthealthyrecipe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarthealthyrecipe.databinding.ActivityMainBinding;
import com.smarthealthyrecipe.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Set up AppBarConfiguration
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        // Set up NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Set up ActionBar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Initialize the SharedViewModel
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        // Set the sharedViewModel in the HomeFragment
        Fragment homeFragment = getSupportFragmentManager().findFragmentById(R.id.navigation_home);
        if (homeFragment != null && homeFragment instanceof HomeFragment) {
            ((HomeFragment) homeFragment).setSharedViewModel(sharedViewModel);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    // Add any other methods as needed for your specific app requirements
}
