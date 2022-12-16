package me.mrfunny.example.ticketbot.db;

import com.mongodb.client.*;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.bson.Document;

import java.util.function.Consumer;

public class MongoDbStorage implements Storage {
    private final MongoClient client;
    private MongoCollection<Document> tickets;
    private MongoCollection<Document> users;
    public MongoDbStorage(DataObject connectionData) {
        DataObject mongoData = connectionData.getObject("mongodb");
        client = MongoClients.create(mongoData.getString("connection-string"));
        MongoDatabase db = client.getDatabase(mongoData.getString("database"));
        String prefix = mongoData.getString("collection-prefix");
        try {
            this.tickets = db.getCollection(prefix + "tickets");
        } catch (Exception e) {
            db.createCollection(prefix + "tickets");
            this.tickets = db.getCollection(prefix + "tickets");
        }

        try {
            this.users = db.getCollection(prefix + "users");
        } catch (Exception e) {
            db.createCollection(prefix + "users");
            this.users = db.getCollection(prefix + "users");
        }
    }

    @Override
    public long getTicketsOpened(String userId) {;
        return tickets.countDocuments(new Document("openedBy", userId));
    }

    @Override
    public void runOnParticipators(String ticketId, Consumer<String> participatorId) {
        for(Document document : users.find(new Document("channelId", ticketId))) {
            participatorId.accept(document.getString("userId"));
        }
    }

    @Override
    public void createTicket(String userId, String username, String channelId) {
        tickets.insertOne(new Document()
                .append("createdBy", userId)
                .append("username", username)
                .append("channelId", channelId)
                .append("closed", false));
        addUser(channelId, userId);
    }

    @Override
    public boolean isJoinedTo(String userId, String channelId) {
        return users.countDocuments(new Document("userId", userId).append("channelId", channelId)) > 0;
    }

    @Override
    public void addUser(String channelId, String userId) {
        users.insertOne(new Document("channelId", channelId).append("userId", userId));
    }

    @Override
    public Document getTicket(String channelId) {
        return tickets.find(new Document("channelId", channelId)).first();
    }

    @Override
    public void removeUser(String channelId, String userId) {
        users.deleteOne(new Document("channelId", channelId).append("userId", userId));
    }

    @Override
    public void closeTicket(String channelId) {
        Document filter = new Document("channelId", channelId);
        tickets.deleteOne(filter);
        users.deleteMany(filter);
    }


    @Override
    public void close() {
        this.client.close();
    }
}