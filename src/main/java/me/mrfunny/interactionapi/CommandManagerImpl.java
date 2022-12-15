package me.mrfunny.interactionapi;

import me.mrfunny.interactionapi.buttons.Button;
import me.mrfunny.interactionapi.commands.context.ContextCommand;
import me.mrfunny.interactionapi.commands.context.ContextCommandInvocation;
import me.mrfunny.interactionapi.commands.context.MessageContextCommand;
import me.mrfunny.interactionapi.commands.context.UserContextCommand;
import me.mrfunny.interactionapi.internal.Command;
import me.mrfunny.interactionapi.internal.ComponentInteractionInvocation;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import me.mrfunny.interactionapi.internal.data.command.CommandExecutor;
import me.mrfunny.interactionapi.internal.data.command.RegisteredCommand;
import me.mrfunny.interactionapi.internal.wrapper.JdaCommandWrapper;
import me.mrfunny.interactionapi.internal.wrapper.JdaModalWrapper;
import me.mrfunny.interactionapi.internal.wrapper.resolver.ContextCommandResolver;
import me.mrfunny.interactionapi.internal.wrapper.resolver.SlashCommandResolver;
import me.mrfunny.interactionapi.menus.SelectMenuInvocation;
import me.mrfunny.interactionapi.response.Modal;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.HashMap;

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
        Modal cached = ResponseCache.getCached(event, Modal.class);
        if(cached == null) {
            cached = ResponseCache.getPermanent(event.getModalId(), Modal.class);
        }
        if(cached == null) {
            return false;
        }
        try {
            JdaModalWrapper.mapAfterRun(event, cached);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if(event.getMember() != null) {
            cached.onExecute(new InteractionInvocation(event), event.getMember());
            return true;
        }
        cached.onExecute(new InteractionInvocation(event), event.getUser());
        return true;
    }

    @Override
    public boolean processButtonInteraction(ButtonInteractionEvent event) {
        Button cached = ResponseCache.getCached(event, Button.class);
        if(cached == null) {
            cached = ResponseCache.getPermanent(event.getComponentId(), Button.class);
        }
        if(cached == null) {
            return false;
        }
        if(event.getMember() != null) {
            System.out.println("cached member");
            cached.onExecute(new ComponentInteractionInvocation(event), event.getMember());
            return true;
        }
        cached.onExecute(new ComponentInteractionInvocation(event), event.getUser());
        return true;
    }

    @Override
    public <T, S extends SelectMenu> boolean processSelectMenuInteraction(GenericSelectMenuInteractionEvent<T, S> event) {
        S rawMenu = event.getComponent();
        me.mrfunny.interactionapi.menus.SelectMenu<?> cached = ResponseCache.getCached(event, me.mrfunny.interactionapi.menus.SelectMenu.class);
        if(cached == null) {
            cached = ResponseCache.getPermanent(event.getComponentId(), me.mrfunny.interactionapi.menus.SelectMenu.class);
        }
        if(cached == null) {
            return false;
        }
        if(event.getMember() != null) {
            System.out.println("cached member");
            cached.onExecute(new SelectMenuInvocation<>(event), event.getMember());
            return true;
        }
        cached.onExecute(new SelectMenuInvocation<>(event), event.getUser());
        return true;
    }
}
