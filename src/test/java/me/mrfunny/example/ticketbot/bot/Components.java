package me.mrfunny.example.ticketbot.bot;

import me.mrfunny.example.ticketbot.bot.buttons.CloseTicketButton;
import me.mrfunny.example.ticketbot.bot.buttons.OpenTicketButton;
import me.mrfunny.example.ticketbot.bot.modals.OpenTicketModal;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.EmbedBuilder;

public class Components {
    public final CloseTicketButton CLOSE_TICKET_BUTTON = new CloseTicketButton();
    public final OpenTicketButton OPEN_TICKET_BUTTON = new OpenTicketButton();
    public final OpenTicketModal OPEN_TICKET_MODAL = new OpenTicketModal();
    public final MessageContent NO_PERMISSIONS = new MessageContent(new EmbedBuilder().setTitle("You don't have enough permissions!"));
}
