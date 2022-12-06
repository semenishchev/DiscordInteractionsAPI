package me.mrfunny.interactionapi.common;

import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public interface SimpleExecutable {
    default void onExecute(InteractionInvocation invocation, User executor) {}

    default void onExecute(InteractionInvocation invocation, Member executor) {}
}
