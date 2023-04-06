package com.server;

import com.app.*;
import com.client.Client;
import com.client.Request;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import static com.client.Request.message.isNameUnique;

public class ServerFrameController implements Observer, FrameController {
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
    private Client cl;
    @FXML
    protected void onStartGameButtonClick() {
        scores.setText("0");
        shots.setText("0");
        big_target.setCenterY(0);
        small_target.setCenterY(0);

        if(anim == null){
            anim = new Animation(big_target, small_target,null);
            anim.AddObserver(this);
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

    @Override
    public void ScoresChanged(int scores) {
        IncreaseScores(scores);
    }

    @Override
    public void ShotsChanged() {
        IncreaseShots();
    }
    @Override
    public void ArrowIsShot(boolean value){
        shot_button.setDisable(value);
    }

    @Override
    public void IncreaseScores(int _scores, Player p) {

    }

    @Override
    public void IncreaseShots(Player p) {

    }

    public void IncreaseScores(int _scores){ scores.setText(String.valueOf(Integer.parseInt(scores.getText()) + _scores));}
    public void IncreaseShots(){ shots.setText(String.valueOf(Integer.parseInt(shots.getText()) + 1));}

    @Override
    public void AddPlayer(String s) {

    }
}
