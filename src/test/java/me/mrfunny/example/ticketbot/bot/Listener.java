package me.mrfunny.example.ticketbot.bot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if(message.getType().equals(MessageType.CHANNEL_PINNED_ADD) && message.getAuthor().isBot()) {
            message.delete().queue();
        }
    }
}
