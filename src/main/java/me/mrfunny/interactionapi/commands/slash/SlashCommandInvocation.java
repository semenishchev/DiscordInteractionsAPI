package me.mrfunny.interactionapi.commands.slash;

import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandInvocation extends InteractionInvocation {

    private final SlashCommandInteractionEvent castedInteraction;

    public SlashCommandInvocation(SlashCommandInteractionEvent replyCallback) {
        super(replyCallback);
        this.castedInteraction = replyCallback;
    }

    public String getCommandId() {
        return castedInteraction.getCommandId();
    }

    public MessageChannelUnion getChannel() {
        return castedInteraction.getChannel();
    }

    @Override
    public GenericCommandInteractionEvent getInteraction() {
        return castedInteraction;
    }
}
