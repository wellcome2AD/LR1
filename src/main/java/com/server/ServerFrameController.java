package com.server;

import com.app.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static com.server.Response.respType.*;

public class ServerFrameController implements Observer, FrameController {
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
    final private ArrayList<Player> allPlayers = new ArrayList<>(); // Player содержатся в Animation
    final private ArrayList<Player> playersReady = new ArrayList<>();
    final private ArrayList<ServerAnimation> allAnims = new ArrayList<>();
    private Server s;
    @FXML
    protected void initialize(){
        s = new Server(this);
        s.AddObserver(this);
        s.StartServer();
    }
    public void OnStartGame(String playerName) {
        Player player = null;
        for(var p : allPlayers) {
            if(p.GetPlayerName().equals(playerName)) {
                player = p;
            }
        }
        System.out.println(playerName + " is ready");
        if(!playersReady.contains(player)) {
            playersReady.add(player);
        }
        System.out.println(playersReady.size());
        System.out.println(allPlayers.size());
        System.out.println(allAnims.size());
        if(playersReady.size() == allPlayers.size() && allAnims.size() == 0) {
            System.out.println("This code is not executed");
            int i = 0;
            for (var p : allPlayers) {
                allAnims.add(new ServerAnimation(big_target, small_target, p));
                allAnims.get(i).AddObserver(this);
                allAnims.get(i).resetAnimation();
                new Thread(allAnims.get(i)).start();
                ++i;
            }
        }
        if(playersReady.size() == allPlayers.size()) {
            s.Broadcast(new Response(startGame, null, null));
            for (var a : allAnims) a.resetAnimation();
        }
    }
    @Override
    public void AddPlayer(String s) {
        ArrayList<Line> arrowLines = new ArrayList<>(Arrays.asList(player0_arrow1, player1_arrow1, player2_arrow1, player3_arrow1));
        ArrayList<Polygon> arrowHeads = new ArrayList<>(Arrays.asList(player0_arrow2, player1_arrow2, player2_arrow2, player3_arrow2));
        ArrayList<Label> nameLabels = new ArrayList<>(Arrays.asList(player0_name, player1_name, player2_name, player3_name));
        ArrayList<Label> scoresLabels = new ArrayList<>(Arrays.asList(player0_scores, player1_scores, player2_scores, player3_scores));
        ArrayList<Label> shotsLabels = new ArrayList<>(Arrays.asList(player0_shots,  player1_shots,  player2_shots,  player3_shots));

        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(s)) { // самого себя нужно добавить только один раз
                return;
            }
        }
        int playerNumber = allPlayers.size();
        Arrow a = new Arrow(arrowLines.get(playerNumber), arrowHeads.get(playerNumber));
        a.setVisible(true);

        Player p = new Player(s, a, nameLabels.get(playerNumber), scoresLabels.get(playerNumber), shotsLabels.get(playerNumber));
        nameLabels.get(playerNumber).setText(s);
        scoresLabels.get(playerNumber).setText("0");
        shotsLabels.get(playerNumber).setText("0");
        allPlayers.add(p);
    }
    @Override
    public void OnPauseGame(String playerName)
    {
        Player player = null;
        for(var p : allPlayers) {
            if(p.GetPlayerName().equals(playerName)) {
                player = p;
            }
        }
        playersReady.remove(player);
        playersReady.trimToSize();
        for(var a : allAnims) {
            a.stopAnimation();
        }
    }
    @Override
    public void OnWinGame(String playerName) {
        for(var a : allAnims) {
            a.stopAnimation();
        }
    }
    @Override
    public void OnShot(String userName){
        ServerAnimation anim = null;
        for(var a : allAnims) {
            if (a.GetPlayer().GetPlayerName().equals(userName)) {
                System.out.println(a.GetPlayer().GetPlayerName() + " made a shot");
                anim = a;
                break;
            }
        }
        anim.makeShot();
    }

    @Override
    public void ScoresChanged(String userName, String scores) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(userName)) {
                player = p;
                break;
            }
        }
        SetScores(scores, player);
    }

    @Override
    public void ShotsChanged(String userName) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(userName)) {
                player = p;
                break;
            }
        }
        IncreaseShots(player);
    }
    @Override
    public void ArrowIsShot(boolean value) {} // отсутствует реализация для серверного контроллера
    @Override
    public void SetScores(String _scores, Player p){
        var scores_label = p.GetScoresLabel();
        var old_value = Integer.parseInt(scores_label.getText());
        var new_value = Integer.parseInt(_scores);
        scores_label.setText(String.valueOf(old_value + new_value));
        s.Broadcast(new Response(scoresNum, p.GetPlayerName(), scores_label.getText()));

        if(Integer.parseInt(_scores) >= 10){
            s.Broadcast(new Response(winGame, null, p.GetPlayerName()));
        }
    }
    @Override
    public void IncreaseShots(Player p){
        var shots_label = p.GetShotsLabel();
        shots_label.setText(String.valueOf(Integer.parseInt(shots_label.getText()) + 1));
        s.Broadcast(new Response(Response.respType.shotsNum, p.GetPlayerName(), shots_label.getText()));
    }

    @Override
    public void ArrowMove(String userName, ArrayList<Double> headCords, ArrayList<Double> lineCords) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(userName)) {
                player = p;
                break;
            }
        }
        s.Broadcast(new Response(Response.respType.arrowCords, player.GetPlayerName(),
                new ArrayList<>(Arrays.asList(headCords, lineCords))));
    }

    @Override
    public void TargetMove(target targetType, double yCord) {
        var rtype = targetType == target.smallTarget ? smallTargetCords : bigTargetCords;
        s.Broadcast(new Response(rtype, null, yCord));
    }
}
