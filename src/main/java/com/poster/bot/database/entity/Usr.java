package com.poster.bot.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table
public class Usr {
    @Id
    private long id;

    private int step;

    private String mode;

    public void nextStep(){
        step++;
    }
}
