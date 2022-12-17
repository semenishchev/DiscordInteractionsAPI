package me.mrfunny.interactionapi.commands.slash;

import me.mrfunny.interactionapi.internal.Command;

import java.util.Collection;
import java.util.List;

public interface SlashCommand extends Command {
    default String description() {
        return "No description provided";
    }
    default Collection<SubcommandGroup> subcommands() {
        return List.of();
    }
}
