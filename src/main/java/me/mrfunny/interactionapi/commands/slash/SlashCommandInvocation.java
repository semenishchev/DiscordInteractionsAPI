package me.mrfunny.interactionapi.commands.slash;

import me.mrfunny.interactionapi.common.InChannelInvocation;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandInvocation extends InteractionInvocation implements InChannelInvocation {

    private final SlashCommandInteractionEvent castedInteraction;

    public SlashCommandInvocation(SlashCommandInteractionEvent replyCallback) {
        super(replyCallback);
        this.castedInteraction = replyCallback;
    }

    public String getCommandId() {
        return castedInteraction.getCommandId();
    }

    @Override
    public MessageChannel getChannel() {
        return castedInteraction.getChannel();
    }

    @Override
    public GenericCommandInteractionEvent getInteraction() {
        return castedInteraction;
    }
}
