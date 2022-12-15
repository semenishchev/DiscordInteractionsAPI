package me.mrfunny.interactionapi.common;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class Mentionable {
    private final IMentionable mentionable;

    public Mentionable(IMentionable mentionable) {
        this.mentionable = mentionable;
    }

    public Role asRole() {
        return (Role) mentionable;
    }

    public User asUser() {
        return (User) mentionable;
    }
}
