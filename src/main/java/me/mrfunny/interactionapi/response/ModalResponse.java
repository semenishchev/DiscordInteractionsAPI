package me.mrfunny.interactionapi.response;

import me.mrfunny.interactionapi.modals.ModalInvocation;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.utils.EntityString;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ModalResponse implements CachedResponse, Modal {
    private String id;
    private String title;
    private final List<ActionRow> components = new ArrayList<>();
    private final HashMap<String, Field> fields = new HashMap<>();

    public ModalResponse(String id, String title) {
        this.id = id;
        this.title = title;
        this.init();
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public void onInit(){}
    public void onExecute(ModalInvocation invocation, User executor) {}

    public void onExecute(ModalInvocation invocation, Member executor) {}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
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
                .put("custom_id", id)
                .put("title", title);

        object.put("components", DataArray.fromCollection(components.stream()
                .map(ActionRow::toData)
                .collect(Collectors.toList())));
        return object;
    }

    @Override
    public String toString() {
        return new EntityString(this)
                .addMetadata("id", id)
                .addMetadata("title", title)
                .toString();
    }

}
