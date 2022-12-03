package me.mrfunny.interactionapi.commands.context;

import net.dv8tion.jda.api.entities.User;

public interface UserContextCommand {
    void execute(User user);
}
