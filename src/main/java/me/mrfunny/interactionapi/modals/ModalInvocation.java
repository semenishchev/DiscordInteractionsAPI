package me.mrfunny.interactionapi.modals;

import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

public class ModalInvocation extends InteractionInvocation {
    public ModalInvocation(IReplyCallback replyCallback) {
        super(replyCallback);
    }
}
