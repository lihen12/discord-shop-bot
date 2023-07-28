package com.henry.discord;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;

import java.util.List;

public class ShoppingListBot {
    public static void main(String[] args) {
        String token = "your-bot-token"; // Replace with your bot token

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        ShoppingListManager manager = new ShoppingListManager();

        // Create slash command for creating a new shopping list
        new SlashCommandBuilder()
                .setName("create")
                .setDescription("Create a new shopping list")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "store", "The name of the store", true))
                .createGlobal(api)
                .join();

        // Create slash command for adding an item to a shopping list
        new SlashCommandBuilder()
                .setName("add")
                .setDescription("Add an item to a shopping list")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "store", "The name of the store", true))
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "item", "The item to add", true))
                .createGlobal(api)
                .join();

        // Create slash command for deleting a shopping list
        new SlashCommandBuilder()
                .setName("delete")
                .setDescription("Delete a shopping list")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "store", "The name of the store", true))
                .createGlobal(api)
                .join();

        // Create slash command for deleting an item from a shopping list
        new SlashCommandBuilder()
                .setName("remove")
                .setDescription("Remove an item from a shopping list")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "store", "The name of the store", true))
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "item", "The item to remove", true))
                .createGlobal(api)
                .join();

        // Create slash command for updating an item in a shopping list
        new SlashCommandBuilder()
                .setName("update")
                .setDescription("Update an item in a shopping list")
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "store", "The name of the store", true))
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "oldItem", "The old item", true))
                .addOption(SlashCommandOption.create(SlashCommandOptionType.STRING, "newItem", "The new item", true))
                .createGlobal(api)
                .join();

        // Listen for slash command create
        api.addSlashCommandCreateListener(event -> {
            if (event.getSlashCommandInteraction().getCommandName().equals("create")) {
                String store = event.getSlashCommandInteraction().getOptionByName("store").get().getStringValue().get();

                // Try to create a new shopping list
                if (!manager.createList(store)) {
                    // Send a message if a list for the store already exists
                    new MessageBuilder()
                            .setContent("A shopping list for " + store + " already exists!")
                            .send((TextChannel) event.getSlashCommandInteraction());
                    return;
                }

                // Send a message with the shopping list embed and store the message ID
                event.getSlashCommandInteraction().getChannel().ifPresent(channel -> {
                    if (channel.asServerTextChannel().isPresent()) {
                        ServerTextChannel textChannel = channel.asServerTextChannel().get();
                        manager.setMessageId(store, new MessageBuilder()
                                .setEmbed(manager.getEmbed(store))
                                .send(textChannel)
                                .thenApply(message -> {
                                    // Add an "X" reaction to the message
                                    message.addReaction("❌");
                                    return message.getId();
                                }));
                    }
                });
            }
        });

        // Listen for slash command add
        api.addSlashCommandCreateListener(event -> {
            if (event.getSlashCommandInteraction().getCommandName().equals("add")) {
                String store = event.getSlashCommandInteraction().getOptionByName("store").get().getStringValue().get();
                String item = event.getSlashCommandInteraction().getOptionByName("item").get().getStringValue().get();

                // Try to add an item to the shopping list
                if (!manager.addItem(store, item)) {
                    // Send a message if a list for the store does not exist
                    new MessageBuilder()
                            .setContent("A shopping list for " + store + " does not exist!")
                            .send((TextChannel) event.getSlashCommandInteraction());
                    return;
                }

                // Update the message with the shopping list embed
                event.getSlashCommandInteraction().getChannel().ifPresent(channel -> {
                    if (channel.asServerTextChannel().isPresent()) {
                        ServerTextChannel textChannel = channel.asServerTextChannel().get();
                        long messageId = manager.getMessageId(store).join();
                        textChannel.getMessageById(messageId).thenAccept(message -> message.edit((List<EmbedBuilder>) new MessageBuilder().setEmbed(manager.getEmbed(store))));
                    }
                });
            }
        });

        // Listen for slash command delete
        api.addSlashCommandCreateListener(event -> {
            if (event.getSlashCommandInteraction().getCommandName().equals("delete")) {
                String store = event.getSlashCommandInteraction().getOptionByName("store").get().getStringValue().get();

                // Try to delete the shopping list
                if (!manager.deleteList(store)) {
                    // Send a message if a list for the store does not exist
                    new MessageBuilder()
                            .setContent("A shopping list for " + store + " does not exist!")
                            .send((TextChannel) event.getSlashCommandInteraction());
                    return;
                }

                // Delete the message with the shopping list embed
                event.getSlashCommandInteraction().getChannel().ifPresent(channel -> {
                    if (channel.asServerTextChannel().isPresent()) {
                        ServerTextChannel textChannel = channel.asServerTextChannel().get();
                        long messageId = manager.getMessageId(store).join();
                        textChannel.getMessageById(messageId).thenAccept(Message::delete);
                    }
                });
            }
        });

        // Listen for slash command remove
        api.addSlashCommandCreateListener(event -> {
            if (event.getSlashCommandInteraction().getCommandName().equals("remove")) {
                String store = event.getSlashCommandInteraction().getOptionByName("store").get().getStringValue().get();
                String item = event.getSlashCommandInteraction().getOptionByName("item").get().getStringValue().get();

                // Try to remove an item from the shopping list
                if (!manager.deleteItem(store, item)) {
                    // Send a message if a list for the store does not exist or the item is not in the list
                    new MessageBuilder()
                            .setContent("A shopping list for " + store + " does not exist or the item is not in the list!")
                            .send((TextChannel) event.getSlashCommandInteraction());
                    return;
                }

                // Update the message with the shopping list embed
                event.getSlashCommandInteraction().getChannel().ifPresent(channel -> {
                    if (channel.asServerTextChannel().isPresent()) {
                        ServerTextChannel textChannel = channel.asServerTextChannel().get();
                        long messageId = manager.getMessageId(store).join();
                        textChannel.getMessageById(messageId).thenAccept(message -> message.edit((List<EmbedBuilder>) new MessageBuilder().setEmbed(manager.getEmbed(store))));
                    }
                });
            }
        });

        // Listen for slash command update
        api.addSlashCommandCreateListener(event -> {
            if (event.getSlashCommandInteraction().getCommandName().equals("update")) {
                String store = event.getSlashCommandInteraction().getOptionByName("store").get().getStringValue().get();
                String oldItem = event.getSlashCommandInteraction().getOptionByName("oldItem").get().getStringValue().get();
                String newItem = event.getSlashCommandInteraction().getOptionByName("newItem").get().getStringValue().get();

                // Try to update an item in the shopping list
                if (!manager.updateItem(store, oldItem, newItem)) {
                    // Send a message if a list for the store does not exist or the old item is not in the list
                    new MessageBuilder()
                            .setContent("A shopping list for " + store + " does not exist or the old item is not in the list!")
                            .send((TextChannel) event.getSlashCommandInteraction());
                    return;
                }

                // Update the message with the shopping list embed
                event.getSlashCommandInteraction().getChannel().ifPresent(channel -> {
                    if (channel.asServerTextChannel().isPresent()) {
                        ServerTextChannel textChannel = channel.asServerTextChannel().get();
                        long messageId = manager.getMessageId(store).join();
                        textChannel.getMessageById(messageId).thenAccept(message -> message.edit((List<EmbedBuilder>) new MessageBuilder().setEmbed(manager.getEmbed(store))));
                    }
                });
            }
        });

        // Listen for reaction add events
        api.addReactionAddListener(event -> {
            // Check if the reaction is an "X" and the user is not the bot itself
            if (event.getEmoji().equalsEmoji("❌") && !event.getUser().get().isYourself()) {
                // Try to delete the shopping list
                String store = event.getMessage().get().getEmbeds().get(0).getTitle().get();
                if (manager.deleteList(store)) {
                    // Delete the message with the shopping list embed
                    event.getMessage().get().delete();
                }
            }
        });
    }
}
