package me.mrfunny.interactionapi.response.interfaces;

import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import net.dv8tion.jda.api.entities.User;

public interface CachedResponse extends InteractionResponse {
    default void init() {
        ResponseCache.handle(this);
    }

    default int deleteAfter() {return -1;}
    User getCreatedFor();
}
