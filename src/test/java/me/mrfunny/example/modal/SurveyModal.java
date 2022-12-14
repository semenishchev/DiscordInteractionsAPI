package me.mrfunny.example.modal;

import me.mrfunny.interactionapi.annotation.ModalFieldData;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.modals.ModalField;
import me.mrfunny.interactionapi.response.MessageContent;
import me.mrfunny.interactionapi.response.Modal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class SurveyModal extends Modal {
    public SurveyModal() {
        super("MODAL_SURVEY", "Take our survey");
    }

    @ModalFieldData(label = "This is a test field")
    private ModalField testField;

    @Override
    public void onExecute(InteractionInvocation invocation, Member executor) {
        invocation.defer(true);
        invocation.send(new MessageContent(new EmbedBuilder().setTitle("Your reply").setDescription(testField.getValue()).build()));
    }
}
