package me.mrfunny.interactionapi.data;

import java.util.ArrayList;

public class CommandParameters extends ArrayList<CommandParameter> {
    public CommandParameter get(String name) {
        for(CommandParameter parameter : this) {
            if(parameter.getName().equals(name)) {
                return parameter;
            }
        }
        return null;
    }
}
