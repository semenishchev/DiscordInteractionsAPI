package me.mrfunny.interactionapi.internal.cache;

import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;

import java.util.HashMap;

public class ResponseCache {
    private final static HashMap<String, CachedResponse> cachedResponses = new HashMap<>();
    public static void handle(CachedResponse cachedResponse) {
        if(cachedResponse.deleteAfter() != -1) {
//            cachedResponse.
            CommandManager.getAsyncExecutor().schedule(() -> cachedResponses.remove(cachedResponse.));
        }

    }

    public static CachedResponse getCached(String id) {
        return cachedResponses.get(id);
    }
}
