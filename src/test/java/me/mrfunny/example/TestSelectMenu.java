package me.mrfunny.example;

import me.mrfunny.interactionapi.menus.OptionCreator;
import me.mrfunny.interactionapi.menus.SelectMenuInvocation;
import me.mrfunny.interactionapi.menus.StringSelectMenu;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;

public class TestSelectMenu extends StringSelectMenu {

    public TestSelectMenu() {
        super("select", "Select a service", 1, 1, false);
    }

    @Override
    public <K, S extends SelectMenu> void onExecute(SelectMenuInvocation<K, S> invocation, Member member) {
        invocation.edit(new MessageContent("You selected: " + invocation.firstSelected()));
    }

    @Override
    public List<SelectOption> options() {
        return List.of(OptionCreator.of("hello", "world"));
    }
}
