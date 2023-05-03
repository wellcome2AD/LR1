package com.database;

import java.util.List;

public interface IPlayerDAO {
    Player findByPlayerName(String player_name);
    void save(Player entry);
    void update(Player entry);
    void delete(Player user);
    List<Player> findAll();
}
