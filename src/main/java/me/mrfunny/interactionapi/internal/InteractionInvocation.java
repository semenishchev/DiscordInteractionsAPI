package me.mrfunny.interactionapi.internal;

import me.mrfunny.interactionapi.internal.wrapper.util.ResponseMapper;
import me.mrfunny.interactionapi.response.MessageContent;
import me.mrfunny.interactionapi.response.ModalResponse;
import me.mrfunny.interactionapi.response.interfaces.InteractionResponse;
import me.mrfunny.interactionapi.util.ConsumerUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class InteractionInvocation {
    protected boolean deferred = false;
    protected final IReplyCallback interaction;
    protected InteractionHook interactionHook = null;
    protected Message possibleMessage = null;
    protected boolean replied = false;

    public InteractionInvocation(IReplyCallback replyCallback) {
        this.interaction = replyCallback;
    }
    
    public void deferAsync() {
        this.deferAsync(false, null);
    }
    
    public void deferAsync(boolean ephemeral) {
        this.deferAsync(ephemeral, null);
    }

    
    public void deferAsync(Consumer<InteractionInvocation> onComplete) {
        this.deferAsync(false, onComplete);
    }

    public void deferAsync(boolean ephemeral, Consumer<InteractionInvocation> onComplete) {
        if(deferred) return;
        this.createDefer(ephemeral).queue(hook -> {
            this.deferred = true;
            this.interactionHook = hook;
            if(onComplete != null) {
                onComplete.accept(this);
            }
        });
    }
    
    public void sendAsync(InteractionResponse response, boolean ephemeral, Consumer<InteractionInvocation> messageConsumer) {
        if(replied) {
            Logger.getGlobal().warning("The interaction is already sent, doing nothing.");
            return;
        }
        if(response instanceof MessageContent messageContent) {
            if(deferred) {
                ResponseMapper.mapSend(messageContent, this.interactionHook).queue(message -> {
                    this.replied = true;
                    this.possibleMessage = message;
                    ConsumerUtil.accept(messageConsumer, this);
                });
                return;
            }
            createSend(messageContent, ephemeral).queue(hook -> {
                setInteractionHook(hook);
                ConsumerUtil.accept(messageConsumer, this);
            });
        } else if(
                response instanceof ModalResponse modalResponse &&
                        interaction instanceof GenericCommandInteractionEvent commandInteraction
        ) {
            if(deferred) {
                throw new RuntimeException("Defer reply does not support modals");
            } else if(ephemeral) {
                throw new IllegalArgumentException("Modals can't be ephemeral");
            }
            commandInteraction.replyModal(modalResponse).queue(s -> ConsumerUtil.accept(messageConsumer, this));
        }
    }
    
    public void sendAsync(InteractionResponse response, boolean ephemeral) {
        this.sendAsync(response, ephemeral, null);
    }

    public void sendAsync(InteractionResponse response, Consumer<InteractionInvocation> consumer) {
        this.sendAsync(response, false, consumer);
    }
    
    public void sendAsync(InteractionResponse response) {
        this.sendAsync(response, false, null);
    }
    
    public void editAsync(MessageContent messageContent, Consumer<Message> consumer) {
        createEdit(messageContent).queue(consumer);
    }
    
    public InteractionInvocation defer() {
        return this.defer(false);
    }

    public InteractionInvocation defer(boolean ephemeral) {
        if(deferred) return this;
        this.interactionHook = createDefer(ephemeral).complete();
        this.deferred = true;
        return this;
    }

    public InteractionInvocation send(InteractionResponse response, boolean ephemeral) {
        if(replied) {
            Logger.getGlobal().warning("The interaction is already sent, doing nothing.");
            return this;
        }
        if(response instanceof MessageContent messageContent) {
            if(deferred) {
                this.possibleMessage = ResponseMapper.mapSend(messageContent, this.interactionHook).complete();
                this.replied = true;
                return this;
            }
            setInteractionHook(createSend(messageContent, ephemeral).complete());
        } else if(
                response instanceof ModalResponse modalResponse &&
                        interaction instanceof GenericCommandInteractionEvent commandInteraction
        ) {
            if(deferred) {
                throw new RuntimeException("Defer reply does not support modals");
            } else if(ephemeral) {
                throw new IllegalArgumentException("Modals can't be ephemeral");
            }
            commandInteraction.replyModal(modalResponse).complete();
            return this;
        }
        else {
            // TODO: handle dialogue
        }


        return this;
    }

    
    public InteractionInvocation send(InteractionResponse response) {
        return this.send(response, false);
    }

    
    public InteractionInvocation send(ModalResponse response) {
        return this.send(response, false);
    }

    
    public Message edit(MessageContent newContent) {
        return createEdit(newContent).complete();
    }

    
    @NotNull
    public ReplyCallbackAction createSend(MessageContent content, boolean ephemeral) {
        ReplyCallbackAction callbackAction = interaction.deferReply(ephemeral);
        ResponseMapper.map(content, callbackAction);
        return callbackAction;
    }

    
    public ModalCallbackAction createModal(ModalResponse response) {
        if(this.interaction instanceof GenericCommandInteractionEvent commandInteraction) {
            return commandInteraction.replyModal(response);
        }
        return null;
    }

    
    public WebhookMessageEditAction<Message> createEdit(MessageContent newContent) {
        if(!replied) {
            throw new RuntimeException("The interaction can't be edited while not being replied");
        }
        return ResponseMapper.mapEdit(newContent, this.interactionHook, this.possibleMessage.getId());
    }

    
    public ReplyCallbackAction createDefer(boolean ephemeral) {
        return this.interaction.deferReply(ephemeral);
    }

    public User getUser() {
        return this.interaction.getUser();
    }

    
    public Member getMember() {
        return this.interaction.getMember();
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
}
