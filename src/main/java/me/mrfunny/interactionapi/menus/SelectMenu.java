package me.mrfunny.interactionapi.menus;

import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.EntityString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SelectMenu<T> implements
        net.dv8tion.jda.api.interactions.components.selections.SelectMenu,
        CachedResponse {
    protected String id, placeholder;
    protected int minValues, maxValues;
    protected boolean disabled;
    protected User createdFor;

    public SelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        this.id = id;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.disabled = disabled;
    }

    public SelectMenu(User createdFor) {
        this.createdFor = createdFor;
    }

    public SelectMenu() {
        this(null);
    }

    @Override
    public User getCreatedFor() {
        return createdFor;
    }

    public SelectMenu<T> setId(String id) {
        this.id = id;
        return this;
    }

    public SelectMenu<T> setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public SelectMenu<T> setMinValues(int minValues) {
        this.minValues = minValues;
        return this;
    }

    public SelectMenu<T> setMaxValues(int maxValues) {
        this.maxValues = maxValues;
        return this;
    }

    public SelectMenu<T> setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @Nullable
    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public int getMinValues() {
        return minValues;
    }

    @Override
    public int getMaxValues() {
        return maxValues;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }



    @NotNull
    @Override
    public DataObject toData() {
        DataObject data = DataObject.empty();
        data.put("custom_id", id);
        data.put("min_values", minValues);
        data.put("max_values", maxValues);
        data.put("disabled", disabled);
        if (placeholder != null)
            data.put("placeholder", placeholder);
        return data;
    }

    @Override
    public String toString() {
        return new EntityString(net.dv8tion.jda.api.interactions.components.selections.SelectMenu.class)
                .setType(getType())
                .addMetadata("id", id)
                .addMetadata("placeholder", placeholder)
                .toString();
    }

    @NotNull
    @Override
    public net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu withDisabled(boolean disabled) {
        try {
            return (net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu) ((SelectMenu<T>)this.clone()).setDisabled(disabled);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public <K, S extends net.dv8tion.jda.api.interactions.components.selections.SelectMenu> void onExecute(SelectMenuInvocation<K,S> invocation, Member member) {
        this.onExecute(invocation, member.getUser());
    }

    public <K, S extends net.dv8tion.jda.api.interactions.components.selections.SelectMenu> void onExecute(SelectMenuInvocation<K,S> invocation, User user) {
    }
}
