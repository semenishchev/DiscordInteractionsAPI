package me.mrfunny.interactionapi.modals;

import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public interface ModalField {
    String getValue();

    ModalFieldImpl setId(String id);

    ModalFieldImpl setLabel(String label);

    ModalFieldImpl setInputStyle(TextInputStyle inputStyle);

    ModalFieldImpl setMinLength(int minLength);

    ModalFieldImpl setMaxLength(int maxLength);

    ModalFieldImpl setRequired(boolean required);

    ModalFieldImpl setDefaultValue(String defaultValue);

    ModalFieldImpl setPlaceholder(String placeholder);

    String getId();

    String getLabel();

    TextInputStyle getInputStyle();

    int getMinLength();

    int getMaxLength();

    boolean isRequired();

    String getDefaultValue();

    String getPlaceholder();
}
