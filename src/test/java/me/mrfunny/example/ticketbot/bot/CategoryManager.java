package me.mrfunny.example.ticketbot.bot;

import me.mrfunny.example.ticketbot.Main;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.utils.data.DataArray;

import java.util.HashSet;

public class CategoryManager {
    private final HashSet<Category> categories = new HashSet<>();
    public CategoryManager(Bot bot) {
        DataArray categories = Main.settings.optArray("categories").orElse(null);
        if(categories == null) return;
        for(Object rawCategory : categories) {
            if(rawCategory == null) continue;
            Category category = bot.getGuild().getCategoryById(rawCategory.toString());
            if(category == null) continue;
            this.categories.add(category);
        }
    }

    public HashSet<Category> getCategories() {
        return categories;
    }
}
