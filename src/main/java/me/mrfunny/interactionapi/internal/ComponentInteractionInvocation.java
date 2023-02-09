package me.mrfunny.interactionapi.internal;

import me.mrfunny.interactionapi.common.InChannelInvocation;
import me.mrfunny.interactionapi.common.channel.Channels;
import me.mrfunny.interactionapi.internal.wrapper.JdaCommandWrapper;
import me.mrfunny.interactionapi.internal.wrapper.util.ResponseMapper;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;

import java.util.function.Consumer;

public class ComponentInteractionInvocation extends InteractionInvocation implements InChannelInvocation {
    private final ComponentInteraction interaction;
    private boolean acked;

    public ComponentInteractionInvocation(IReplyCallback replyCallback) {
        super(replyCallback);
        if(replyCallback instanceof ComponentInteraction componentInteraction) {
            this.interaction = componentInteraction;
        } else throw new RuntimeException("Failed to assign component interaction");
    }

    public ComponentInteractionInvocation ack() {
        this.possibleMessage = this.interaction.getMessage();
        setInteractionHook(this.createAck().complete());

        return this;
    }
    public void ackAsync(Consumer<ComponentInteractionInvocation> after) {
        this.createAck().queue((hook) -> {
            setInteractionHook(hook);
            after.accept(this);
            this.possibleMessage = this.interaction.getMessage();
        });
    }

    public void editOriginal(MessageContent content) {
        if(replied && acked) {
            createEdit(content).complete();
            return;
        }
        createEditOriginal(content).complete();
    }

    public void editOriginalAsync(MessageContent content) {
        if(replied) {
            Channels.editAsync(possibleMessage.getChannel(), possibleMessage.getId(), content);
            return;
        }
        createEditOriginal(content).queue(h -> interactionHook = h);
    }

    public MessageEditCallbackAction createEditOriginal(MessageContent content) {
        MessageEditCallbackAction action = createAck();
        return (MessageEditCallbackAction) ResponseMapper.map(content, action);
    }

    private MessageEditCallbackAction createAck() {
        this.acked = true;
        return this.interaction.deferEdit();
    }

    @Override
    public MessageChannel getChannel() {
        return this.interaction.getChannel();
    }

    public Guild getGuild() {
        return this.interaction.getGuild();
    }
}
