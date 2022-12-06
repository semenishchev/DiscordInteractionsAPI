package me.mrfunny.example;

import me.mrfunny.interactionapi.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class Main extends ListenerAdapter {
    private static CommandManager manager;
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties properties = getProperties();
        if (properties == null) return;

        JDA jda = JDABuilder.createDefault(properties.getProperty("token")).build();
        jda.addEventListener(new Main());
        jda.awaitReady();
        manager = CommandManager.manage(jda);
        manager.registerCommand(new ExampleCommand());
        manager.registerCommand(new AnotherExample());
    }

    @Nullable
    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("environment.properties"));
        } catch (FileNotFoundException e) {
            FileWriter writer = new FileWriter("environment.properties");
            Console console = System.console();
            if(console == null) {
                writer.write("token=enteryourtoken");
                writer.close();
                System.out.println("Change your token in environment.properties");
                return null;
            }
            String token = console.readLine("Enter token of your bot");
            writer.write("token=" + token);
            writer.close();
            properties.setProperty("token", token);
        }
        return properties;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(manager.processCommandInteraction(event)) return;
        System.out.println("unknown command");
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
    }
}