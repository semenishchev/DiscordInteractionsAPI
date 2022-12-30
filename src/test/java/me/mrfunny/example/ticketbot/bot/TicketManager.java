package me.mrfunny.example.ticketbot.bot;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.permissions.Permissions;
import me.mrfunny.example.ticketbot.exception.NotATicketException;
import me.mrfunny.example.ticketbot.util.EmbedUtil;
import me.mrfunny.example.ticketbot.util.TicketUtil;
import me.mrfunny.interactionapi.common.InChannelInvocation;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TicketManager {
    private final static int MAX_CHANNELS_PER_CATEGORY = 50;
    private final int maxTicketsPerUser;
    private final MessageContent helloMessage;
    private final List<Permission> allPermissions = Arrays.asList(Permission.values());
    private final List<Permission> memberPermissions = Arrays.asList(
            Permission.MESSAGE_SEND,
            Permission.VIEW_CHANNEL,
            Permission.MESSAGE_HISTORY
    );

    private final List<Permission> managerPermissions = Arrays.asList(
            Permission.MESSAGE_SEND,
            Permission.VIEW_CHANNEL,
            Permission.MESSAGE_MANAGE
    );
    public TicketManager(Bot bot) {
        this.maxTicketsPerUser = Main.settings.getInt("tickets-per-user", 5);
        DataObject creation = Main.messages.getObject("in-ticket").getObject("creation");
        this.helloMessage = new MessageContent(EmbedUtil.fromData(creation.getObject("embed"))).addActionRow(bot.getComponents().CLOSE_TICKET_BUTTON);
    }

    public TextChannel createTicket(Member member, String request) {
        ChannelAction<TextChannel> creationProcess = Main.bot.getGuild()
                .createTextChannel("ticket-" + member.getUser().getName())
                .addPermissionOverride(Main.bot.getGuild().getSelfMember(), allPermissions, null)
                .addPermissionOverride(Main.bot.getGuild().getPublicRole(), null, allPermissions)
                .setTopic("Request: " + request)
                .addPermissionOverride(member, memberPermissions, null);

        for(Role role : Main.bot.getPermissionManager().getRolesWithPermission(Permissions.VIEW_TICKETS)) {
            creationProcess = creationProcess.addPermissionOverride(role, memberPermissions, null);
        }

        for(Role role : Main.bot.getPermissionManager().getRolesWithPermission(Permissions.MANAGE_TICKETS)) {
            creationProcess = creationProcess.addPermissionOverride(role, managerPermissions, null);
        }
        TextChannel channel = creationProcess.complete();

        Message message = helloMessage.send(channel);
        channel.pinMessageById(message.getIdLong()).complete();
        User user = member.getUser();
        Main.db.createTicket(user.getId(), user.getName() + "#" + user.getDiscriminator(), channel.getId());
        Main.bot
                .getLogsChannel()
                .sendMessage(String.format("%s created a ticket %s (%s), id: %s, %s", member.getAsMention(), channel.getAsMention(), "#" + channel.getName(), channel.getId(), channel.getJumpUrl()))
                .complete();
        for(Category category : Main.bot.getCategoryManager().getCategories()) {
            if(category.getChannels().size() >= MAX_CHANNELS_PER_CATEGORY) continue;
            channel.getManager().setParent(category).complete();
            break;
        }
        return channel;
    }

    public MessageContent addToTicket(TextChannel ticket, Member toAdd) {
        ticket.upsertPermissionOverride(toAdd).grant(memberPermissions).complete();
        User user = toAdd.getUser();
        Main.db.addUser(ticket.getId(), user.getId());
        return new MessageContent(toAdd.getAsMention(), new EmbedBuilder()
                .setTitle(user.getName() + "#" + user.getDiscriminator() + " has joined the ticket")
                .build()
        );
    }

    public MessageContent removeFromTicket(TextChannel ticket, Member toRemove) {
        ticket.upsertPermissionOverride(toRemove)
                .clear(memberPermissions)
                .deny(memberPermissions)
                .complete();
        User user = toRemove.getUser();
        Main.db.removeUser(ticket.getId(), user.getId());
        return new MessageContent(toRemove.getAsMention(), new EmbedBuilder()
                .setTitle(user.getName() + "#" + user.getDiscriminator() + " has left the ticket")
                .build()
        );
    }

    public void closeTicket(TextChannel ticket) throws IOException {
        String ticketId = ticket.getId();
        String name = "#" + ticket.getName();
        Document ticketInfo = Main.db.getTicket(ticketId);
        if(ticketInfo == null) {
            throw new NotATicketException();
        }
        FileUpload transcript = TicketUtil.getTranscript(ticket);
        MessageContent content = new MessageContent(
                new EmbedBuilder()
                        .setTitle("Ticket " + name + " got closed")
                        .addField("Channel ID", ticketId, false)
                        .setFooter("Message before embed is the ticket transcript")
                        .build()
        ).addFileUpload(transcript);
        Main.db.runOnParticipators(ticketId, (id) -> {
            User participator = Main.bot.getJda().getUserById(id);
            if(participator == null) return;
            try {
                PrivateChannel dm = participator.openPrivateChannel().complete();
                if(dm == null) return;
                content.send(dm);
            } catch (Exception ignored) {}

        });
        Main.bot
                .getLogsChannel()
                .sendMessage(name + " got closed, id: " + ticketId)
                .addFiles(transcript)
                .complete();
        Main.db.closeTicket(ticketId);
        ticket.delete().complete();
    }

    public static void handleTicketClosure(InteractionInvocation invocation) {
        TextChannel ticket = (TextChannel) ((InChannelInvocation)invocation).getChannel();
        String ticketId = ticket.getId();

        Document ticketInfo = Main.db.getTicket(ticketId);
        if(
                !Main.bot.getPermissionManager().hasPermission(invocation.getMember(), Permissions.MANAGE_TICKETS) &&
                !ticketInfo.getString("createdBy").equals(invocation.getMember().getUser().getId())
        ) {
            invocation.ephemeral(true).send(Main.bot.getComponents().NO_PERMISSIONS);
            return;
        }

        try {
            Main.bot.getTicketManager().closeTicket(ticket);
            invocation.defer(false);
            invocation.send(new MessageContent("Ticket closed!"));
        } catch (NotATicketException e) {
            invocation.ephemeral(true).send(new MessageContent("Not in the valid ticket channel"));
        } catch (IOException e) {
            invocation.edit(new MessageContent("Failed to close"));
            e.printStackTrace();
        } catch (Exception ignored){}
    }

    public int getMaxTicketsPerUser() {
        return maxTicketsPerUser;
    }
}
