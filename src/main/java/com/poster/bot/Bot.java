package com.poster.bot;

import com.poster.bot.handlers.UserMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {
    private final UserMapper userMapper;

    @Value("${bot.token}")
    private String BOT_TOKEN;

    @Value("${bot.channelId}")
    public long channelId;

    @Lazy
    public Bot(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        userMapper.handle(update);
    }
/***********************************************************/

    public Message sendMessage(Long chatId, String text, ReplyKeyboard keyboard){
        Message message = null;
        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(String.valueOf(chatId));
        sm.setReplyMarkup(keyboard);
        message = send(sm);
        return message;
    }

    public Message sendMessage(long chatId, String text){
        Message message = null;
        SendMessage sm = new SendMessage();
        sm.setText(text);
        sm.setChatId(String.valueOf(chatId));
        message = send(sm);
        return message;
    }
    public Message send(SendMessage sendMessage){
        Message message = null;
        try {
           message =  execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return message;
    }
    public void editMessage(Message message, String text, InlineKeyboardMarkup inlineKeyboard) {
        try {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(String.valueOf(message.getChatId()));
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setText(text);
            editMessageText.setReplyMarkup(inlineKeyboard);

            execute(editMessageText);

        } catch (TelegramApiException e) {e.printStackTrace();}
    }
    public void delMessage(Message message){
        delMessage(message.getChatId(), message.getMessageId());
    }
    public void delMessage(long chatId, int messageId){
        try {
            execute(new DeleteMessage(String.valueOf(chatId), messageId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @SneakyThrows
    public void editMessageReplyMarkup(Message message, InlineKeyboardMarkup keyboard){
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(String.valueOf(message.getChatId()));
        editMessageReplyMarkup.setMessageId(message.getMessageId());
        editMessageReplyMarkup.setReplyMarkup(keyboard);
        execute(editMessageReplyMarkup);
    }
    public Message sendMessage(long usrId, String text, int replyToMessageId){
        SendMessage sendMessage = new SendMessage(String.valueOf(usrId), text);
        sendMessage.setReplyToMessageId(replyToMessageId);
        return send(sendMessage);
    }
    public Message sendMessage(long usrId, String text, int replyToMessageId, ReplyKeyboard replyKeyboard){
        SendMessage sendMessage = new SendMessage(String.valueOf(usrId), text);
        sendMessage.setReplyToMessageId(replyToMessageId);
        sendMessage.setReplyMarkup(replyKeyboard);
        return send(sendMessage);
    }
}
