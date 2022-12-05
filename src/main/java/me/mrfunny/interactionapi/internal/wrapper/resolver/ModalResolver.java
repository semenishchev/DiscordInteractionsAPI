package me.mrfunny.interactionapi.internal.wrapper.resolver;

import me.mrfunny.interactionapi.internal.wrapper.resolver.interfaces.ComplexResolver;
import me.mrfunny.interactionapi.response.ModalResponse;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.List;

public class ModalResolver implements ComplexResolver<List<ActionRow>> {
    private final ArrayList<ActionRow> result = new ArrayList<>();

    public ModalResolver(Class<? extends ModalResponse> modalResponseClass) {

    }

    @Override
    public void resolve() {

    }

    public List<ActionRow> result() {
        return this.result;
    }
}
