package me.mrfunny.interactionapi.internal;

import me.mrfunny.interactionapi.internal.wrapper.JdaCommandWrapper;
import me.mrfunny.interactionapi.internal.wrapper.util.ResponseMapper;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;

import java.util.function.Consumer;

public class ComponentInteractionInvocation extends InteractionInvocation{
    private final ComponentInteraction interaction;
    public ComponentInteractionInvocation(IReplyCallback replyCallback) {
        super(replyCallback);
        if(replyCallback instanceof ComponentInteraction componentInteraction) {
            this.interaction = componentInteraction;
        } else throw new RuntimeException("Failed to assign component interaction");
    }

    public ComponentInteractionInvocation ack() {
        this.createAck().complete();
        return this;
    }
    public void ackAsync(Consumer<ComponentInteractionInvocation> after) {
        this.createAck().queue((hook) -> {
            setInteractionHook(hook);
            after.accept(this);
        });
    }

    public ComponentInteractionInvocation editOriginal(MessageContent content) {
        return this;
    }

    public MessageEditCallbackAction createEditOriginal(MessageContent content) {
        MessageEditCallbackAction action = createAck();
        return (MessageEditCallbackAction) ResponseMapper.map(content, action);
    }

    private MessageEditCallbackAction createAck() {
        return this.interaction.deferEdit();
    }

    public MessageChannel getChannel() {
        return this.interaction.getChannel();
    }

    public Guild getGuild() {
        return this.interaction.getGuild();
    }
}
