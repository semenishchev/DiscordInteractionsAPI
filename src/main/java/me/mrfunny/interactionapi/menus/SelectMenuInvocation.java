package me.mrfunny.interactionapi.menus;

import me.mrfunny.interactionapi.common.SimpleExecutable;
import me.mrfunny.interactionapi.internal.ComponentInteractionInvocation;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;

import java.util.ArrayList;
import java.util.List;

public class SelectMenuInvocation <T, S extends SelectMenu> extends ComponentInteractionInvocation {

    private final List<T> selected;
    private final SelectMenuInteraction<T, S> event;

    public SelectMenuInvocation(SelectMenuInteraction<T, S> event) {
        super(event);
        this.event = event;
        this.selected = event.getValues();
    }

    public T firstSelected() {
        if(selected == null) return null;
        if(selected.size() == 0) return null;
        return selected.get(0);
    }

    public SelectMenuInteraction<T, S> getEvent() {
        return event;
    }

    public List<T> getSelected() {
        return selected;
    }
}
