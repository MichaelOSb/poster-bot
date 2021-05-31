package com.poster.bot.handlers.tools;

import com.poster.bot.Bot;
import com.poster.bot.database.entity.Usr;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Handler {
    protected Bot bot;

    @Autowired
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public abstract void handle(Update update, Usr usr);
}
