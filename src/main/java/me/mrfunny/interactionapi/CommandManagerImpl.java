package me.mrfunny.interactionapi;

import me.mrfunny.interactionapi.commands.context.ContextCommand;
import me.mrfunny.interactionapi.commands.context.ContextCommandInvocation;
import me.mrfunny.interactionapi.commands.context.MessageContextCommand;
import me.mrfunny.interactionapi.commands.context.UserContextCommand;
import me.mrfunny.interactionapi.internal.Command;
import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import me.mrfunny.interactionapi.internal.wrapper.JdaModalWrapper;
import me.mrfunny.interactionapi.internal.wrapper.resolver.ContextCommandResolver;
import me.mrfunny.interactionapi.internal.wrapper.resolver.SlashCommandResolver;
import me.mrfunny.interactionapi.internal.data.command.CommandExecutor;
import me.mrfunny.interactionapi.internal.data.command.RegisteredCommand;
import me.mrfunny.interactionapi.internal.wrapper.JdaCommandWrapper;
import me.mrfunny.interactionapi.modals.ModalInvocation;
import me.mrfunny.interactionapi.response.ModalResponse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class CommandManagerImpl implements CommandManager {
    private final JDA jda;

    public CommandManagerImpl(JDA jda) {
        this.jda = jda;
    }

    private final HashMap<String, RegisteredCommand> slashCommands = new HashMap<>();
    private final HashMap<String, MessageContextCommand> messageContextCommands = new HashMap<>();
    private final HashMap<String, UserContextCommand> userContextCommands = new HashMap<>();

    @Override
    public void registerCommand(Command commandInstance) {
        if(commandInstance instanceof ContextCommand<?> contextCommand) {
            new ContextCommandResolver(contextCommand, userContextCommands, messageContextCommands);
            return;
        }
        SlashCommandResolver resolver = new SlashCommandResolver(commandInstance);
        resolver.resolve();
        RegisteredCommand command = resolver.result();
        if(command == null) {
            throw new RuntimeException("Failed resolving command");
        }
        CommandData jdaCommand;
        try {
            jdaCommand = JdaCommandWrapper.wrap(command);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("\nError while registering command " + command.getCommandBlueprint().getClass().getName() + ": " + e.getMessage());
        }

        if(command.isGlobal()) {
            jda.upsertCommand(jdaCommand).queue();
        } else {
            for(Guild guild : jda.getGuilds()) {
                if(!command.getCommandBlueprint().shouldRegisterToGuild().apply(guild)) continue;
                guild.upsertCommand(jdaCommand).queue();
            }
        }
        slashCommands.put(command.getName(), command);
    }

    @Override
    public boolean processCommandInteraction(SlashCommandInteractionEvent event) {
        if(event == null) return false;
        RegisteredCommand command = slashCommands.get(event.getName());
        if(command == null) return false;

        if(event.getSubcommandName() != null) {
            CommandExecutor subcommand = command.getSubcommand(event.getSubcommandName());
            if(subcommand == null) return false;
            subcommand.execute(event);
            return true;
        }
        command.getMainExecutor().execute(event);
        return true;
    }

    @Override
    public boolean processContextInteraction(UserContextInteractionEvent event) {
        UserContextCommand command = userContextCommands.get(event.getCommandId());
        if(command == null) return false;
        command.execute(new ContextCommandInvocation<>(event));
        return true;
    }

    @Override
    public boolean processContextInteraction(MessageContextInteractionEvent event) {
        MessageContextCommand command = messageContextCommands.get(event.getCommandId());
        if(command == null) return false;
        command.execute(new ContextCommandInvocation<>(event));
        return true;
    }

    @Override
    public boolean processModalInteraction(ModalInteractionEvent event) {
        ModalResponse cached = (ModalResponse) ResponseCache.getCached(event.getModalId());
        if(cached == null) {
            event.reply("Interaction expired").setEphemeral(true).queue();
            return true;
        }
        try {
            JdaModalWrapper.mapAfterRun(event, cached);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if(event.getMember() != null) {
            cached.onExecute(new ModalInvocation(event), event.getMember());
            return true;
        }
        cached.onExecute(new ModalInvocation(event), event.getUser());
        return false;
    }
}
