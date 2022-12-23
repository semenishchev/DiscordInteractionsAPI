package me.mrfunny.example.ticketbot.bot.modals;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.interactionapi.annotation.ModalFieldData;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.modals.ModalField;
import me.mrfunny.interactionapi.response.MessageContent;
import me.mrfunny.interactionapi.modals.Modal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class OpenTicketModal extends Modal {
    public OpenTicketModal() {
        super("CREATE_TICKET_MODAL", "What's your problem");
    }

    @ModalFieldData(
            label = "Brief description of your request",
            placeholder = "You can always write more detailed request after ticket gets created",
            maxLength = 1000,
            style = TextInputStyle.PARAGRAPH
    )
    ModalField description;

    @Override
    public void onExecute(InteractionInvocation invocation, Member executor) {
        invocation.defer(true);
        TextChannel channel = Main.bot.getTicketManager().createTicket(executor, description.getValue());
        new MessageContent(
                new EmbedBuilder()
                        .setTitle("Your original request")
                        .setDescription("```" + description.getValue() + "```")
        ).send(channel);
        invocation.send(new MessageContent(Main.messages
                .getObject("outside-ticket")
                .getString("create-interaction-response")
                .replaceAll("%ticket%", channel.getAsMention())));
    }
}
