package me.mrfunny.interactionapi.data;

import me.mrfunny.interactionapi.internal.wrapper.util.ParameterMapper;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CommandParameter {
    private final String name;
    private final Parameter javaParameter;
    private final int parameterArgumentIndex;
    private final Method mapperMethod; // method of OptionMapping which will map our argument to needed type
    private final boolean isRequired;
    private final String description;

    public CommandParameter(String name, String description, boolean isRequired, Parameter parameter, int parameterArgumentIndex) {
        this.javaParameter = parameter;
        this.description = description;
        this.isRequired = isRequired;
        this.parameterArgumentIndex = parameterArgumentIndex;
        this.name = name;
        try {
            this.mapperMethod = ParameterMapper.mapParameter(javaParameter.getType()); // remapping the java type to the JDA type and giving the error if there is not needed class found
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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
