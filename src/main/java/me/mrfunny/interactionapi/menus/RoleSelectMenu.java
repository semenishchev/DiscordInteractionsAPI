package me.mrfunny.interactionapi.menus;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class RoleSelectMenu extends EntitySelectMenu<Role> {
    public RoleSelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        super(id, placeholder, minValues, maxValues, disabled);
    }

    public RoleSelectMenu(User createdFor) {
        super(createdFor);
    }

    @NotNull
    @Override
    public EnumSet<SelectTarget> getEntityTypes() {
        return EnumSet.of(SelectTarget.ROLE);
    }

    @NotNull
    @Override
    public Type getType() {
        return Type.ROLE_SELECT;
    }
}
