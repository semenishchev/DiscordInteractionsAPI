package me.mrfunny.interactionapi.internal.data.command;

import me.mrfunny.interactionapi.commands.slash.SubcommandGroupData;

import java.util.HashMap;

public record RegisteredGroup(SubcommandGroupData data, HashMap<String, CommandExecutor> executors) {
}
