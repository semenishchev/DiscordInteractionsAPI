package me.mrfunny.example;

import me.mrfunny.interactionapi.annotation.Parameter;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.annotation.Sync;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.EmbedBuilder;

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
        invocation.sendAsync(new MessageContent("You set the switch to: " + (enabled ? "Enabled" : "Disabled")));
    }

    @Subcommand(name = "echo", description = "echoes your input")
    public void executeEcho(SlashCommandInvocation invocation, @Parameter(name = "input", required = true) String input) {
        invocation.send(new MessageContent(input));
    }

    @Subcommand(name = "survey")
    public void takeSurvey(SlashCommandInvocation invocation) {
        invocation.send(Main.surveyModal);
    }

    @Subcommand(name = "executesync")
    @Sync
    public void executeSync(SlashCommandInvocation invocation) { // the invocation of the method would be done syncronised
        invocation.deferAsync(s -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            s.sendAsync(new MessageContent("Message content", new EmbedBuilder().setTitle("This message was sent using sync").build()));
        });
    }
}
