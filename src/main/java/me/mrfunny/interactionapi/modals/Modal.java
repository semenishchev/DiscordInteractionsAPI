package me.mrfunny.interactionapi.modals;

import me.mrfunny.interactionapi.common.SimpleExecutable;
import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import me.mrfunny.interactionapi.internal.wrapper.JdaModalWrapper;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.EntityString;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Modal implements CachedResponse, SimpleExecutable, net.dv8tion.jda.api.interactions.modals.Modal {
    private String id;
    private String title;
    private final List<ActionRow> components = new ArrayList<>();
    private final HashMap<String, Field> fields = new HashMap<>();
    private final User createdFor;
    private final int deleteAfter;
    private net.dv8tion.jda.api.interactions.modals.Modal mappedModal = null;

    public Modal(User createdFor, int deleteAfter) {
        this.createdFor = createdFor;
        this.deleteAfter = deleteAfter;
        ResponseCache.decide(this);
    }

    public Modal(String id, String title) {
        this.deleteAfter = -1;
        this.createdFor = null;
        this.id = id;
        this.title = title;
        ResponseCache.addPermanent(this);
        try {
            mappedModal = JdaModalWrapper.buildModalToRun(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Modal(User createdFor) {
        this(createdFor, ResponseCache.DEFAULT_DELETE_AFTER);
    }

    public net.dv8tion.jda.api.interactions.modals.Modal getMappedModal() throws Exception {
        return mappedModal == null ? JdaModalWrapper.buildModalToRun(this) : mappedModal;
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public void onInit(){}

    public Modal setTitle(String title) {
        this.title = title;
        return this;
    }

    public Modal setId(String id) {
        this.id = id;
        return this;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @NotNull
    @Override
    public String getTitle() {
        return title;
    }

    @NotNull
    @Override
    public List<ActionRow> getActionRows() {
        return components;
    }

    @NotNull
    @Override
    public DataObject toData() {
        DataObject object = DataObject.empty()
                .put("custom_id", getId())
                .put("title", getTitle());

        object.put("components", DataArray.fromCollection(getActionRows().stream()
                .map(ActionRow::toData)
                .collect(Collectors.toList())));
        return object;
    }

    @Override
    public String toString() {
        return new EntityString(this)
                .addMetadata("id", getId())
                .addMetadata("title", getTitle())
                .toString();
    }

    @Override
    public User getCreatedFor() {
        return this.createdFor;
    }

    @Override
    public int deleteAfter() {
        return this.deleteAfter;
    }
}
