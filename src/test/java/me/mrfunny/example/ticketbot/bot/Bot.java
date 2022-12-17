package me.mrfunny.example.ticketbot.bot;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.commands.TicketCommand;
import me.mrfunny.example.ticketbot.bot.permissions.PermissionManager;
import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.CommandManagerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Arrays;

public class Bot {
    private final JDA jda;
    private final Components components;
    private final TicketManager ticketManager;
    private final PermissionManager permissionManager;
    private final Guild guild;
    private final TextChannel logsChannel;
    private final CategoryManager categoryManager;
    public Bot(DataObject connectionData) throws InterruptedException {

        this.jda = JDABuilder.create(connectionData.getString("discord-token"), Arrays.asList(GatewayIntent.values())).build();
        this.components = new Components();
        CommandManager manager = CommandManager.manage(jda);
        jda.addEventListener(new CommandManagerAdapter(manager));
        jda.addEventListener(new Listener());
        jda.awaitReady();
        this.guild = jda.getGuildById(Main.settings.getString("guild-id"));
        if(guild == null) {
            throw new RuntimeException("Guild not found");
        }
        this.permissionManager = new PermissionManager(this);
        this.categoryManager = new CategoryManager(this);
        manager.registerCommand(new TicketCommand());
        this.ticketManager = new TicketManager(this);
        this.logsChannel = guild.getChannelById(TextChannel.class, Main.settings.getString("logs-channel"));
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public JDA getJda() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }

    public Components getComponents() {
        return components;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public TextChannel getLogsChannel() {
        return logsChannel;
    }
}
