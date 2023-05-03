package com.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PlayerDAO implements IPlayerDAO {

    @Override
    public Player findByPlayerName(String player_name) {
        var list = findAll();
        for (var element : list)
        {
            if(element.GetPlayerName().equals(player_name))
            {
                return element;
            }
        }
        return null;
    }

    @Override
    public void save(Player player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(player);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Player player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(player);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Player player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(player);
        tx1.commit();
        session.close();
    }

    @Override
    public List<Player> findAll() {
        return (List<Player>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Player").list();
    }
}