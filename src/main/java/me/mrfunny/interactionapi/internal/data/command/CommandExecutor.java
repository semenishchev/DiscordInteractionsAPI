package me.mrfunny.interactionapi.internal.data.command;

import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.commands.slash.SubcommandGroupData;
import me.mrfunny.interactionapi.internal.wrapper.util.ParameterMapper;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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

    public void execute(SlashCommandInteractionEvent event) {
        if(!sync) {
            execute0(event);
            return;
        }
        CommandManager.getAsyncExecutor().execute(() -> execute0(event));
    }

    private void execute0(SlashCommandInteractionEvent event) {
        try {
            callingMethod.setAccessible(true);
            Object source;
            if(group != null) {
                source = group.source();
            } else {
                source = this.source.getCommandBlueprint();
            }
            callingMethod.invoke(source, injectInvokeArgs(event, event.getOptions()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public CommandParameters getParameters() {
        return parameters;
    }

    public Object[] injectInvokeArgs(SlashCommandInteractionEvent context, List<OptionMapping> arguments) {
        Object[] result = new Object[parameters.size() + 1];
        int filled = 0;
        for(OptionMapping option : arguments) {
            CommandParameter toInject = parameters.get(option.getName());
            try {
                result[toInject.getParameterArgumentIndex()] = toInject.map(option);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            filled++;
        }
        if(filled != (result.length - 1)) {
            for (int i = 0; i < result.length - 1; i++) {
                Object parameter = result[i];
                if(parameter instanceof SlashCommandInvocation) continue;
                if(parameter != null) continue;
                result[i] = ParameterMapper.mapTypeToNull(parameters.get(i).getJavaParameter().getType());
            }
        }
        result[contextArgumentIndex] = new SlashCommandInvocation(context);
        return result;
    }
}
