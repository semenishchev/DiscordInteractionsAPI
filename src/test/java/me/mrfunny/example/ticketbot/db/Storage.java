package me.mrfunny.example.ticketbot.db;

import org.bson.Document;

import java.io.Closeable;
import java.util.function.Consumer;

public interface Storage extends Closeable {
    long getTicketsOpened(String userId);

    void runOnParticipators(String ticketId, Consumer<String> participatorId);

    void createTicket(String userId, String username, String channelId);

    boolean isJoinedTo(String userId, String channelId);

    void addUser(String channelId, String userId);

    Document getTicket(String channelId);

    void removeUser(String channelId, String userId);

    void closeTicket(String channelId);

    @Override
    void close();
}
