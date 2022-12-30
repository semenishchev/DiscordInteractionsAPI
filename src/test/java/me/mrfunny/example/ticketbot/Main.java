package me.mrfunny.example.ticketbot;

import me.mrfunny.example.ticketbot.bot.Bot;
import me.mrfunny.example.ticketbot.bot.commands.ClosureReason;
import me.mrfunny.example.ticketbot.bot.permissions.PermissionManager;
import me.mrfunny.example.ticketbot.db.MongoDbStorage;
import me.mrfunny.example.ticketbot.db.Storage;
import me.mrfunny.example.ticketbot.util.ConstantPool;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static DataObject config;
    public static DataObject messages;
    public static DataObject settings;
    public static Bot bot;
    public static Storage db;
    public static void main(String[] args) throws InterruptedException, IOException {
        config = loadConfig();
        if(config == null) {
            System.out.println("This is the first start, please fill up config.json");
            return;
        }
        DataObject connectionData = config.getObject("connections");
        messages = config.getObject("messages");
        settings = config.getObject("settings");
        bot = new Bot(connectionData);
        db = new MongoDbStorage(connectionData);
        System.out.println(ClosureReason.DONE.getClass().getName());
    }

    private static DataObject loadConfig() throws IOException {
        byte[] configBytes;
        try(FileInputStream stream = new FileInputStream("config.json")) {
            configBytes = stream.readAllBytes();
        } catch (FileNotFoundException e) {
            InputStream stream = ConstantPool.CLASS_LOADER.getResourceAsStream("config.json");
            if(stream == null) {
                throw new RuntimeException("Config resource not found");
            }
            configBytes = stream.readAllBytes();
            stream.close();
            Files.write(Path.of("config.json"), configBytes);
            return null;
        }


        return DataObject.fromJson(configBytes);
    }
}
