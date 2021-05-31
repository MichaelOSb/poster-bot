package com.poster.bot.handlers.menus;

import com.poster.bot.database.entity.Usr;
import com.poster.bot.handlers.tools.Menu;
import com.poster.bot.handlers.modes.PassPostBuilder;
import com.poster.bot.handlers.tools.KeyboardTools;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

@Component
public class StartMenu extends Menu {
    @Override
    public void handle(Update update, Usr usr) {
        if(update.hasCallbackQuery())
            bot.delMessage(update.getCallbackQuery().getMessage());
        bot.sendMessage(usr.getId(), "Здрасвуйте, это бот для создания обьявлений", getKeyboard());
    }

    private InlineKeyboardMarkup getKeyboard(){
        List<List<InlineKeyboardButton>> rowList = Arrays.asList(
                Arrays.asList(KeyboardTools.CallbackButton.apply("Создать обьявление", PassPostBuilder.class.getSimpleName()))
        );
        return new InlineKeyboardMarkup(rowList);
    }

}
