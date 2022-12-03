package me.mrfunny.interactionapi;

import me.mrfunny.interactionapi.internal.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;

public interface CommandManager {

    void registerCommand(Command commandInstance);
    boolean processCommandInteraction(SlashCommandInteractionEvent event);
    boolean processContextInteraction(UserContextInteractionEvent event);
    boolean processContextInteraction(MessageContextInteractionEvent event);

    static CommandManager manage(JDA jda) {
        return new CommandManagerImpl(jda);
    }
}
