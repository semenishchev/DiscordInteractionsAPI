package me.mrfunny.interactionapi.internal.data.command;

import me.mrfunny.interactionapi.internal.Command;

import java.util.Collection;
import java.util.HashMap;

public class RegisteredCommand {
    private final Command commandBlueprint;
    private final boolean global;
    private CommandExecutor mainExecutor;
    private final HashMap<String, CommandExecutor> subcommands = new HashMap<>();
    private final String name;

    public RegisteredCommand(Command instance, String name, boolean global) {
        this.name = name;
        this.commandBlueprint = instance;
        this.global = global;
    }

    public CommandExecutor getMainExecutor() {
        return mainExecutor;
    }

    public boolean isGlobal() {
        return global;
    }

    public Command getCommandBlueprint() {
        return commandBlueprint;
    }

    public void setMainExecutor(CommandExecutor mainExecutor) {
        this.mainExecutor = mainExecutor;
    }

    public void addSubcommand(CommandExecutor executor) {
        subcommands.put(executor.getName(), executor);
    }

    public Collection<CommandExecutor> getSubcommands() {
        return subcommands.values();
    }

    public CommandExecutor getSubcommand(String name) {
        return subcommands.get(name);
    }

    public String getName() {
        return name;
    }
}
