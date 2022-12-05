package me.mrfunny.interactionapi.commands.slash;

import me.mrfunny.interactionapi.internal.Command;

public interface SlashCommand extends Command {
    default String description() {
        return "No description provided";
    }
}
