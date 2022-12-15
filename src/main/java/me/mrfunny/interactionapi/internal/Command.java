package me.mrfunny.interactionapi.internal;

import net.dv8tion.jda.api.entities.Guild;

import java.util.function.Function;

public interface Command {
    String name();
    default boolean isGuildOnly() {
        return true;
    }
    default boolean isGlobal() {
        return true;
    }
    default Function<Guild, Boolean> shouldRegisterToGuild() {
        return (guild) -> true;
    }
}
