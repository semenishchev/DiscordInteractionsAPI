package me.mrfunny.applicationbot;


import me.mrfunny.applicationbot.commands.ApplicationCommand;
import me.mrfunny.applicationbot.components.ApplyModal;
import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.CommandManagerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class Main {
    public static final ApplyModal APPLY_MODAL = new ApplyModal();

    public static void main(String[] args) throws InterruptedException, IOException {
        Properties properties = getProperties();
        if (properties == null) return;

        JDA jda = JDABuilder.createDefault(properties.getProperty("token")).build();
        jda.awaitReady();
        CommandManager manager = CommandManager.manage(jda);
        jda.addEventListener(new CommandManagerAdapter(manager));
        manager.registerCommand(new ApplicationCommand());
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
}
