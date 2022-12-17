package me.mrfunny.interactionapi.commands.slash;

public interface SubcommandGroup {
    String name();
    default String description() {
        return "No description provided";
    }
}
