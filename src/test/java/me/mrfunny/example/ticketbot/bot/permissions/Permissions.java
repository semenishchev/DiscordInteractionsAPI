package me.mrfunny.example.ticketbot.bot.permissions;

public enum Permissions {
    VIEW_TICKETS("view-ticket-roles"),
    MANAGE_USERS("manage-users-roles"),
    MANAGE_TICKETS("manage-ticket-roles");

    private final String configListName;

    Permissions(String configListName) {
        this.configListName = configListName;
    }

    public String getConfigListName() {
        return configListName;
    }
}
