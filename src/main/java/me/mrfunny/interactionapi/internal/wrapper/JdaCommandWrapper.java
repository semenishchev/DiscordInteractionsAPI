package me.mrfunny.interactionapi.internal.wrapper;

import me.mrfunny.interactionapi.internal.wrapper.util.ParameterMapper;
import me.mrfunny.interactionapi.commands.slash.SlashCommand;
import me.mrfunny.interactionapi.internal.data.command.CommandExecutor;
import me.mrfunny.interactionapi.internal.data.command.CommandParameter;
import me.mrfunny.interactionapi.internal.data.command.CommandParameters;
import me.mrfunny.interactionapi.internal.data.command.RegisteredCommand;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.util.ArrayList;

public class JdaCommandWrapper {
    public static CommandData wrap(RegisteredCommand command) {
        SlashCommandData initial;
        try {
            initial = Commands.slash(command.getName(), ((SlashCommand)command.getCommandBlueprint()).description()).setGuildOnly(command.getCommandBlueprint().isGuildOnly());
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("\nFailure while registering command " + command.getCommandBlueprint().getClass().getName() + ": " + exception.getMessage());
        }
        if(command.getMainExecutor() != null) {
            initial.addOptions(processExecutor(command.getMainExecutor()));
        } else {
            for(CommandExecutor subcommand : command.getSubcommands()) {
                initial.addSubcommands(
                        new SubcommandData(
                                subcommand.getName(),
                                subcommand.getDescription()
                        ).addOptions(processExecutor(subcommand)
                        ));
            }
        }
        return initial;
    }

    private static ArrayList<OptionData> processExecutor(CommandExecutor executor) {
        CommandParameters commandParameters = executor.getParameters();
        ArrayList<OptionData> result = new ArrayList<>(commandParameters.size());
        for(CommandParameter parameter : commandParameters) {
            result.add(new OptionData(
                    ParameterMapper.mapParameterToType(parameter.getJavaParameter().getType()),
                    parameter.getName(),
                    parameter.getDescription(),
                    parameter.isRequired()
            ));
        }
        return result;
    }
}
