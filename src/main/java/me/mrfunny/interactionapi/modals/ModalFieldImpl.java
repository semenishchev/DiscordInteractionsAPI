package me.mrfunny.interactionapi.modals;

import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class ModalFieldImpl implements ModalField {
    private String id;
    private String label;
    private TextInputStyle inputStyle;
    private int minLength;
    private int maxLength;
    private boolean required;
    private String defaultValue;
    private String placeholder;
    private String value;

    public ModalFieldImpl() {
        this.value = null;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isPresent() {
        return value != null && !value.equals("");
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public ModalFieldImpl setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public ModalFieldImpl setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public ModalFieldImpl setInputStyle(TextInputStyle inputStyle) {
        this.inputStyle = inputStyle;
        return this;
    }

    @Override
    public ModalFieldImpl setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    @Override
    public ModalFieldImpl setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public ModalFieldImpl setRequired(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public ModalFieldImpl setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public ModalFieldImpl setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public TextInputStyle getInputStyle() {
        return inputStyle;
    }

    @Override
    public int getMinLength() {
        return minLength;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }
}