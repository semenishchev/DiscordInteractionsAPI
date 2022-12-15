package me.mrfunny.interactionapi.menus;

import me.mrfunny.interactionapi.common.ComplexExecutable;
import me.mrfunny.interactionapi.common.SimpleExecutable;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class EntitySelectMenu<T extends IMentionable> extends SelectMenu<T> implements net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu {
    public EntitySelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        super(id, placeholder, minValues, maxValues, disabled);
    }

    public EntitySelectMenu(User createdFor) {
        super(createdFor);
    }

    @NotNull
    @Override
    public DataObject toData() {
        Type type = getType();
        DataObject json = super.toData().put("type", type.getKey());
        if(this instanceof ChannelSelectMenu channelSelect) {
            if (type == Type.CHANNEL_SELECT && !channelSelect.getChannelTypes().isEmpty())
                json.put("channel_types", DataArray.fromCollection(
                        channelSelect
                                .getChannelTypes()
                                .stream()
                                .map(ChannelType::getId)
                                .collect(Collectors.toList())
                ));
        }
        return json;
    }
    public EnumSet<ChannelType> channelTypes() {
        if(this instanceof ChannelSelectMenu) {
            return getChannelTypes();
        }
        return EnumSet.noneOf(ChannelType.class);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeholder, minValues, maxValues, disabled, getType(), channelTypes());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu other))
            return false;
        return Objects.equals(id, other.getId())
                && Objects.equals(placeholder, other.getPlaceholder())
                && minValues == other.getMinValues()
                && maxValues == other.getMaxValues()
                && disabled == other.isDisabled()
                && getType() == other.getType()
                && Objects.equals(channelTypes(), other.getChannelTypes());
    }

    @NotNull
    @Override
    public EnumSet<ChannelType> getChannelTypes() {
        return EnumSet.noneOf(ChannelType.class);
    }
}
