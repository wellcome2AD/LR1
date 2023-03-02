package com.example.lr1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class MainController implements Observer{
    @FXML
    private Circle big_target, small_target;
    @FXML
    private Line arrow1;
    @FXML
    private Polygon arrow2;
    @FXML
    private Label scores, shots;
    @FXML
    public Button shot_button;
    private Animation anim;
    @FXML
    protected void onStartGameButtonClick() {
        scores.setText("0");
        shots.setText("0");
        big_target.setCenterY(0);
        small_target.setCenterY(0);

        if(anim == null){
            anim = new Animation(this, big_target, small_target, arrow1, arrow2);
            Thread anim_thread = new Thread(anim);
            anim_thread.start();
            shot_button.setDisable(false);
        }

        anim.resetAnimation();
    }
    @FXML
    protected void onStopGameButtonClick()
    {
        anim.stopAnimation();
        shot_button.setDisable(true);
    }
    @FXML
    protected void OnShotButtonClick(){
        anim.makeShot();
        shot_button.setDisable(true);
    }
    public void increaseScores(int _scores){ scores.setText(String.valueOf(Integer.parseInt(scores.getText()) + _scores));}
    public void increaseShots(){ shots.setText(String.valueOf(Integer.parseInt(shots.getText()) + 1));}

    @Override
    public void ScoresChanged(int scores) {
        increaseScores(scores);
    }

    @Override
    public void ShotsChanged() {
        increaseShots();
    }
    @Override
    public void ArrowIsShot(boolean value){
        shot_button.setDisable(value);
    }
}
