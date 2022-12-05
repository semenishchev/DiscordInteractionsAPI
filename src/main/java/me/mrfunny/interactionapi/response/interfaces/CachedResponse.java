package me.mrfunny.interactionapi.response.interfaces;

import me.mrfunny.interactionapi.internal.cache.ResponseCache;

public interface CachedResponse extends InteractionResponse {
    default void init() {
        ResponseCache.handle(this);
    }

    default int deleteAfter() {return -1;}
}
