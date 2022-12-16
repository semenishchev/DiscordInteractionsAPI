package me.mrfunny.interactionapi.common;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public interface InChannelInvocation {
    MessageChannel getChannel();
}
