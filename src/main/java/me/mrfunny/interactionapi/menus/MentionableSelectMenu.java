package me.mrfunny.interactionapi.menus;

import me.mrfunny.interactionapi.common.ComplexExecutable;
import me.mrfunny.interactionapi.common.SimpleExecutable;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.User;
import org.w3c.dom.Entity;

import java.util.EnumSet;

public class MentionableSelectMenu extends EntitySelectMenu<IMentionable> {
    public MentionableSelectMenu(String id, String placeholder, int minValues, int maxValues, boolean disabled) {
        super(id, placeholder, minValues, maxValues, disabled);
    }

    public MentionableSelectMenu(User createdFor) {
        super(createdFor);
    }

    @Override
    public Type getType() {
        return Type.MENTIONABLE_SELECT;
    }

    @Override
    public EnumSet<SelectTarget> getEntityTypes() {
        return EnumSet.of(SelectTarget.ROLE, SelectTarget.USER);
    }
}
