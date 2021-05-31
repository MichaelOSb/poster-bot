package com.poster.bot.database.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@Table
public class PassPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalTime goTime;

    private String startCity;

    private String finishCity;


    private boolean active;

    @ManyToOne
    private Usr usr;

    public String toString(){
        return "Время: "+goTime+"\nНачало: "+ startCity+"\nКонец: "+finishCity;
    }
}
