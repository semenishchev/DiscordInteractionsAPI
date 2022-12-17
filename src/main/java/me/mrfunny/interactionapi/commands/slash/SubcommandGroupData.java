package me.mrfunny.interactionapi.commands.slash;

import java.util.Collection;

public record SubcommandGroupData(String name, String description, SubcommandGroup source) {
    public SubcommandGroupData(String name, SubcommandGroup source) {
        this(name, "No description provided", source);
    }
}
