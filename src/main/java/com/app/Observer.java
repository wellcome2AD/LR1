package com.app;

import com.database.Player;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public interface Observer {
    public void ScoresChanged(String userName, String scores);
    public void ShotsChanged(String userName);
    public void ArrowIsShot(boolean value);
    public void AddPlayer(String user_name);
    public void ArrowMove(String user_name, ArrayList<Double> headCords,  ArrayList<Double>  lineCords);
    public void TargetMove(target targetType, double yCord);
    public void OnStartGame(String playerName);
    public void OnPauseGame(String playerName);
    public void OnWinGame(String playerName);
    public void OnRecords(List<Player> playersRecords);
    public void OnShot(String playerName);
}
