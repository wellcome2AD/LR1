package com.example.lr1;

public interface Observer {
    public void ScoresChanged(int scores);
    public void ShotsChanged();
    public void ArrowIsShot(boolean value);
}
