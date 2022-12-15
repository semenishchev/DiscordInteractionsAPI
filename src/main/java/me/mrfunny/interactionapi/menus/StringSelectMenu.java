package me.mrfunny.interactionapi.menus;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class StringSelectMenu extends SelectMenu<SelectOption> {
    private List<SelectOption> options;

    public StringSelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        super(id, placeholder, minValues, maxValues, disabled);
        this.options = options();
    }

    public StringSelectMenu(String id, String placeholder) {
        super(id, placeholder, 1, 1, false);
        this.options = options();
    }

    public StringSelectMenu(String id) {
        super(id, null, 1, 1, false);
        this.options = options();
    }

    public StringSelectMenu(User createdFor) {
        this.createdFor = createdFor;
        this.options = options();
    }

    public StringSelectMenu() {
        this((User) null);
    }

    public SelectOption get(String key) {
        for(SelectOption option : options) {
            if(option.getValue().equals(key)) {
                return option;
            }
        }
        return null;
    }

    public SelectOption get(SelectMenuInvocation<String, ?> invocation) {
        return get(invocation.firstSelected());
    }

    @NotNull
    @Override
    public Type getType() {
        return Type.STRING_SELECT;
    }

    public abstract List<SelectOption> options();
    public StringSelectMenu addOption(SelectOption option) {
        this.options.add(option);
        return this;
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
