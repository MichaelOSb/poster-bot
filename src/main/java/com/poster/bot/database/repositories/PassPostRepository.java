package com.poster.bot.database.repositories;

import com.poster.bot.database.entity.PassPost;
import com.poster.bot.database.entity.Usr;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassPostRepository extends CrudRepository<PassPost, Long> {
    void deleteAllByActiveFalseAndUsr(Usr usr);
    Optional<PassPost> findByUsrAndActiveFalse(Usr usr);
}
