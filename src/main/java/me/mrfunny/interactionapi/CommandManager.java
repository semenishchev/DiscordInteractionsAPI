package me.mrfunny.interactionapi;

import me.mrfunny.interactionapi.internal.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public interface CommandManager {
    void registerCommand(Command commandInstance);
    boolean processCommandInteraction(SlashCommandInteractionEvent event);
    boolean processContextInteraction(UserContextInteractionEvent event);
    boolean processContextInteraction(MessageContextInteractionEvent event);

    static CommandManager manage(JDA jda) {
        return new CommandManagerImpl(jda);
    }

    boolean processModalInteraction(ModalInteractionEvent event);

    ScheduledExecutorService asyncExecutor = Executors.newScheduledThreadPool(3);

    static ScheduledExecutorService getAsyncExecutor() {
        return asyncExecutor;
    }
}
