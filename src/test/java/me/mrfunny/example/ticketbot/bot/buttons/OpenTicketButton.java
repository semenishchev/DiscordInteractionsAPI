package me.mrfunny.example.ticketbot.bot.buttons;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.permissions.Permissions;
import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.internal.ComponentInteractionInvocation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;

public class OpenTicketButton extends Button {
    public OpenTicketButton() {
        super("OPEN_TICKET", null, null);
        DataObject button = Main.messages
                .getObject("outside-ticket")
                .getObject("create-ticket-embed")
                .getObject("create-ticket-button");
        setLabel(button.getString("text"));
        setStyle(ButtonStyle.valueOf(button.getString("style")));
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public void onExecute(ComponentInteractionInvocation invocation, Member executor) {
        String executorId = executor.getUser().getId();
        if(Main.bot.getTicketManager().getMaxTicketsPerUser() <= Main.db.getTicketsOpened(executorId)) {
            if(!Main.bot.getPermissionManager().hasPermission(executor, Permissions.MANAGE_TICKETS)) {
                invocation.send(Main.bot.getComponents().NO_PERMISSIONS);
                return;
            }
        }
        invocation.send(Main.bot.getComponents().OPEN_TICKET_MODAL);
    }
}
