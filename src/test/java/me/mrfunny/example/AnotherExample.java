package me.mrfunny.example;

import me.mrfunny.interactionapi.annotation.Parameter;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.commands.SlashCommand;
import me.mrfunny.interactionapi.commands.SlashCommandInvocation;
import me.mrfunny.interactionapi.response.MessageContent;

public class AnotherExample implements SlashCommand {
    @Override
    public String name() {
        return "anotherexample";
    }

    @Override
    public String description() {
        return "This is another example command";
    }

    @Subcommand
    public void subcommand(SlashCommandInvocation invocation, @Parameter(name = "enabled", required = true) boolean enabled) {
        invocation.send(new MessageContent("You set the switch to: " + (enabled ? "Enabled" : "Disabled")));
    }

    @Subcommand(name = "echo", description = "echoes your input")
    public void executeEcho(SlashCommandInvocation invocation, @Parameter(name = "input", required = true) String input) {
        invocation.send(new MessageContent(input));
    }
}
