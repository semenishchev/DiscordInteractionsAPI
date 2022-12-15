package me.mrfunny.interactionapi.internal.wrapper.util;

import me.mrfunny.interactionapi.response.MessageContent;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.requests.restaction.MessageEditAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.messages.MessageEditRequest;
import net.dv8tion.jda.internal.interactions.InteractionHookImpl;

public class ResponseMapper {
    public static MessageEditRequest<?> map(MessageContent messageContent, MessageEditRequest<?> toApply) {
        if(messageContent.getContent() != null) {
            toApply.setContent(messageContent.getContent());
        }
        if(!messageContent.getComponents().isEmpty()) {
            toApply.setComponents(messageContent.getComponents());
        }
        if(!messageContent.getEmbeds().isEmpty()) {
            toApply.setEmbeds(messageContent.getEmbeds());
        }
        if(!messageContent.getFileUploads().isEmpty()) {
            toApply.setFiles(messageContent.getFileUploads());
        }

        return toApply;
    }

    public static void map(MessageContent messageContent, ReplyCallbackAction toApply) {
        if(messageContent.getContent() != null) {
            toApply.setContent(messageContent.getContent());
        }
        if(!messageContent.getComponents().isEmpty()) {
            toApply.setComponents(messageContent.getComponents());
        }
        if(!messageContent.getEmbeds().isEmpty()) {
            toApply.setEmbeds(messageContent.getEmbeds());
        }
        if(!messageContent.getFileUploads().isEmpty()) {
            toApply.setFiles(messageContent.getFileUploads());
        }

    }

    public static WebhookMessageEditAction<Message> mapEdit(MessageContent messageContent, InteractionHook responseActionRaw, String id) {
        InteractionHookImpl interactionHook = (InteractionHookImpl) responseActionRaw;
        WebhookMessageEditAction<Message> result = interactionHook.editRequest(id);

        if(messageContent.getContent() != null) {
            result.setContent(messageContent.getContent());
        }
        if(!messageContent.getComponents().isEmpty()) {
            result.setComponents(messageContent.getComponents());
        }
        if(!messageContent.getEmbeds().isEmpty()) {
            result.setEmbeds(messageContent.getEmbeds());
        }
        if(!messageContent.getFileUploads().isEmpty()) {
            result.setFiles(messageContent.getFileUploads());
        }

        return result;
    }
    public static WebhookMessageCreateAction<Message> mapSend(MessageContent messageContent, InteractionHook responseActionRaw) {
        InteractionHookImpl interactionHook = (InteractionHookImpl) responseActionRaw;
        WebhookMessageCreateAction<Message> result = interactionHook.sendRequest();

        if(messageContent.getContent() != null) {
            result.setContent(messageContent.getContent());
        }
        if(!messageContent.getComponents().isEmpty()) {
            for(LayoutComponent component : messageContent.getComponents()) {
                if(component.isEmpty()) continue;
                result.addActionRow(component.getComponents());
            }
        }
        if(!messageContent.getEmbeds().isEmpty()) {
            result.setEmbeds(messageContent.getEmbeds());
        }
        if(!messageContent.getFileUploads().isEmpty()) {
            result.setFiles(messageContent.getFileUploads());
        }

        return result;
    }

}
