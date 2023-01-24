package me.mrfunny.interactionapi.response.interfaces;

import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import net.dv8tion.jda.api.entities.User;

public interface CachedResponse extends InteractionResponse {

    default int deleteAfter() {return -1;}

    /**
     * Represents if the response to the interaction is permanent and needs to be always loaded
     * @return True - interaction won't be cached, and you need to reference either a field of the response type, False will cache the interaction as default
     */
    default boolean isPermanent() {
        return false;
    }

    User getCreatedFor();
    String getId();

    default boolean close() {
        return ResponseCache.removeCached(this);
    }
}
