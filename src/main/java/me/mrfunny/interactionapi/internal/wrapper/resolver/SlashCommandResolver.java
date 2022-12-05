package me.mrfunny.interactionapi.internal.wrapper.resolver;

import me.mrfunny.interactionapi.annotation.Sync;
import me.mrfunny.interactionapi.internal.data.command.CommandExecutor;
import me.mrfunny.interactionapi.internal.Command;
import me.mrfunny.interactionapi.annotation.Subcommand;
import me.mrfunny.interactionapi.annotation.Main;
import me.mrfunny.interactionapi.annotation.Parameter;
import me.mrfunny.interactionapi.internal.data.command.CommandParameter;
import me.mrfunny.interactionapi.internal.data.command.CommandParameters;
import me.mrfunny.interactionapi.internal.data.command.RegisteredCommand;
import me.mrfunny.interactionapi.commands.slash.SlashCommandInvocation;
import me.mrfunny.interactionapi.internal.wrapper.resolver.interfaces.ComplexResolver;

import java.lang.reflect.Method;

public class SlashCommandResolver implements ComplexResolver<RegisteredCommand> {
    private final Class<?> commandClass;
    private RegisteredCommand command = null;
    private final Command source;

    public SlashCommandResolver(Command commandInstance) {
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
                command.setMainExecutor(resolveExecutable(method));
                gotMain = true;
                continue;
            }
            if(!method.isAnnotationPresent(Subcommand.class)) continue;
            if(gotMain) {
                throw new RuntimeException("Command can't have main executor and subcommands");
            }
            command.addSubcommand(resolveExecutable(method));
        }
    }

    private CommandExecutor resolveExecutable(Method method) {

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

        if(description == null || name.equals("")) {
            description = "No description provided.";
        }
        return new CommandExecutor(this.command, name, description, method, parameters, contextArgumentIndex, method.isAnnotationPresent(Sync.class));
    }

    private static CommandParameter resolveParameter(java.lang.reflect.Parameter param, int index) {
        String name;
        String description;
        boolean required;
        if(param.isAnnotationPresent(Parameter.class)) {
            Parameter parameterDeclaration = param.getAnnotation(Parameter.class);
            name = parameterDeclaration.name();
            description = parameterDeclaration.description();
            required = parameterDeclaration.required();;
        } else {
            name = param.getName();
            description = "No description provided.";
            required = true;
        }

        return new CommandParameter(name, description, required, param, index);
    }

    @Override
    public RegisteredCommand result() {
        return this.command;
    }
}
