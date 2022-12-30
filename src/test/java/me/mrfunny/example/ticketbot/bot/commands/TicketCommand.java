package me.mrfunny.example.ticketbot.bot.commands;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.TicketManager;
import me.mrfunny.example.ticketbot.bot.permissions.Permissions;
import me.mrfunny.example.ticketbot.util.EmbedUtil;
import me.mrfunny.interactionapi.annotation.Parameter;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.commands.slash.SubcommandGroup;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bson.Document;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class TicketCommand implements SlashCommand {
    @Override
    public String name() {
        return "ticket";
    }

    @Override
    public Collection<SubcommandGroup> subcommands() {
        return List.of(new AdminGroup());
    }

    @Subcommand
    public void add(
            SlashCommandInvocation invocation,
            @Parameter(name = "user", description = "User to add", required = true) User rawUser
    ) {
        Member user = Main.bot.getGuild().getMember(rawUser);
        if(user == null) {
            invocation.send(new MessageContent("User not found"));
            return;
        }

        TextChannel ticket = (TextChannel) invocation.getChannel();
        if(
            !Main.bot.getPermissionManager().hasPermission(invocation.getMember(), Permissions.MANAGE_USERS) &&
            !Main.bot.getPermissionManager().hasPermission(invocation.getMember(), Permissions.MANAGE_TICKETS)
        ) {
            invocation.ephemeral(true).send(Main.bot.getComponents().NO_PERMISSIONS);
            return;
        }

        String ticketId = ticket.getId();
        if(Main.db.isJoinedTo(rawUser.getId(), ticketId)) {
            invocation.ephemeral(true).send(new MessageContent("User is already in the ticket"));
            return;
        }

        invocation.defer(false);
        invocation.send(Main.bot.getTicketManager().addToTicket(ticket, user));
    }

    @Subcommand
    public void remove(
            SlashCommandInvocation invocation,
            @Parameter(name = "user", description = "User to remove", required = true) User rawUser
    ) {
        Member user = Main.bot.getGuild().getMember(rawUser);
        if(user == null) {
            invocation.ephemeral(true).send(new MessageContent("User not found"));
            return;
        }

        TextChannel ticket = (TextChannel) invocation.getChannel();
        if(
            !Main.bot.getPermissionManager().hasPermission(invocation.getMember(), Permissions.MANAGE_USERS) &&
            !Main.bot.getPermissionManager().hasPermission(invocation.getMember(), Permissions.MANAGE_TICKETS)
        ) {
            invocation.ephemeral(true).send(Main.bot.getComponents().NO_PERMISSIONS);
            return;
        }

        String ticketId = ticket.getId();
        if(!Main.db.isJoinedTo(rawUser.getId(), ticketId)) {
            invocation.ephemeral(true).send(new MessageContent("User isn't in the ticket"));
            return;
        }

        invocation.defer(false);
        invocation.send(Main.bot.getTicketManager().removeFromTicket(ticket, user));
    }

    @Subcommand
    public void close(SlashCommandInvocation invocation, ClosureReason reason) {
        System.out.println(reason);
        TicketManager.handleTicketClosure(invocation);
    }

    private static class AdminGroup implements SubcommandGroup {

        @Override
        public String name() {
            return "admin";
        }

        @Override
        public String description() {
            return "Admin commands";
        }

        @Subcommand(name = "sendopenticketembed")
        public void sendOpenTicketEmbed(
                SlashCommandInvocation invocation,
                @Parameter(
                        name = "result",
                        stringChoices = {"Done!", "Sent ticket embed"}
                ) String result
        ) {
            Member member = invocation.getMember();
            invocation.ephemeral(true);
            if(!member.hasPermission(Permission.ADMINISTRATOR)) {
                invocation.send(new MessageContent("You don't have permissions to execute this command"));
                return;
            }

            new MessageContent(EmbedUtil.fromData(Main.messages
                    .getObject("outside-ticket")
                    .getObject("create-ticket-embed")
                    .getObject("the")))
                    .addActionRow(Main.bot.getComponents().OPEN_TICKET_BUTTON)
                    .send(invocation.getChannel());

            invocation.send(new MessageContent(result == null ? "Done!" : result));
        }
    }
}
