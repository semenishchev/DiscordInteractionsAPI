package me.mrfunny.interactionapi.menus;

import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class StringSelectMenu extends SelectMenu<String> implements net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu {
    private final List<SelectOption> options;

    public StringSelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled, List<SelectOption> options) {
        super(id, placeholder, minValues, maxValues, disabled);
        this.options = options;
    }

    private static List<SelectOption> parseOptions(DataArray array) {
        List<SelectOption> options = new ArrayList<>(array.length());
        array.stream(DataArray::getObject)
                .map(SelectOption::fromData)
                .forEach(options::add);
        return options;
    }

    @NotNull
    @Override
    public Type getType() {
        return Type.STRING_SELECT;
    }

    @NotNull
    @Override
    public List<SelectOption> getOptions() {
        return options;
    }

    @NotNull
    @Override
    public DataObject toData() {
        return super.toData()
                .put("type", Type.STRING_SELECT.getKey())
                .put("options", DataArray.fromCollection(options));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeholder, minValues, maxValues, disabled, options);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu other))
            return false;
        return Objects.equals(id, other.getId())
                && Objects.equals(placeholder, other.getPlaceholder())
                && minValues == other.getMinValues()
                && maxValues == other.getMaxValues()
                && disabled == other.isDisabled()
                && Objects.equals(options, other.getOptions());
    }
}
