package com.app;

import javafx.util.Pair;

public interface FrameController {
    public void SetScores(String _scores, Player p);
    public void IncreaseShots(Player p);
    public void AddPlayer(String s);
}
