package me.mrfunny.interactionapi.response;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.*;
import net.dv8tion.jda.api.utils.FileUpload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class MessageContent implements InteractionResponse, Cloneable {
    private final ArrayList<MessageEmbed> embeds = new ArrayList<>();
    private String content = null;
    private final ArrayList<FileUpload> fileUploads = new ArrayList<>();
    private final ArrayList<LayoutComponent> components = new ArrayList<>();

    public MessageContent(String content) {
        this.content = content;
    }

    public MessageContent(String content, MessageEmbed... embeds) {
        this.content = content;
        this.embeds.addAll(List.of(embeds));
    }

    public MessageContent(MessageEmbed... embeds) {
        this.embeds.addAll(List.of(embeds));
    }

    public MessageContent addEmbed(MessageEmbed embed) {
        this.embeds.add(embed);
        return this;
    }

    public MessageContent addEmbeds(Collection<MessageEmbed> embeds) {
        this.embeds.addAll(embeds);
        return this;
    }

    public MessageContent addEmbeds(MessageEmbed... embeds) {
        this.embeds.addAll(List.of(embeds));
        return this;
    }

    public MessageContent addEmbeds(EmbedBuilder... embeds) {
        this.embeds.addAll(Stream.of(embeds).map(EmbedBuilder::build).toList());
        return this;
    }

    public MessageContent addEmbed(EmbedBuilder embed) {
        this.embeds.add(embed.build());
        return this;
    }

    public MessageContent addFileUpload(FileUpload upload) {
        this.fileUploads.add(upload);
        return this;
    }

    public MessageContent addFileUploads(FileUpload... upload) {
        this.fileUploads.addAll(List.of(upload));
        return this;
    }

    public MessageContent setContent(String content) {
        this.content = content;
        return this;
    }

    public MessageContent addActionRow(ItemComponent... components) {
        this.components.add(ActionRow.of(components));
        return this;
    }

    public String getContent() {
        return content;
    }


    public ArrayList<FileUpload> getFileUploads() {
        return fileUploads;
    }

    public ArrayList<LayoutComponent> getComponents() {
        return components;
    }

    public ArrayList<MessageEmbed> getEmbeds() {
        return embeds;
    }

    @Override
    public MessageContent clone() {
        try {
            MessageContent clone = (MessageContent) super.clone();
            clone.content = this.content;
            clone.embeds.addAll(embeds);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
