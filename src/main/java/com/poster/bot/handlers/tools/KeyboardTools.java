package com.poster.bot.handlers.tools;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class KeyboardTools {

    public static BiFunction<String, String, InlineKeyboardButton> CallbackButton = (text, callback) -> (new Object(){
        public InlineKeyboardButton getButton(){
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(callback);
            return button;
        }
    }.getButton());

}
