package me.mrfunny.example.button;

import me.mrfunny.example.Main;
import me.mrfunny.example.modal.SurveyModal;
import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class SurveyButton extends Button {
    public SurveyButton() {
        super();
        setId("survey");
        setStyle(ButtonStyle.DANGER);
        setLabel("Take it");

    }
    @Override
    public void onExecute(InteractionInvocation invocation, Member executor) {
        invocation.send(Main.surveyModal);
    }
}
