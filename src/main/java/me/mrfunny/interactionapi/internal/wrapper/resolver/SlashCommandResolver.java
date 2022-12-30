package me.mrfunny.interactionapi.internal.wrapper.resolver;

import me.mrfunny.interactionapi.annotation.Main;
import me.mrfunny.interactionapi.annotation.Parameter;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.annotation.Sync;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.commands.slash.SubcommandGroup;
import me.mrfunny.interactionapi.commands.slash.SubcommandGroupData;
import me.mrfunny.interactionapi.internal.data.command.*;
import me.mrfunny.interactionapi.internal.wrapper.resolver.interfaces.ComplexResolver;

import java.lang.reflect.Method;
import java.util.HashMap;

public class SlashCommandResolver implements ComplexResolver<RegisteredCommand> {
    private final Class<?> commandClass;
    private RegisteredCommand command = null;
    private final SlashCommand source;

    public SlashCommandResolver(SlashCommand commandInstance) {
        this.commandClass = commandInstance.getClass();
        this.source = commandInstance;
    }

    @Override
    public void resolve() {
        this.command = new RegisteredCommand(source, source.name(), source.isGlobal());
        resolveExecutables();
    }

    private void resolveExecutables() {
        boolean gotMain = false;
        for(Method method : commandClass.getMethods()) {
            if(method.isAnnotationPresent(Main.class)) {
                if(gotMain) throw new IllegalStateException("Class " + commandClass.getName() + " has 2 or more main executable methods");
                command.setMainExecutor(resolveExecutable(method, null));
                gotMain = true;
                continue;
            }
            if(!method.isAnnotationPresent(Subcommand.class)) continue;
            if(gotMain) {
                throw new RuntimeException("Command can't have main executor and subcommands");
            }
            command.addSubcommand(resolveExecutable(method, null));
        }

        for(SubcommandGroup group : source.subcommands()) {
            SubcommandGroupData data = new SubcommandGroupData(group.name(), group.description(), group);
            HashMap<String, CommandExecutor> executors = new HashMap<>();
            for(Method method : group.getClass().getMethods()) {
                if(method.isAnnotationPresent(Main.class)) {
                    throw new RuntimeException("Cannot have Main method for group. Declared in " + commandClass.getName() + ", " + group.getClass().getName());
                }
                if(!method.isAnnotationPresent(Subcommand.class)) continue;
                CommandExecutor executor = resolveExecutable(method, data);
                executors.put(executor.getName(), executor);
            }
            command.addGroup(new RegisteredGroup(data, executors));
        }
    }

    private CommandExecutor resolveExecutable(Method method, SubcommandGroupData group) {

        CommandParameters parameters = new CommandParameters();
        java.lang.reflect.Parameter[] methodParameters = method.getParameters();
        int contextArgumentIndex = -1;
        for (int i = 0; i < methodParameters.length; i++) {
            java.lang.reflect.Parameter parameter = methodParameters[i];
            if(parameter.getType().equals(SlashCommandInvocation.class)) {
                if(contextArgumentIndex != -1) throw new IllegalStateException("Method can't have 2 context arguments");
                contextArgumentIndex = i;

                continue;
            }
            parameters.add(resolveParameter(parameter, i));
        }
        if(contextArgumentIndex == -1) {
            throw new IllegalArgumentException("Argument with type " + SlashCommandInvocation.class.getName() + " is required to build a command");
        }
        String name = null;
        String description = null;
        if(method.isAnnotationPresent(Subcommand.class)) {
            Subcommand subcommandData = method.getAnnotation(Subcommand.class);
            name = subcommandData.name();
            description = subcommandData.description();
        }

        if(name == null || name.equals("")) {
            name = method.getName();
        }
        if(description == null || description.equals("")) {
            description = "No description provided.";
        }
        return new CommandExecutor(this.command, name, group, description, method, parameters, contextArgumentIndex, method.isAnnotationPresent(Sync.class));
    }

    private static CommandParameter resolveParameter(java.lang.reflect.Parameter param, int index) {
        String name;
        String description;
        boolean required;
        String[] stringChoices = {};
        long[] longChoices = {};
        if(param.isAnnotationPresent(Parameter.class)) {
            Parameter parameterDeclaration = param.getAnnotation(Parameter.class);
            name = parameterDeclaration.name();
            description = parameterDeclaration.description();
            required = parameterDeclaration.required();
            stringChoices = parameterDeclaration.stringChoices();
            longChoices = parameterDeclaration.longChoices();
        } else {
            name = param.getName();
            description = "No description provided.";
            required = true;
        }
        if(stringChoices.length > 0 && longChoices.length > 0) {
            throw new IllegalArgumentException("Parameter declaration has long and string predefined choices\n" +
                    "Method: " + param.getDeclaringExecutable().getName() + " in " + param.getDeclaringExecutable().getDeclaringClass().getName());
        }
        CommandParameter result = new CommandParameter(name, description, required, param, index);

        if(stringChoices.length > 0) {
            for(String choice : stringChoices) {
                result.addChoice(choice);
            }
        } else if(longChoices.length > 0) {
            for(long choice : longChoices) {
                result.addChoice(choice);
            }
        }
        return result;
    }

    @Override
    public RegisteredCommand result() {
        return this.command;
    }
}
