package com.poster.bot.services;

import com.poster.bot.database.entity.Usr;
import com.poster.bot.database.repositories.UsrRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
public class UsrService {
    public UsrRepository usrRepository;

    UsrService(UsrRepository usrRepository){
        this.usrRepository = usrRepository;
    }

    public Usr getUsr(User user){
        Optional<Usr> usrOptional = usrRepository.findById(Long.valueOf(user.getId()));
        if(!usrOptional.isPresent()){
            Usr usr = new Usr();
            usr.setId(user.getId());
            return usrRepository.save(usr);
        }else
            return usrOptional.get();
    }


}
