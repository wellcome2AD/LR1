package com.app;

import javafx.util.Pair;

public interface FrameController {
    public void IncreaseScores(int _scores, Player p);
    public void IncreaseShots(Player p);

    void IncreaseScores(int data);

    void IncreaseShots();
    void AddPlayer(String s);
}
