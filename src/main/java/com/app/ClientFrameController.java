package com.app;

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
    private Label player0NameLabel, player1NameLabel, player2NameLabel, player3NameLabel;
    @FXML
    private Label player0_scores, player1_scores, player2_scores, player3_scores;
    @FXML
    private Label player0_shots,  player1_shots,  player2_shots,  player3_shots, winLabel;
    @FXML
    public Button shot_button, start_game_button, stop_game_button;
    private Client cl;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    @FXML
    protected void initialize(){
        MyDialog dialog = new MyDialog(null);
        dialog.getResult().ifPresent(name -> cl = new Client(name, this));
        cl.ConnectClient();
        cl.AddObserver(this);
        cl.SendToServer(new Request(isNameUnique, null, cl.GetPlayerName()));
        cl.HandleResponse(cl.ReceiveFromServer());
        new Thread(()-> {
                while(true) {
                    cl.HandleResponse(cl.ReceiveFromServer());
                }
            }
        ).start();
        stop_game_button.setDisable(true);
    }
    @FXML
    protected void onStartGameButtonClick() {
        cl.SendToServer(new Request(Request.message.playerIsReady, cl.GetPlayerName(), null));
        start_game_button.setDisable(true);
        stop_game_button.setDisable(false);
    }
    @FXML
    protected void onStopGameButtonClick()
    {
        cl.SendToServer(new Request(Request.message.pauseGame, cl.GetPlayerName(), null));
        start_game_button.setDisable(false);
        stop_game_button.setDisable(true);
        shot_button.setDisable(true);
    }
    @FXML
    protected void OnShotButtonClick() {
        shot_button.setDisable(true);
        cl.SendToServer(new Request(Request.message.arrowIsShot, cl.GetPlayerName(), null));
    }
    @Override
    public void SetScores(String _scores, Player p) {
        var scores_label = p.GetScoresLabel();
        scores_label.setText(_scores);
    }
    @Override
    public void IncreaseShots(Player p){
        var shots_label = p.GetShotsLabel();
        shots_label.setText(String.valueOf(Integer.parseInt(shots_label.getText()) + 1));
    }
    @Override
    public void AddPlayer(String playerName) {
        ArrayList<Line> arrowLines = new ArrayList<>(Arrays.asList(player0_arrow1, player1_arrow1, player2_arrow1, player3_arrow1));
        ArrayList<Polygon> arrowHeads = new ArrayList<>(Arrays.asList(player0_arrow2, player1_arrow2, player2_arrow2, player3_arrow2));
        ArrayList<Label> nameLabels = new ArrayList<>(Arrays.asList(player0_name, player1_name, player2_name, player3_name));
        ArrayList<Label> nameLabels1 = new ArrayList<>(Arrays.asList(player0NameLabel, player1NameLabel, player2NameLabel, player3NameLabel));
        ArrayList<Label> scoresLabels = new ArrayList<>(Arrays.asList(player0_scores, player1_scores, player2_scores, player3_scores));
        ArrayList<Label> shotsLabels = new ArrayList<>(Arrays.asList(player0_shots,  player1_shots,  player2_shots,  player3_shots));

        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(playerName)) {
                return;
            }
        }

        int playerNumber = allPlayers.size();
        Arrow a = new Arrow(arrowLines.get(playerNumber), arrowHeads.get(playerNumber));
        a.setVisible(true);

        Player p = new Player(playerName, a, nameLabels.get(playerNumber), scoresLabels.get(playerNumber), shotsLabels.get(playerNumber));
        nameLabels.get(playerNumber).setText(playerName);
        if(playerName.equals(cl.GetPlayerName()))
            nameLabels.get(playerNumber).setStyle("-fx-font-weight: bold;");

        nameLabels1.get(playerNumber).setText(playerName);
        if(playerName.equals(cl.GetPlayerName()))
            nameLabels1.get(playerNumber).setStyle("-fx-font-weight: bold;");

    scoresLabels.get(playerNumber).setText("0");
        shotsLabels.get(playerNumber).setText("0");
        allPlayers.add(p);
    }

    @Override
    public void ArrowMove(String user_name, ArrayList<Double> headCords,  ArrayList<Double> lineCords) {
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(user_name)) {
                player = p;
            }
        }
        player.GetArrow().SetHeadCords(headCords);
        player.GetArrow().SetLineCords(new Pair<>(lineCords.get(0), lineCords.get(1)));
    }

    @Override
    public void TargetMove(target targetType, double yCord) {
        if(targetType == target.smallTarget)
            small_target.setCenterY(yCord);
        else {
            big_target.setCenterY(yCord);
        }
    }

    @Override
    public void OnStartGame(String playerName) {
        start_game_button.setDisable(true);
        stop_game_button.setDisable(false);
        shot_button.setDisable(false);
    }

    @Override
    public void OnPauseGame(String playerName) {
        start_game_button.setDisable(false);
        stop_game_button.setDisable(true);
    }
    @Override
    public void OnWinGame(String playerName){
        winLabel.setText(winLabel.getText() + playerName);
        winLabel.setVisible(true);
        start_game_button.setDisable(true);
        stop_game_button.setDisable(true);
        shot_button.setDisable(true);
    }

    public void OnShot(String playerName) {
        shot_button.setDisable(true);
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
    public void ShotsChanged(String playerName) {
        shot_button.setDisable(false);
        Player player = null;
        for(var p : allPlayers) {
            if (p.GetPlayerName().equals(playerName)) {
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
}
