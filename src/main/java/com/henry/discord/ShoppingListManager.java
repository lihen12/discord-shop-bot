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

    // Delete a shopping list for a store
    public boolean deleteList(String store) {
        if (!shoppingLists.containsKey(store)) {
            return false; // Return false if a list for the store does not exist
        }

        shoppingLists.remove(store);
        return true; // Returne true if the list was deleted succesfully
    }

    // Delete an item from a shopping list for a store
    public boolean deleteItem(String store, String item) {
        List<String> list = shoppingLists.get(store);
        if (list == null || !list.contains(item)) {
            return false; // Return false if a list for the store does not exist
        }

        list.remove(item);
        return true; // Return true if the item was deleted successfully
    }

    // Update an item in a shopping list for a store
    public boolean updateItem(String store, String oldItem, String newItem) {
        List<String> list = shoppingLists.get(store);
        if (list == null || !list.contains(oldItem)) {
            return false; // Return false if a list for the store does not exist
        }

        list.set(list.indexOf(oldItem), newItem);
        return true; // Return true if the item was updated successfully
    }

    // Get an embed for a shopping list for a store
    public EmbedBuilder getEmbed(String store) {
        List<String> list = shoppingLists.get(store);
        if (list == null) {
            return null; // Return null if a list for the store does not exist
        }

        // Create an embed with the store name as the title and the items as fields
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(store)
                .setDescription("Shopping List")
                .setColor(Color.GREEN);

        for (String listItem : list) {
            embed.addField(listItem, "");
        }

        return embed;

    }

    // Store the message ID for a shopping list for a store
    public void setMessageId(String store, CompletableFuture<Long> messageId) {
        messageIds.put(store, messageId);
    }

    // Get the message ID for a shopping list for a store
    public CompletableFuture<Long> getMessageId(String store) {
        return messageIds.get(store);
    }
}
