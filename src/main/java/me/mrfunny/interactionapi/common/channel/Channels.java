package me.mrfunny.interactionapi.common.channel;

import me.mrfunny.interactionapi.internal.wrapper.util.ResponseMapper;
import me.mrfunny.interactionapi.response.MessageContent;
import me.mrfunny.interactionapi.util.ConsumerUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.internal.requests.restaction.MessageCreateActionImpl;
import net.dv8tion.jda.internal.requests.restaction.MessageEditActionImpl;

import java.util.function.Consumer;

public class Channels {
    public static void sendAsync(MessageChannel channel, MessageContent content, Consumer<Message> onComplete) {
        createSend(channel, content).queue((m) -> ConsumerUtil.accept(onComplete, m));
    }

    public static void sendAsync(MessageChannel channel, MessageContent content) {
        sendAsync(channel, content, null);
    }

    public static Message send(MessageChannel channel, MessageContent content) {
        return createSend(channel, content).complete();
    }

    public static void editAsync(MessageChannel channel, String messageId, MessageContent toEdit, Consumer<Message> onComplete) {
        createEdit(channel, messageId, toEdit).queue((m) -> ConsumerUtil.accept(onComplete, m));
    }

    public static void editAsync(MessageChannel channel, String messageId, MessageContent toEdit) {
        editAsync(channel, messageId, toEdit, null);
    }

    public static Message edit(MessageChannel channel, String messageId, MessageContent toEdit) {
        return createEdit(channel, messageId, toEdit).complete();
    }

    private static MessageCreateAction createSend(MessageChannel channel, MessageContent content) {
        MessageCreateActionImpl result = new MessageCreateActionImpl(channel);
        ResponseMapper.map(content, result);
        return result;
    }

    private static MessageEditAction createEdit(MessageChannel channel, String messageId, MessageContent toEdit) {
        MessageEditActionImpl result = new MessageEditActionImpl(channel, messageId);
        ResponseMapper.map(toEdit, result);
        return result;
    }
}
