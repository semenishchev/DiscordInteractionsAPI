package me.mrfunny.example;

import me.mrfunny.example.button.SurveyButton;
import me.mrfunny.example.modal.SurveyModal;
import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.CommandManagerAdapter;
import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.response.Modal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class Main {
    public static SurveyModal surveyModal = new SurveyModal();
    public static Button takeSurvey = new SurveyButton();
    public static void main(String[] args) throws InterruptedException, IOException {
        Properties properties = getProperties();
        if (properties == null) return;

        JDA jda = JDABuilder.createDefault(properties.getProperty("token")).build();
        CommandManager manager = CommandManager.manage(jda);
        jda.addEventListener(new CommandManagerAdapter(manager));
        jda.awaitReady();
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
}