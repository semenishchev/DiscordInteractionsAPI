package me.mrfunny.example.ticketbot.bot.buttons;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.TicketManager;
import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.internal.ComponentInteractionInvocation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;

public class CloseTicketButton extends Button {

    public CloseTicketButton() {
        super("CLOSE_TICKET", null, null);
        DataObject button = Main.messages
                .getObject("in-ticket")
                .getObject("creation")
                .getObject("close-button");
        setLabel(button.getString("text"));
        setStyle(ButtonStyle.valueOf(button.getString("style")));
    }

    @Override
    public void onExecute(ComponentInteractionInvocation invocation, Member executor) {
        TicketManager.handleTicketClosure(invocation);
    }
}