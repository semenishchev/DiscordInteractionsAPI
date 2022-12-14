package me.mrfunny.interactionapi.menus;

import me.mrfunny.interactionapi.common.SimpleExecutable;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;

import java.util.ArrayList;

public class SelectMenuInvocation <T, S extends SelectMenu> extends InteractionInvocation {

    private ArrayList<T> selected = null;
    private final SelectMenuInteraction<T, S> event;

    public SelectMenuInvocation(SelectMenuInteraction<T, S> event) {
        super(event);
        this.event = event;
    }

    public T firstSelected() {
        if(selected == null) return null;
        if(selected.size() == 0) return null;
        return selected.get(0);
    }

    public ArrayList<T> getSelected() {
        return selected;
    }
}
