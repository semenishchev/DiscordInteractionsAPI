package me.mrfunny.interactionapi.commands;

import me.mrfunny.interactionapi.response.InteractionResponse;
import me.mrfunny.interactionapi.internal.wrapper.util.ResponseMapper;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class SlashCommandInvocation {
    private boolean deferred = false;
    private final SlashCommandInteractionEvent interaction;
    private InteractionHook interactionHook = null;
    private Message possibleMessage = null;
    private boolean replied = false;


    public SlashCommandInvocation(SlashCommandInteractionEvent replyCallback) {
        this.interaction = replyCallback;
    }

    public void defer() {
        this.defer(false, null);
    }

    public void defer(boolean ephemeral) {
        this.defer(ephemeral, null);
    }

    public void defer(Consumer<SlashCommandInvocation> onComplete) {
        this.defer(false, onComplete);
    }

    public void defer(boolean ephemeral, Consumer<SlashCommandInvocation> onComplete) {
        if(deferred) return;
        this.createDefer(ephemeral).queue(hook -> {
            this.deferred = true;
            this.interactionHook = hook;
            if(onComplete != null) {
                onComplete.accept(this);
            }

        });
    }

    public SlashCommandInvocation deferSync() {
        return this.deferSync(false);
    }

    public SlashCommandInvocation deferSync(boolean ephemeral) {
        if(deferred) return this;
        this.interactionHook = createDefer(ephemeral).complete();
        this.deferred = true;
        return this;
    }

    public ReplyCallbackAction createDefer(boolean ephemeral) {
        return this.interaction.deferReply(ephemeral);
    }

    public void send(InteractionResponse response, boolean ephemeral, Consumer<SlashCommandInvocation> messageConsumer) {
        if(response instanceof MessageContent messageContent) {
            if(replied) {
                createEdit(messageContent).queue();
                return;
            }
            if(deferred) {
                ResponseMapper.mapSend(messageContent, this.interactionHook).queue(message -> {
                    this.replied = true;
                    this.possibleMessage = message;
                });
            }
            createSend(messageContent, ephemeral).queue(hook -> {
                setInteractionHook(hook);
                if(messageConsumer != null) {
                    messageConsumer.accept(this);
                }
            });
        } else {

        }
    }

    public void send(InteractionResponse response, boolean ephemeral) {
        this.send(response, ephemeral, null);
    }

    public void send(InteractionResponse response) {
        this.send(response, false, null);
    }

    public SlashCommandInvocation sendSync(InteractionResponse response, boolean ephemeral) {
        if(response instanceof MessageContent messageContent) {
            if(replied) {
                Logger.getGlobal().warning("The interaction is already sent, doing nothing.");
                return this;
            }
            if(deferred) {
                this.possibleMessage = ResponseMapper.mapSend(messageContent, this.interactionHook).complete();
                this.replied = true;
                return this;
            }
            setInteractionHook(createSend(messageContent, ephemeral).complete());
        } else {
            // TODO: handle dialogue
        }


        return this;
    }

    public SlashCommandInvocation sendSync(InteractionResponse response) {
        return this.sendSync(response, false);
    }

    public void edit(MessageContent messageContent, Consumer<Message> consumer) {
        createEdit(messageContent).queue(consumer);
    }

    public Message editSync(MessageContent newContent) {
        return createEdit(newContent).complete();
    }

    public void setInteractionHook(InteractionHook interactionHook) {
        this.replied = true;
        this.interactionHook = interactionHook;
    }

    public InteractionHook getInteractionHook() {
        return interactionHook;
    }

    public Message getPossibleMessage() {
        return possibleMessage;
    }

    public IReplyCallback getInteraction() {
        return interaction;
    }

    @NotNull
    private ReplyCallbackAction createSend(MessageContent content, boolean ephemeral) {
        ReplyCallbackAction callbackAction = interaction.deferReply(ephemeral);
        ResponseMapper.map(content, callbackAction);
        return callbackAction;
    }

    private WebhookMessageEditAction<Message> createEdit(MessageContent newContent) {
        if(!replied) {
            throw new RuntimeException("The interaction can't be edited while not being replied");
        }
        return ResponseMapper.mapEdit(newContent, this.interactionHook, this.possibleMessage.getId());
    }

    public User getUser() {
        return this.interaction.getUser();
    }

    public Member getMember() {
        return this.interaction.getMember();
    }

    public MessageChannelUnion getChannel() {
        return this.interaction.getChannel();
    }

    public String getCommandId() {
        return this.interaction.getCommandId();
    }

    public OffsetDateTime getTimeCreated() {
        return this.interaction.getTimeCreated();
    }

    public ChannelType getChannelType() {
        return this.interaction.getChannelType();
    }

    public Guild getGuild() {
        return this.interaction.getGuild();
    }
}
