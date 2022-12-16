package me.mrfunny.example.ticketbot.bot.commands;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.Components;
import me.mrfunny.example.ticketbot.util.EmbedUtil;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.common.channel.Channels;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class AdminCommands implements SlashCommand {
    @Override
    public String name() {
        return "admin";
    }

    @Subcommand(name = "sendopenticketembed")
    public void sendOpenTicketEmbed(SlashCommandInvocation invocation) {
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

        invocation.send(new MessageContent("Done!"));
    }
}
