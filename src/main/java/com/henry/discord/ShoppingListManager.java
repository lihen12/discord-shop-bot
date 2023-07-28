package com.henry.discord;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShoppingListManager {
    // Store shopping lists in a HashMap with the store name as the key and the list of items as the value
    private final HashMap<String, List<String>> shoppingLists = new HashMap<>();
    // Store message IDs in a HashMap with the store name as the key and the messageIds as the value
    private final HashMap<String, CompletableFuture<Long>> messageIds = new HashMap<>();

    // Create a new shopping list for a store
    public boolean createList(String store) {
        if (shoppingLists.containsKey(store)) {
            return false; // Return false if a list for the store already exists
        }

        shoppingLists.put(store, new ArrayList<>());
        return true; // Return true if the list was created successfully
    }

    // Add an item to a shopping list for a store
    public boolean addItem(String store, String item) {
        List<String> list = shoppingLists.get(store);
        if (list == null) {
            return false; // Return false if a list for the store does not exist
        }

        list.add(item);
        return true; // Return true if the item as added successfully
    }

}
