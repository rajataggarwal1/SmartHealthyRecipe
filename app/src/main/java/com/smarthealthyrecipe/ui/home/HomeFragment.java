package com.smarthealthyrecipe.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarthealthyrecipe.R;
import com.smarthealthyrecipe.ui.dashboard.DashboardFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment implements DashboardFragment.OnItemAddedListener {
    private static final String PREF_NAME = "ItemPrefs";
    private static final String KEY_ITEM_SET = "itemSet";

    private SharedPreferences sharedPreferences;
    private Set<String> itemSet;
    private List<String> itemList;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        itemSet = sharedPreferences.getStringSet(KEY_ITEM_SET, new HashSet<>());
        itemList = new ArrayList<>(itemSet);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, itemList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

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
}

