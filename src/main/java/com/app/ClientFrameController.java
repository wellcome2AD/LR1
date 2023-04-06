package com.app;

import com.client.Client;
import com.client.Request;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;

import static com.client.Request.message.isNameUnique;

public class ClientFrameController implements Observer, FrameController{
    @FXML
    private Circle big_target, small_target;
    @FXML
    private Line player0_arrow1, player1_arrow1, player2_arrow1, player3_arrow1;
    @FXML
    private Polygon player0_arrow2, player1_arrow2, player2_arrow2, player3_arrow2;
    @FXML
    private Label player0_name, player1_name, player2_name, player3_name;
    @FXML
    private Label player0_scores, player1_scores, player2_scores, player3_scores, player0_shots,  player1_shots,  player2_shots,  player3_shots;
    @FXML
    public Button shot_button;
    private ArrayList<Animation> allAnims = new ArrayList<>();
    private Client cl;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    @FXML
    protected void initialize(){
        MyDialog dialog = new MyDialog(null);
        dialog.getResult().ifPresent(name -> cl = new Client(name, this));
        cl.ConnectClient();
        cl.AddObserver(this);
        cl.SendToServer(new Request(isNameUnique, null, cl.getUserName()));
        cl.HandleResponse(cl.ReceiveFromServer());
        new Thread(()-> {
                while(true) {
                    cl.HandleResponse(cl.ReceiveFromServer());
                }
            }
        ).start();
    }
    @FXML
    protected void onStartGameButtonClick() {
        cl.SendToServer(new Request(Request.message.playerIsReady, cl.getUserName(), null));
        for (var player : allPlayers) {
            player.scores.setText("0");
            player.shots.setText("0");
        }
        big_target.setCenterY(0);
        small_target.setCenterY(0);

        cl.SendToServer(new Request(Request.message.playerIsReady, allPlayers.get(0).GetUserName(), null));
        /*if(allAnims.size() == 0){
            int i = 0;
            for(var p : allPlayers) {
                allAnims.add(new Animation(big_target, small_target, p));
                allAnims.get(i).AddObserver(this);
                new Thread(allAnims.get(i)).start();
            }
            shot_button.setDisable(false);
        }
        for(var a : allAnims) {
            a.resetAnimation();
        }*/
    }
    @FXML
    protected void onStopGameButtonClick()
    {
        cl.SendToServer(new Request(Request.message.pauseGame, allPlayers.get(0).GetUserName(), null));
        shot_button.setDisable(true);
    }
    @FXML
    protected void OnShotButtonClick() {
        //anim.makeShot();
        cl.SendToServer(new Request(Request.message.arrowIsShot, allPlayers.get(0).GetUserName(), null));
        shot_button.setDisable(true);
    }
    @Override
    public void IncreaseScores(int _scores) { IncreaseScores(_scores,  allPlayers.get(0)); }
    @Override
    public void IncreaseScores(int _scores, Player p){ p.IncreaseScores(_scores);}
    @Override
    public void IncreaseShots(){ IncreaseShots(allPlayers.get(0));}

    @Override
    public void AddPlayer(String s) {
        for(var p : allPlayers) {
            if (p.GetUserName().equals(s)) { // самого себя нужно добавить только один раз
                return;
            }
        }
        Arrow a = null;
        Player p = null;
        switch(allPlayers.size()) {
            case 0 -> {
                a = new Arrow(player0_arrow1, player0_arrow2);
                p = new Player(s, a, player0_name, player0_scores, player0_shots);
                Platform.runLater(() ->player0_name.setText(s));
            }
            case 1 -> {
                a = new Arrow(player1_arrow1, player1_arrow2);
                p = new Player(s, a, player1_name, player1_scores, player1_shots);
                Platform.runLater(() ->player1_name.setText(s));
            }
            case 2 -> {
                a = new Arrow(player2_arrow1, player2_arrow2);
                p = new Player(s, a, player2_name, player2_scores, player2_shots);
                Platform.runLater(() ->player2_name.setText(s));
            }
            case 3 -> {
                a = new Arrow(player3_arrow1, player3_arrow2);
                p = new Player(s, a, player3_name, player3_scores, player3_shots);
                Platform.runLater(() ->player3_name.setText(s));
            }
        }
        allPlayers.add(p);
    }

    @Override
    public void IncreaseShots(Player p){ p.IncreaseShots();}

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
}
