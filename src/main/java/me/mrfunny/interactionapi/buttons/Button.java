package me.mrfunny.interactionapi.buttons;

import me.mrfunny.interactionapi.common.ComplexExecutable;
import me.mrfunny.interactionapi.common.SimpleExecutable;
import me.mrfunny.interactionapi.internal.ComponentInteractionInvocation;
import me.mrfunny.interactionapi.internal.InteractionInvocation;
import me.mrfunny.interactionapi.internal.cache.ResponseCache;
import me.mrfunny.interactionapi.response.interfaces.CachedResponse;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import net.dv8tion.jda.internal.utils.EntityString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Button implements CachedResponse, ComplexExecutable<ComponentInteractionInvocation>, net.dv8tion.jda.api.interactions.components.buttons.Button {
    private String id;
    private String label;
    private ButtonStyle style;

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setStyle(ButtonStyle style) {
        this.style = style;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setEmoji(EmojiUnion emoji) {
        this.emoji = emoji;
    }

    private String url;
    private boolean disabled;
    private EmojiUnion emoji;
    private User createdFor = null;

    public Button(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji) {
        this(id, label, style, null, disabled, emoji);
    }

    public Button(String id, String label, ButtonStyle style, String url, boolean disabled, Emoji emoji) {
        this.id = id;
        this.label = label;
        this.style = style;
        this.url = url;
        this.disabled = disabled;
        this.emoji = (EmojiUnion) emoji;
        ResponseCache.addPermanent(this);
    }

    public Button(User createdFor) {
        this.createdFor = createdFor;
        this.style = ButtonStyle.PRIMARY;
    }

    public Button() {
        this(null);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    @NotNull
    @Override
    public Type getType() {
        return Type.BUTTON;
    }

    @Override
    public User getCreatedFor() {
        return this.createdFor;
    }

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @NotNull
    @Override
    public String getLabel() {
        return label;
    }

    @NotNull
    @Override
    public ButtonStyle getStyle() {
        return style;
    }

    @Nullable
    @Override
    public String getUrl() {
        return url;
    }

    @Nullable
    @Override
    public EmojiUnion getEmoji() {
        return emoji;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @NotNull
    @Override
    public DataObject toData() {
        try {
            DataObject json = DataObject.empty();
            json.put("type", 2);
            json.put("label", label);
            json.put("style", style.getKey());
            json.put("disabled", disabled);
            if (emoji != null)
                json.put("emoji", emoji);
            if (url != null)
                json.put("url", url);
            else
                json.put("custom_id", id);
            return json;
        } catch (Exception exception) {
            throw new RuntimeException("Button is not finished. Required fields are `id`, `label` and `style`");
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, style, url, disabled, emoji);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ButtonImpl other)) return false;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getLabel(), label)
                && Objects.equals(other.getUrl(), url)
                && Objects.equals(other.getEmoji(), emoji)
                && other.isDisabled() == disabled
                && other.getStyle() == style;
    }

    @Override
    public String toString() {
        return new EntityString(this)
                .setName(label)
                .addMetadata("id", id)
                .toString();
    }
}
