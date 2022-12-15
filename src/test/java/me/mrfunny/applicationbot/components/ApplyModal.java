package me.mrfunny.applicationbot.components;

import me.mrfunny.interactionapi.annotation.ModalFieldData;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.modals.ModalField;
import me.mrfunny.interactionapi.response.Modal;
import net.dv8tion.jda.api.entities.Member;

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

    @ModalFieldData(label = "Additional info", required = false)
    ModalField additionalInfo;

    @Override
    public void onExecute(InteractionInvocation invocation, Member executor) {
        
    }
}
