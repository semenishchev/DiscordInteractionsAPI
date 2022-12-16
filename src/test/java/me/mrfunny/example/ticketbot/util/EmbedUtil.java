package me.mrfunny.example.ticketbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.awt.*;
import java.util.Optional;

public class EmbedUtil {
    public static MessageEmbed fromData(DataObject json) {
        EmbedBuilder builder = new EmbedBuilder();
        DataObject title = json.optObject("title").orElse(null);

        if(title != null) {
            String titleString = title.getString("it");
            String url = title.getString("url", null);
            builder.setTitle(titleString, url);
        }

        DataObject author = json.optObject("author").orElse(null);
        if (author != null) {
            String authorName = author.getString("name", "<missing_name>");
            String authorIconUrl = author.getString("icon-url", null);
            builder.setAuthor(authorName, authorIconUrl);
        }

        String description = json.getString("description", null);
        if (description != null){
            builder.setDescription(description);
        }

        String color = json.getString("color", null);
        if (color != null){
            Color colorObject = Color.decode(color);
            builder.setColor(colorObject);
        }

        DataArray fields = json.optArray("fields").orElse(null);
        if (fields != null) {
            for (int i = 0; i < fields.length(); i++) {
                DataObject field = fields.getObject(i);
                builder.addField(
                    field.getString("name"),
                    field.getString("value"),
                    field.getBoolean("inline", false)
                );
            }
        }

        String thumbnail = json.getString("thumbnail", null);
        if(thumbnail != null) {
            builder.setThumbnail(thumbnail);
        }

        DataObject footer = json.optObject("footer").orElse(null);
        if(footer != null) {
            String value = footer.getString("value", "<missing_value>");
            String icon = footer.getString("icon-url", null);
            builder.setFooter(value, icon);
        }
        return builder.build();
    }
}
