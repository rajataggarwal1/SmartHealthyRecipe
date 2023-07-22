package com.smarthealthyrecipe;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private static final String PREF_NAME = "ItemPrefs";
    private static final String KEY_ITEM_LIST = "itemList";

    private SharedPreferences sharedPreferences;

    public ItemManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public List<String> getItemList() {
        String itemListJson = sharedPreferences.getString(KEY_ITEM_LIST, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(itemListJson, type);
    }

    public void addItem(String item) {
        List<String> itemList = getItemList();
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        itemList.add(item);

        Gson gson = new Gson();
        String itemListJson = gson.toJson(itemList);

        sharedPreferences.edit().putString(KEY_ITEM_LIST, itemListJson).apply();
    }
}


