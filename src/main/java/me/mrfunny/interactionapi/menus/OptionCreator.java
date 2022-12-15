package me.mrfunny.interactionapi.menus;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.jetbrains.annotations.NotNull;

public class OptionCreator extends net.dv8tion.jda.api.interactions.components.selections.SelectOption {
    protected OptionCreator(String label, String value) {
        super(label, value);
    }

    protected OptionCreator(String label, String description, String value, boolean isDefault, Emoji emoji) {
        super(label, value, description, isDefault, emoji);
    }


    public static net.dv8tion.jda.api.interactions.components.selections.SelectOption create(@NotNull String label, @NotNull String value) {
        return new OptionCreator(label, value);
    }
    public static net.dv8tion.jda.api.interactions.components.selections.SelectOption create(String label, String value, Emoji emoji) {
        return new OptionCreator(label, value, null, false, emoji);
    }

    public static net.dv8tion.jda.api.interactions.components.selections.SelectOption create(String label, String description, String value, Emoji emoji) {
        return new OptionCreator(label, value, description, false, emoji);
    }

    public static net.dv8tion.jda.api.interactions.components.selections.SelectOption create(String label, String description, String value) {
        return new OptionCreator(label, value, description, false, null);
    }

    public static net.dv8tion.jda.api.interactions.components.selections.SelectOption create(String label, String description, String value, boolean isDefault, Emoji emoji) {
        return new OptionCreator(label, value, description, isDefault, emoji);
    }
}
