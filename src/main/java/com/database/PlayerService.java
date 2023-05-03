package com.database;

import java.util.List;

public class PlayerService {
    private final IPlayerDAO m_playerDao = new PlayerDAO();

    public PlayerService() {
    }

    public Player findPlayer(String player_name) {
        return m_playerDao.findByPlayerName(player_name);
    }

    public void savePlayer(Player player) {
        m_playerDao.save(player);
    }

    public void deletePlayer(Player player) {
        m_playerDao.delete(player);
    }

    public void updatePlayer(Player player) {
        m_playerDao.update(player);
    }

    public List<Player> findAllPlayers() {
        return m_playerDao.findAll();
    }
}