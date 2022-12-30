package me.mrfunny.interactionapi.internal.wrapper;

import me.mrfunny.interactionapi.internal.data.command.*;
import me.mrfunny.interactionapi.internal.wrapper.util.ParameterMapper;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.*;

public class JdaCommandWrapper {
    public static CommandData wrap(RegisteredCommand command) {
        SlashCommandData initial;
        try {
            initial = Commands
                    .slash(command.getName(), ((SlashCommand)command.getCommandBlueprint()).description())
                    .setGuildOnly(command.getCommandBlueprint().isGuildOnly());
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException(
                    "\nFailure while registering command " +
                    command.getCommandBlueprint().getClass().getName() + ": " +
                    exception.getMessage()
            );
        }
        if(command.getMainExecutor() != null) {
            initial.addOptions(processOptions(command.getMainExecutor()));
        } else {
            for(CommandExecutor subcommand : command.getSubcommands()) {
                initial.addSubcommands(new SubcommandData(
                        subcommand.getName(),
                        subcommand.getDescription()
                ).addOptions(processOptions(subcommand)));
            }
        }

        for (RegisteredGroup group : command.getGroups().values()) {
            SubcommandGroupData data = new SubcommandGroupData(
                    group.data().name(),
                    group.data().description()
            );
            for(CommandExecutor subcommand : group.executors().values()) { // yes this is repetitive code, but JDA doesn't have ny abstraction for addSubcommands method
                data.addSubcommands(new SubcommandData(
                        subcommand.getName(),
                        subcommand.getDescription()
                ).addOptions(processOptions(subcommand)));
            }
            initial.addSubcommandGroups(data);
        }

        return initial;
    }


    private static ArrayList<OptionData> processOptions(CommandExecutor executor) {
        CommandParameters commandParameters = executor.getParameters();
        ArrayList<OptionData> result = new ArrayList<>(commandParameters.size());
        for(CommandParameter parameter : commandParameters) {
            OptionData data = new OptionData(
                    ParameterMapper.mapParameterToType(parameter.getJavaParameter().getType()),
                    parameter.getName(),
                    parameter.getDescription(),
                    parameter.isRequired()
            );
            result.add(data);

            // processing other things
            HashMap<String, Object> choices = parameter.getPredefinedChoices();
            if(choices.isEmpty()) continue;
            Class<?> choiceType = parameter.getPredefinedChoicesType();
            boolean primitive = false;
            if(choiceType.isPrimitive()) {
                if(!choiceType.equals(long.class) && !choiceType.equals(int.class)) {
                    throw new IllegalArgumentException("Primitive type " + choiceType.getName() + " cannot have predefined values\n" +
                            "Declaration: " + executor.getSource().getCommandBlueprint().getClass() + "." + executor.getName() + "()");
                }
                primitive = true;
            }

            for(Map.Entry<String, Object> choice : choices.entrySet()) {
                if(primitive) {
                    data.addChoice(choice.getKey(), (long) choice.getValue());
                    continue;
                }

                data.addChoice(choice.getKey(), choice.getValue().toString());
            }
        }
        return result;
    }
}
