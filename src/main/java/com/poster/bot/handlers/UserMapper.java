package com.poster.bot.handlers;

import com.poster.bot.database.entity.Usr;
import com.poster.bot.database.repositories.UsrRepository;
import com.poster.bot.handlers.tools.Handler;
import com.poster.bot.handlers.tools.Menu;
import com.poster.bot.handlers.menus.StartMenu;
import com.poster.bot.services.UsrService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Component
public class UserMapper {
    private final UsrService usrService;
    private Map<String, Handler> handlerMap;


    public UserMapper(UsrService usrService, Map<String, Handler> handlerMap){
        this.usrService = usrService;
        this.handlerMap = handlerMap;
    }

    @Transactional
    public void handle(Update update){
        Usr usr = null;
        if(update.hasMessage())
            usr = usrService.getUsr(update.getMessage().getFrom());
        if(update.hasCallbackQuery())
            usr = usrService.getUsr(update.getCallbackQuery().getFrom());

        if(usr==null) return;

        Handler handler;//will be used to handle the update

        String data = null;//for mode switching

        if (update.hasCallbackQuery())
            data = toLowerCamelCase.apply(update.getCallbackQuery().getData());

        if(update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start"))
            data = StartMenu.class.getSimpleName();

        if(data != null){
            handler = findHandler.apply(data);
            if(handler != null){    //if mode or menu is found
                if(!Arrays.asList(handler.getClass().getInterfaces()).contains(Menu.class)){
                    usr.setStep(0);//if found menu is mode - set step to 0
                }
                usr.setMode(data);

                handler.handle(update, usr);//goto handler
                return; //end the current iteration, cause update is already handled
            }
        }

        handler = findHandler.apply(usr.getMode());
        if(handler != null)
            handler.handle(update, usr);
        else
            handlerMap.get(toLowerCamelCase.apply(StartMenu.class.getSimpleName())).handle(update, usr);
    }


    UnaryOperator<String> toLowerCamelCase = in -> {
        if(in == null && in.length() == 0)
            return null;
        if(in.length() == 1)
            return in.toLowerCase();
        return Character.toLowerCase(in.charAt(0))+in.substring(1);
    };

    Function<String, Handler> findHandler = userMode -> {
        for (Map.Entry<String, Handler> entry : handlerMap.entrySet()) {
            if((toLowerCamelCase.apply(userMode)).startsWith(entry.getKey()))
                return entry.getValue();
        }
        return null;
    };

}