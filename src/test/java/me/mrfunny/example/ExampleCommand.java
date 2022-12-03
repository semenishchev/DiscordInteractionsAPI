package me.mrfunny.example;

import me.mrfunny.interactionapi.commands.SlashCommand;
import me.mrfunny.interactionapi.annotation.Main;
import me.mrfunny.interactionapi.annotation.Parameter;
import me.mrfunny.interactionapi.commands.SlashCommandInvocation;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class ExampleCommand implements SlashCommand {
    @Override
    public String name() {
        return "example";
    }

    @Main
    public void main(
            SlashCommandInvocation invocation,
            @Parameter(name = "welcome_test", required = true) String welcomeText,
            @Parameter(name = "to_send") User toSend
    ) throws InterruptedException {
        invocation.defer(true);
        Thread.sleep(4000);
        User user = toSend == null ? invocation.getUser() : toSend;
        MessageContent response = new MessageContent(
                "this is it",
                new EmbedBuilder()
                        .setAuthor(user.getName() + "#" + user.getDiscriminator())
                        .addField("Welcome", welcomeText, true)
                        .build()
        );

        invocation.send(response);
        Thread.sleep(4000);
        invocation.edit(response.addEmbed(new EmbedBuilder().setTitle("This is an edited message")));
    }
}
