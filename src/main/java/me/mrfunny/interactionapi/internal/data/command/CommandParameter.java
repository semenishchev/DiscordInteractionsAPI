package me.mrfunny.interactionapi.internal.data.command;

import me.mrfunny.interactionapi.internal.wrapper.util.ParameterMapper;
import me.mrfunny.interactionapi.util.HumanReadableEnum;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

public class CommandParameter {
    private final String name;
    private final Parameter javaParameter;
    private final int parameterArgumentIndex;
    private final Method mapperMethod; // method of OptionMapping which will map our argument to needed type
    private final boolean isRequired;
    private final String description;
    private final HashMap<String, Object> predefinedChoices = new HashMap<>();
    private Class<?> predefinedChoicesType = null;
    private boolean usesEnum = false;

    public CommandParameter(String name, String description, boolean isRequired, Parameter parameter, int parameterArgumentIndex) {
        this.javaParameter = parameter;
        this.description = description;
        this.isRequired = isRequired;
        this.parameterArgumentIndex = parameterArgumentIndex;
        this.name = name;
        Class<?> parameterType = parameter.getType();
        try {
            this.mapperMethod = ParameterMapper.mapParameter(parameterType); // remapping the java type to the JDA type and giving the error if there is not needed class found
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        if(parameterType.isEnum()) {
            usesEnum = true;
            predefinedChoicesType = parameterType;
            for(Object constantRaw : parameterType.getEnumConstants()) {
                String resultName;
                Enum<?> constant = (Enum<?>) constantRaw;
                if(constantRaw instanceof HumanReadableEnum readable) {
                    resultName = readable.humanReadable();
                } else {
                    resultName = capitalise(constant.name().replaceAll("_", " "));
                }

                predefinedChoices.put(resultName, constant.name());
            }
        }
    }

    private static String capitalise(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public boolean usesEnum() {
        return usesEnum;
    }

    public void addChoice(Object object) {
        if(usesEnum) throw new IllegalArgumentException("Cannot add custom choice to the enum");
        if(object == null) {
            return;
        }
        if(predefinedChoicesType == null) {
            predefinedChoicesType = object.getClass();
        } else if(!object.getClass().equals(predefinedChoicesType)) {
            throw new IllegalArgumentException("Cannot have different types of predefined choices");
        }
        predefinedChoices.put(object.toString(), object);
    }

    public HashMap<String, Object> getPredefinedChoices() {
        return predefinedChoices;
    }

    public Class<?> getPredefinedChoicesType() {
        return predefinedChoicesType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getDescription() {
        return description;
    }

    public Object map(OptionMapping mapping) throws InvocationTargetException, IllegalAccessException {
        return mapperMethod.invoke(mapping);
    }

    public int getParameterArgumentIndex() {
        return parameterArgumentIndex;
    }

    public Parameter getJavaParameter() {
        return javaParameter;
    }

    public String getName() {
        return name;
    }
}
