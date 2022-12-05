package me.mrfunny.interactionapi.commands.context;

import me.mrfunny.interactionapi.internal.Command;

public interface ContextCommand <T> extends Command {
    net.dv8tion.jda.api.interactions.commands.Command.Type getType();

    void execute(ContextCommandInvocation<T> invocation);
}
