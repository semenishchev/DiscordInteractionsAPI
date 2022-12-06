package me.mrfunny.interactionapi.response;

import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.entities.User;

public class Dialogue implements CachedResponse {
    @Override
    public User getCreatedFor() {
        return null;
    }
}
