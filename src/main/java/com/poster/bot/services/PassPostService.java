package com.poster.bot.services;

import com.poster.bot.database.entity.PassPost;
import com.poster.bot.database.entity.Usr;
import com.poster.bot.database.repositories.PassPostRepository;
import org.springframework.stereotype.Service;

@Service
public class PassPostService {
    private final PassPostRepository passPostRepository;

    public PassPostService(PassPostRepository passPostRepository) {
        this.passPostRepository = passPostRepository;
    }

    public PassPost getNewByUsr(Usr usr){
        return passPostRepository.findByUsrAndActiveFalse(usr).orElseGet(()->createNew(usr));
    }

    private PassPost createNew(Usr usr){
        passPostRepository.deleteAllByActiveFalseAndUsr(usr);
        PassPost passPost = new PassPost();
        passPost.setUsr(usr);
        return passPostRepository.save(passPost);
    }
}
