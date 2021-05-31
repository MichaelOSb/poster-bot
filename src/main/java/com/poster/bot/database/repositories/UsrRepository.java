package com.poster.bot.database.repositories;

import com.poster.bot.database.entity.Usr;
import org.springframework.data.repository.CrudRepository;

public interface UsrRepository  extends CrudRepository<Usr, Long> {
}
