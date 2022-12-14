package me.mrfunny.interactionapi.menus;

import me.mrfunny.interactionapi.common.SimpleExecutable;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class EntitySelectMenu<T extends IMentionable> extends SelectMenu<T> implements net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu, SimpleExecutable {
    protected final Component.Type type;
    protected final EnumSet<ChannelType> channelTypes;

    public EntitySelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled, Type type, EnumSet<ChannelType> channelTypes) {
        super(id, placeholder, minValues, maxValues, disabled);
        this.type = type;
        this.channelTypes = channelTypes;
    }

    @NotNull
    @Override
    public Type getType() {
        return type;
    }

    @NotNull
    @Override
    public EnumSet<net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget> getEntityTypes() {
        switch (type) {
            case ROLE_SELECT:
                return EnumSet.of(net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget.ROLE);
            case USER_SELECT:
                return EnumSet.of(net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget.USER);
            case CHANNEL_SELECT:
                return EnumSet.of(net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget.CHANNEL);
            case MENTIONABLE_SELECT:
                return EnumSet.of(net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget.ROLE, net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget.USER);
        }
        // Ideally this never happens, so its undocumented
        throw new IllegalStateException("Unsupported type: " + type);
    }

    @NotNull
    @Override
    public EnumSet<ChannelType> getChannelTypes() {
        return channelTypes;
    }

    @NotNull
    @Override
    public DataObject toData() {
        DataObject json = super.toData().put("type", type.getKey());
        if (type == Type.CHANNEL_SELECT && !channelTypes.isEmpty())
            json.put("channel_types", DataArray.fromCollection(channelTypes.stream().map(ChannelType::getId).collect(Collectors.toList())));
        return json;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeholder, minValues, maxValues, disabled, type, channelTypes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu))
            return false;
        net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu other = (net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu) obj;
        return Objects.equals(id, other.getId())
                && Objects.equals(placeholder, other.getPlaceholder())
                && minValues == other.getMinValues()
                && maxValues == other.getMaxValues()
                && disabled == other.isDisabled()
                && type == other.getType()
                && channelTypes.equals(other.getChannelTypes());
    }
}
