package com.database;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "high_scores_table")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String player_name;
    private int win_count;

    public Player() {
    }

    public Player(String player_name, int win_count) {
        this.player_name = player_name;
        this.win_count = win_count;
    }

    public int getId() {
        return id;
    }

    public String GetPlayerName() {
        return player_name;
    }

    public void SetPlayerName(String player_name) {
        this.player_name = player_name;
    }

    public int GetWinCount() {
        return win_count;
    }

    public void SetWinCount(int win_count) {
        this.win_count = win_count;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", player_name='" + player_name + '\'' +
                ", win_count=" + win_count +
                '}';
    }
}
