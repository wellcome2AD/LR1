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
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static com.client.Request.message.isNameUnique;
import static com.server.Response.respType.bigTargetCords;
import static com.server.Response.respType.smallTargetCords;

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
    private ArrayList<Player> allPlayers = new ArrayList<>(); // Player содержатся в Animation
    private ArrayList<ServerAnimation> allAnims;
    private Server s;
    @FXML
    protected void initialize(){
        s = new Server();
        s.AddObserver(this);
        s.StartServer();
    }
    protected void onStartGame(String playerName) {
        Player player = null;
        for(var p : allPlayers){
            if(p.GetUserName().equals(playerName)) {
                player = p;
            }
        }
        player.scores.setText("0");
        player.shots.setText("0");

        big_target.setCenterY(0);
        small_target.setCenterY(0);

        /*if(allAnims.size() == 0){
            int i = 0;
            for(var p : allPlayers) {
                allAnims.add(new ServerAnimation(big_target, small_target, p));
                allAnims.get(i).AddObserver(this);
                new Thread(allAnims.get(i)).start();
            }
            shot_button.setDisable(false);
        }

        for(var a : allAnims) {
            a.resetAnimation();
        }*/
    }
    protected void onStopGame()
    {
        for(var a : allAnims) {
            a.stopAnimation();
        }
        shot_button.setDisable(true);
    }
    @Override
    public void OnShot(String userName){
        ServerAnimation anim = null;
        for(var a : allAnims) {
            if (a.GetPlayer().GetUserName().equals(userName)) {
                anim = a;
                break;
            }
        }
        anim.makeShot();
        shot_button.setDisable(true);
    }

    @Override
    public void ScoresChanged(String userName, int scores) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetUserName().equals(userName)) {
                player = p;
                break;
            }
        }
        IncreaseScores(scores, player);
    }

    @Override
    public void ShotsChanged(String userName) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetUserName().equals(userName)) {
                player = p;
                break;
            }
        }
        IncreaseShots(player);
    }
    @Override
    public void ArrowIsShot(boolean value){
        shot_button.setDisable(value);
    }

    @Override
    public void IncreaseScores(int _scores) { IncreaseScores(_scores,  allPlayers.get(0)); }
    @Override
    public void IncreaseScores(int _scores, Player p){
        var scores_label = p.GetScoresLabel();
        scores_label.setText(String.valueOf(Integer.parseInt(scores_label.getText()) + _scores));
        s.Broadcast(new Response(Response.respType.scoresNum, p.GetUserName(), scores_label.getText()));
    }
    @Override
    public void IncreaseShots(){ IncreaseShots(allPlayers.get(0));}
    @Override
    public void IncreaseShots(Player p){
        var shots_label = p.GetShotsLabel();
        shots_label.setText(String.valueOf(Integer.parseInt(shots_label.getText()) + 1));
        s.Broadcast(new Response(Response.respType.shotsNum, p.GetUserName(), shots_label.getText()));
    }

    @Override
    public void AddPlayer(String s) {

    }

    @Override
    public void ArrowMove(String userName, ArrayList<Double> headCords, Pair<Double, Double> lineCords) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetUserName().equals(userName)) {
                player = p;
                break;
            }
        }
        s.Broadcast(new Response(Response.respType.arrowCords, player.GetUserName(),
                new Pair<ArrayList<Double>, Pair<Double, Double>>(headCords, lineCords)));
    }

    @Override
    public void TargetMove(String userName, target targetType, double yCord) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetUserName().equals(userName)) {
                player = p;
                break;
            }
        }
        var rtype = targetType == target.smallTarget ? smallTargetCords : bigTargetCords;
        s.Broadcast(new Response(rtype, player.GetUserName(), yCord));
    }
}
