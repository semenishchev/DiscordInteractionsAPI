package me.mrfunny.applicationbot.components;

import me.mrfunny.interactionapi.annotation.ModalFieldData;
import me.mrfunny.interactionapi.common.channel.Channels;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.modals.ModalField;
import me.mrfunny.interactionapi.response.MessageContent;
import me.mrfunny.interactionapi.response.Modal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.awt.*;
import java.util.EnumSet;

public class ApplyModal extends Modal {
    public ApplyModal() {
        super("APPLICATION:INFO", "Please, fill the initial info");
    }

    @ModalFieldData(label = "Your name", maxLength = 100)
    ModalField name;

    @ModalFieldData(label = "Your age", maxLength = 2)
    ModalField age;

    @ModalFieldData(label = "Your portfolio", maxLength = 400)
    ModalField portfolio;

    @ModalFieldData(label = "Has someone invited you", placeholder = "Enter a Tag#0000 or ID", maxLength = 64, required = false)
    ModalField invited;

    @ModalFieldData(label = "Additional info", required = false, maxLength = 1000, style = TextInputStyle.PARAGRAPH)
    ModalField additionalInfo;

    @Override
    public void onExecute(InteractionInvocation invocation, Member executor) {
        invocation.defer(true);
        TextChannel channel = invocation.getGuild().createTextChannel("app-" + executor.getEffectiveName())
                .addRolePermissionOverride(invocation.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .addMemberPermissionOverride(executor.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .complete();

        EmbedBuilder builder = new EmbedBuilder()
                .setAuthor("Your application has been submitted")
                .setTitle("Your Application")
                .addField("Name", name.getValue(), false)
                .addField("Age", age.getValue(), false)
                .addField("Portfolio", portfolio.getValue(), false)
                .setColor(Color.MAGENTA)
                .setFooter("Your application would be reviewed as soon as we would need a new member for this position");
        if(invited.isPresent()) {
            builder.addField("Invited", invited.getValue(), false);
        }
        if(additionalInfo.isPresent()) {
            builder.addField("Additional info", String.format("```%s```", additionalInfo.getValue()), false);
        }
        Channels.send(channel, new MessageContent(builder));
        invocation.send(new MessageContent("Application created " + channel.getAsMention()));
    }
}
