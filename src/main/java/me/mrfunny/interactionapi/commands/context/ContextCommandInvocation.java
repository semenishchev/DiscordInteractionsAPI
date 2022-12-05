package me.mrfunny.interactionapi.commands.context;

import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;

public class ContextCommandInvocation <T> extends InteractionInvocation {
    private final GenericContextInteractionEvent<T> castedInteraction;
    public ContextCommandInvocation(GenericContextInteractionEvent<T> replyCallback) {
        super(replyCallback);
        this.castedInteraction = replyCallback;
    }

    public T getTarget() {
        return castedInteraction.getTarget();
    }
}
