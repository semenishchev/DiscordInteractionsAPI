package me.mrfunny.example.button;

import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class NameButton extends Button {
    public NameButton(User createdFor) {
        super(createdFor);
        setId("TESTBUTTON");
        setLabel(createdFor.getName());
        setStyle(ButtonStyle.SUCCESS);
    }

    @Override
    public void onExecute(InteractionInvocation invocation, Member executor) {
        invocation.defer(true);
        invocation.send(new MessageContent(new EmbedBuilder().setTitle("Hello " + getCreatedFor().getName() + "#" + getCreatedFor().getDiscriminator()).build()));
    }
}
