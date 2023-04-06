package com.app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Player {
    private String userName;
    private Arrow arrow;
    public Label name, scores, shots;
    public Player(String _userName, Arrow _arrow, Label _name, Label _scores, Label _shots){
        userName = _userName;
        arrow = _arrow;
        name = _name;
        name.setText(userName);
        scores = _scores;
        shots = _shots;
    }
    public String GetUserName(){ return userName; }
    public Arrow GetArrow(){ return arrow; }
    public void IncreaseScores(int s){ shots.setText(String.valueOf(Integer.parseInt(shots.getText()) + s)); }
    public void IncreaseShots(){ shots.setText(String.valueOf(Integer.parseInt(shots.getText()) + 1)); }
}
