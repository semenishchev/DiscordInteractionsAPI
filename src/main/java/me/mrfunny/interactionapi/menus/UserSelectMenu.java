package me.mrfunny.interactionapi.menus;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class UserSelectMenu extends EntitySelectMenu<User> {
    public UserSelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        super(id, placeholder, minValues, maxValues, disabled);
    }

    public UserSelectMenu(User createdFor) {
        super(createdFor);
    }

    public void onExecute(SelectMenuInvocation<User, UserSelectMenu> invocation) {}

    @NotNull
    @Override
    public Type getType() {
        return Type.USER_SELECT;
    }

    @NotNull
    @Override
    public EnumSet<SelectTarget> getEntityTypes() {
        return EnumSet.of(SelectTarget.USER);
    }
}
