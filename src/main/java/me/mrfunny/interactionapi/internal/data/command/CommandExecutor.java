package me.mrfunny.interactionapi.internal.data.command;

import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.commands.slash.SubcommandGroupData;
import me.mrfunny.interactionapi.internal.wrapper.util.ParameterMapper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandExecutor {
    private final String name;
    private final Method callingMethod;
    private final CommandParameters parameters;
    private final RegisteredCommand source;
    private final String description;
    private final int contextArgumentIndex;
    private final boolean sync;
    private final SubcommandGroupData group;

    public CommandExecutor(RegisteredCommand source, String name, SubcommandGroupData group, String description, Method callingMethod, CommandParameters parameters, int contextArgumentIndex, boolean sync) {
        this.name = name;
        this.description = description;
        this.callingMethod = callingMethod;
        this.parameters = parameters;

        this.source = source;
        this.sync = sync;
        this.contextArgumentIndex = contextArgumentIndex;
        this.group = group;
    }

    public SubcommandGroupData getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public RegisteredCommand getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public void execute(CommandManager manager, SlashCommandInteractionEvent event) {
        if(!sync) {
            execute0(manager, event);
            return;
        }
        CommandManager.getAsyncExecutor().execute(() -> execute0(manager, event));
    }

    private void execute0(CommandManager manager, SlashCommandInteractionEvent event) {
        try {
            callingMethod.setAccessible(true);
            Object source;
            if(group != null) {
                source = group.source();
            } else {
                source = this.source.getCommandBlueprint();
            }
            callingMethod.invoke(source, injectInvokeArgs(manager, event, event.getOptions()));
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public CommandParameters getParameters() {
        return parameters;
    }

    public Object[] injectInvokeArgs(CommandManager manager, SlashCommandInteractionEvent context, List<OptionMapping> arguments) {
        Object[] result = new Object[parameters.size() + 1];
        int filled = 0;
        for(OptionMapping option : arguments) {
            CommandParameter toInject = parameters.get(option.getName());
            try {
                Object injecting = toInject.map(option);
                if(toInject.usesEnum()) {
                    for(Object rawConstant : toInject.getPredefinedChoicesType().getEnumConstants()) {
                        Enum<?> constant = (Enum<?>) rawConstant;
                        if(!constant.name().equals(injecting.toString())) continue;
                        injecting = constant;
                        break;
                    }
                }
//                objectToIndexMap.put(injecting, toInject.getParameterArgumentIndex());
                result[toInject.getParameterArgumentIndex()] = injecting;
                filled++;
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if(filled != (result.length - 1)) {
            for(CommandParameter javaParameter : parameters) {
                int i = javaParameter.getParameterArgumentIndex();
                if(i == contextArgumentIndex) continue;
                Object parameter = result[i];
                if(parameter instanceof SlashCommandInvocation) continue;
                if(parameter != null) continue;
                Class<?> type = javaParameter.getJavaParameter().getType();
                result[i] = ParameterMapper.mapTypeToNull(type);
                if(manager.isDebug()) {
                    System.out.println("Mapped null parameter " + javaParameter.getName() + " into " + result[i] +" with target type of " + type.getName());
                }
            }
        }

        result[contextArgumentIndex] = new SlashCommandInvocation(context);
        if(manager.isDebug()) {
            System.out.println("[DEBUG] Processing interaction arguments: " + Arrays.toString(result));
        }
        return result;
    }
}
