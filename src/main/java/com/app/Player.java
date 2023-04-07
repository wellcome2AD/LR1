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
        scores = _scores;
        shots = _shots;
        name.setVisible(true);
        scores.setVisible(true);
        shots.setVisible(true);
    }
    public String GetPlayerName(){ return userName; }
    public Arrow GetArrow(){ return arrow; }
    public Label GetScoresLabel(){ return scores; }
    public Label GetShotsLabel(){ return shots; }
}
