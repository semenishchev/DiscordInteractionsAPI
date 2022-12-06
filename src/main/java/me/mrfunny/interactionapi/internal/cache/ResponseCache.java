package me.mrfunny.interactionapi.internal.cache;

import me.mrfunny.interactionapi.CommandManager;
import me.mrfunny.interactionapi.response.Modal;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

@SuppressWarnings("unchecked")
public class ResponseCache {
    public static final int DEFAULT_DELETE_AFTER = 300;
    private final static ArrayList<CachedResponse> responses = new ArrayList<>();
    private final static ArrayList<CachedResponse> permanentResponses = new ArrayList<>();
    private final static HashMap<Class<? extends CachedResponse>, BiPredicate<GenericInteractionCreateEvent, CachedResponse>> searchers = new HashMap<>();
    static {
        searchers.put(Modal.class, (interaction, gotResponse) -> {
            if(gotResponse instanceof Modal response) {
                return response.getCreatedFor().getIdLong() == interaction.getUser().getIdLong();
            }
            return false;
        });
    }

    public static void handle(CachedResponse cachedResponse) {
        if((!cachedResponse.isPermanent() || cachedResponse.deleteAfter() != -1) && cachedResponse.getCreatedFor() != null) {
            CommandManager.getAsyncExecutor().schedule(() -> responses.remove(cachedResponse), cachedResponse.deleteAfter(), TimeUnit.SECONDS);
            return;
        }
        permanentResponses.add(cachedResponse);
    }

    public static <T extends CachedResponse> T getCached(GenericInteractionCreateEvent event, Class<T> toSearch) {
        BiPredicate<GenericInteractionCreateEvent, CachedResponse> searcher = searchers.get(toSearch);
        if(searcher == null) return null;
        for(CachedResponse response : responses) {
            if(response.getCreatedFor() == null) continue;
            if(!event.getInteraction().getId().equals(response.getId())) continue;
            if(!searcher.test(event, response)) continue;
            return (T) response;
        }
        return null;
    }

    public static <T extends CachedResponse> T getPermanent(String id, Class<T> toReturn) {
        for(CachedResponse response : permanentResponses) {
            if(!toReturn.isAssignableFrom(response.getClass())) continue;
            if(response.getId().equals(id)) {
                return (T) response;
            }
        }
        return null;
    }
}
