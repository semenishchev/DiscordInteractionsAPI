package me.mrfunny.interactionapi.internal.wrapper;

import me.mrfunny.interactionapi.annotation.ModalFieldData;
import me.mrfunny.interactionapi.modals.ModalField;
import me.mrfunny.interactionapi.modals.ModalFieldImpl;
import me.mrfunny.interactionapi.response.Modal;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class JdaModalWrapper {
    public static net.dv8tion.jda.api.interactions.modals.Modal buildModalToRun(Modal modal) throws Exception {
        net.dv8tion.jda.api.interactions.modals.Modal.Builder modalBuilder = net.dv8tion.jda.api.interactions.modals.Modal.create(modal.getId(), modal.getTitle());
        ArrayList<Field> fields = new ArrayList<>();
        for(Field field : modal.getClass().getDeclaredFields()) {
            if(field.getDeclaringClass().getName().equals(ModalField.class.getName())) {
                ModalFieldData modalFieldData = field.getAnnotation(ModalFieldData.class);
                if(modalFieldData == null) continue;
                if((field.getModifiers() & Modifier.FINAL) == 0) {
                    throw new RuntimeException("Field " + field.getDeclaringClass().getName() + "#" + field.getName() + " should be final!");
                }

                // doing magic with reflection or setting a value to the private field
                field.setAccessible(true);

                ModalFieldImpl modalField = new ModalFieldImpl()
                        .setId(modalFieldData.id())
                        .setLabel(modalFieldData.label())
                        .setPlaceholder(modalFieldData.placeholder())
                        .setInputStyle(modalFieldData.style())
                        .setMinLength(modalFieldData.minLength())
                        .setMaxLength(modalFieldData.maxLength())
                        .setRequired(modalFieldData.required());

                field.set(modal, modalField);
                fields.add(field);
            }
        }
        modal.onInit();
        for(Field javaField : fields) {
            ModalFieldImpl field = (ModalFieldImpl) javaField.get(modal);
            modalBuilder.addActionRow(TextInput.create(field.getId(), field.getLabel(), field.getInputStyle())
                    .setPlaceholder(field.getPlaceholder())
                    .setMinLength(field.getMinLength())
                    .setMaxLength(field.getMaxLength())
                    .setRequired(field.isRequired()).build());

        }
        return modalBuilder.build();
    }

    public static void mapAfterRun(ModalInteractionEvent modalInteractionEvent, Modal response) throws IllegalAccessException {
        Map<String, ModalMapping> map = modalInteractionEvent.getValues().stream().collect(Collectors.toMap(ModalMapping::getId, m -> m));
        for(Map.Entry<String, Field> classField : response.getFields().entrySet()) {
            Field field = classField.getValue();
            ModalMapping mapping = map.get(classField.getKey());
            if(mapping == null) continue;
            ModalFieldImpl modalField = (ModalFieldImpl) field.get(response);
            if(modalField == null) continue;
            modalField.setValue(mapping.getAsString());
        }
    }
}
