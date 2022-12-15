package me.mrfunny.applicationbot.commands;

import me.mrfunny.applicationbot.Main;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.common.channel.Channels;
import me.mrfunny.interactionapi.internal.ComponentInteractionInvocation;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ApplicationCommand implements SlashCommand {

    private final Button applyButton = new Button("APPLICATION:CREATE", "Apply!", ButtonStyle.SECONDARY) {
        @Override
        public void onExecute(ComponentInteractionInvocation invocation, Member executor) {
            invocation.send(Main.APPLY_MODAL);
        }
    };

    @Override
    public String description() {
        return "Server staff applications related command";
    }

    @Override
    public String name() {
        return "application";
    }

    @Subcommand(name = "sendmessage")
    public void sendEmbedMessage(SlashCommandInvocation invocation) {
        Member user = invocation.getMember();
        invocation.ephemeral(true);
        if(!user.hasPermission(Permission.ADMINISTRATOR)) {
            invocation.send(new MessageContent("You don't have enough permissions"));
            return;
        }
        Channels.send(invocation.getChannel(), new MessageContent(
                new EmbedBuilder()
                    .setTitle("Apply to Ural")
                    .addField("Things to make sure", "You like carbonara pizza", false)
                    .build())
                .addActionRow(applyButton)
        );
        invocation.send(new MessageContent("Done"));
    }
}
