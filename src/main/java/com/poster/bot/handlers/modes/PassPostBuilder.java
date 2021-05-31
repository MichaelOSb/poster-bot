package com.poster.bot.handlers.modes;

import com.poster.bot.database.entity.PassPost;
import com.poster.bot.database.entity.Usr;
import com.poster.bot.handlers.tools.Mode;
import com.poster.bot.handlers.menus.StartMenu;
import com.poster.bot.handlers.tools.KeyboardTools;
import com.poster.bot.services.PassPostService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PassPostBuilder extends Mode {
    private final PassPostService passPostService;

    public PassPostBuilder(PassPostService passPostService) {
        this.passPostService = passPostService;
    }

    @Override
    public void handle(Update update, Usr usr) {
        switch (usr.getStep()){
            case 0:
                if(update.hasCallbackQuery())
                    bot.delMessage(update.getCallbackQuery().getMessage());
                bot.sendMessage(usr.getId(), "Укажите время выезда");
                usr.nextStep();
                break;
            case 1:
                if(update.hasMessage()){
                    if(update.getMessage().hasText()){

                        try {
                            passPostService.getNewByUsr(usr)
                                    .setGoTime(LocalTime.from(
                                            DateTimeFormatter.ofPattern("H:mm")
                                                    .parse(update.getMessage().getText())));
                        }catch (DateTimeParseException e){
                            bot.sendMessage(usr.getId(), "Укажите время следуя примеру 15:25");
                            return;
                        }
                        usr.nextStep();
                        bot.sendMessage(usr.getId(), "Укажите место начала поездки");
                    }
                }
                break;
            case 2:
                if(update.hasMessage() && update.getMessage().hasText()) {

                    passPostService.getNewByUsr(usr).setStartCity(update.getMessage().getText());

                    usr.nextStep();
                    bot.sendMessage(usr.getId(), "Укажите место конца поездки");
                }
                break;
            case 3:
                if(update.hasMessage() && update.getMessage().hasText()) {

                    PassPost passPost = passPostService.getNewByUsr(usr);
                    passPost.setFinishCity(update.getMessage().getText());
                    usr.nextStep();

                    bot.sendMessage(usr.getId(), passPost.toString(), getConfirmationKeyboard());
                }
                break;
            case 4:
                if(update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("Confirm")) {
                    bot.delMessage(update.getCallbackQuery().getMessage());

                    PassPost passPost = passPostService.getNewByUsr(usr);
                    passPost.setActive(true);
                    usr.nextStep();

                    bot.sendMessage(bot.channelId, passPost.toString());
                    bot.sendMessage(usr.getId(), "Отправлено", getKeyboard());
                }
                break;
        }
    }

    private InlineKeyboardMarkup getConfirmationKeyboard(){
        List<List<InlineKeyboardButton>> rowList = Arrays.asList(
                Collections.singletonList(KeyboardTools.CallbackButton.apply("Подтвердить", "Confirm")),
                Collections.singletonList(KeyboardTools.CallbackButton.apply("Отклонить", StartMenu.class.getSimpleName()))
        );
        return new InlineKeyboardMarkup(rowList);
    }

    private InlineKeyboardMarkup getKeyboard(){
        List<List<InlineKeyboardButton>> rowList = Collections.singletonList(
                Collections.singletonList(KeyboardTools.CallbackButton.apply("В меню", StartMenu.class.getSimpleName()))
        );
        return new InlineKeyboardMarkup(rowList);
    }
}
