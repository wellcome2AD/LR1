package com.app;

import javafx.util.Pair;

import java.util.ArrayList;

public interface Observer {
    public void ScoresChanged(String userName, int scores);
    public void ShotsChanged(String userName);
    public void ArrowIsShot(boolean value);
    public void AddPlayer(String user_name);
    public void ArrowMove(String user_name, ArrayList<Double> headCords, Pair<Double, Double> lineCords);
    public void TargetMove(target targetType, double yCord);
    public void OnPauseGame();
    public void OnShot(String userName);
}
