package com.app;

public interface Observer {
    public void ScoresChanged(int scores);
    public void ShotsChanged();
    public void ArrowIsShot(boolean value);
}
