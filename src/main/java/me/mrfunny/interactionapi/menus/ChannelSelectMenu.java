package me.mrfunny.interactionapi.menus;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Objects;

public abstract class ChannelSelectMenu extends EntitySelectMenu<GuildChannel> {

    public ChannelSelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        super(id, placeholder, minValues, maxValues, disabled);
    }

    @NotNull
    @Override
    public abstract EnumSet<ChannelType> getChannelTypes();

    @NotNull
    @Override
    public Type getType() {
        return Type.CHANNEL_SELECT;
    }
    @NotNull
    @Override
    public final EnumSet<SelectTarget> getEntityTypes() {
        return EnumSet.of(SelectTarget.CHANNEL);
    }
}
