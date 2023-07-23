package com.smarthealthyrecipe.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarthealthyrecipe.MainActivity;
import com.smarthealthyrecipe.R;
import com.smarthealthyrecipe.SharedViewModel;
import com.smarthealthyrecipe.ui.dashboard.DashboardFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class HomeFragment extends Fragment implements DashboardFragment.OnItemAddedListener {

    private SharedViewModel sharedViewModel;
    private static final String PREF_NAME = "ItemPrefs";
    private static final String KEY_ITEM_SET = "itemSet";

    private SharedPreferences sharedPreferences;
    private Set<String> itemSet;
    private List<String> itemList;
    private ArrayAdapter<String> adapter;
    private ListView listView; // Declare listView here

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        itemSet = sharedPreferences.getStringSet(KEY_ITEM_SET, new HashSet<>());
        itemList = new ArrayList<>(itemSet);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, itemList);

        // Note: The sharedViewModel assignment is removed from here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        listView = rootView.findViewById(R.id.listView); // Initialize listView here
        listView.setAdapter(adapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Observe changes in the data and update the UI
        sharedViewModel.getInputData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String text) {
                // Update the UI with the text from the DashboardFragment
                // For example, update a TextView with the received text
                TextView textView = rootView.findViewById(R.id.textView);
                textView.setText(text);
            }
        });

        return rootView;
    }

    public void updateItemList(String newItem) {
        itemSet.add(newItem);
        itemList.add(newItem);
        adapter.notifyDataSetChanged();
        saveItemsToSharedPreferences();
    }

    private void saveItemsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ITEM_SET, itemSet).apply();
    }

    @Override
    public void onItemAdded(String item) {
        itemList.add(item);
        adapter.notifyDataSetChanged();
    }

    // Method to set the SharedViewModel from MainActivity
    public void setSharedViewModel(SharedViewModel viewModel) {
        sharedViewModel = viewModel;
    }
}

