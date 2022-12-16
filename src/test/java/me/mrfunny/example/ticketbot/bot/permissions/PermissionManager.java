package me.mrfunny.example.ticketbot.bot.permissions;

import me.mrfunny.example.ticketbot.Main;
import me.mrfunny.example.ticketbot.bot.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PermissionManager {

    private final HashMap<Permissions, HashSet<Role>> permissionsMap = new HashMap<>();

    public PermissionManager(Bot bot) {
        DataObject permissions = Main.settings.getObject("permissions");
        for(Permissions permission : Permissions.values()) {
            HashSet<Role> rolesSet = new HashSet<>();
            DataArray roles = permissions.optArray(permission.getConfigListName()).orElse(null);
            if(roles == null) {
                throw new RuntimeException("Missing role descriptor in config on " + permission.getConfigListName());
            }

            for(Object role : roles) {
                rolesSet.add(bot.getGuild().getRoleById(role.toString()));
            }

            permissionsMap.put(permission, rolesSet);
        }
    }

    public boolean hasPermission(Member member, Permissions permission) {
        if(member.hasPermission(Permission.MANAGE_SERVER)) return true;
        HashSet<Role> permissionRoles = permissionsMap.get(permission);
        List<Role> memberRoles = member.getRoles();
        for(Role role : permissionRoles) {
            if(memberRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public HashSet<Role> getRolesWithPermission(Permissions permission) {
        return permissionsMap.get(permission);
    }
}
