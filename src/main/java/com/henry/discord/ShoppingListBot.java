package com.henry.discord;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShoppingListBot {
    // Where we store our shopping lists data structure
    // use static type so that data is passed across all instances of shoppingLists created
    private static final HashMap<String, List<String>> shoppingLists = new HashMap<>();

    public static void main(String[] args) {
        // Our bot token from Discord
        String token = System.getenv("token");

        // Allows us to access Javacord's API using our token
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // Create slash commands for


    }
}
