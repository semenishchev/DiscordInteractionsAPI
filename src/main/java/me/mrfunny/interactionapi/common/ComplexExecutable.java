package me.mrfunny.interactionapi.common;

import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.menus.SelectMenuInvocation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

public interface ComplexExecutable<T extends InteractionInvocation> {
    default void onExecute(T invocation, User executor) {}

    default void onExecute(T invocation, Member executor) {
        this.onExecute(invocation, executor.getUser());
    }
}
