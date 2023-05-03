package com.client;

import com.app.*;
import com.database.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.server.Response;
import javafx.application.Platform;
import javafx.util.Pair;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.client.Request.message.isNameUnique;

public class Client {
    private String playerName;
    final private FrameController fc;
    private Socket socketAtClient;
    private DataInputStream dis;
    private DataOutputStream dos;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    final private ArrayList<Observer> allObservers = new ArrayList<>();
    public Client(String _playerName, FrameController _fc) {
        playerName = _playerName;
        fc = _fc;
    }
    public String GetPlayerName(){
        return playerName;
    }
    public void ConnectClient(){
        try {
            InetAddress ip = InetAddress.getLocalHost();
            int port = 3125;
            socketAtClient = new Socket(ip, port);
            System.out.println("Client connected. Port " + socketAtClient.getPort());

            var os = socketAtClient.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void SendToServer(Request m){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String m_to_s = gson.toJson(m);
        try {
            dos.writeUTF(m_to_s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response ReceiveFromServer(){
        Response r;
        try {
            InputStream is = socketAtClient.getInputStream();
            dis = new DataInputStream(is);
            while(dis.available() == 0)
            {
                Thread.sleep(10);
            }
            r = gson.fromJson(dis.readUTF(), Response.class);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return r;
    }
    public void AddObserver(Observer o) {
        allObservers.add(o);
    }
    public void HandleResponse(Response r){
        var response_type = r.getRType();
        switch (response_type){
            case isNameUnique -> {
                if(!(Boolean)r.getData()) {
                    MyDialog dialog = new MyDialog("Такое имя уже есть, введите другое");
                    dialog.getResult().ifPresent(name -> playerName = name);
                    SendToServer(new Request(isNameUnique, null, playerName));
                    HandleResponse(ReceiveFromServer());
                }
                else{
                    playerName = r.getClientName();
                    SendToServer(new Request(Request.message.getAllPlayers, playerName, null));
                }
            }
            case newPlayer -> Platform.runLater(() -> SendToServer(new Request(Request.message.getAllPlayers, playerName, null)));
            case allPlayers -> {
                for (var name : (ArrayList<String>) r.getData()) {
                    Platform.runLater(() -> fc.AddPlayer(name));
                }
            }
            case bigTargetCords -> {
                var yCord = (Double)r.getData();
                for (var o : allObservers) {
                    Platform.runLater(() -> o.TargetMove(target.bigTarget, yCord));
                }
            }
            case smallTargetCords -> {
                var yCord = (Double)r.getData();
                for (var o : allObservers) {
                    Platform.runLater(() -> o.TargetMove(target.smallTarget, yCord));
                }
            }
            case arrowCords -> {
                var listCords = (ArrayList<ArrayList<Double>>)r.getData();
                var headCords = listCords.get(0);
                var lineCords = listCords.get(1);
                for (var o : allObservers) {
                    Platform.runLater(() -> o.ArrowMove(r.getClientName(), headCords, lineCords));
                }
            }
            case scoresNum -> {
                for (var o : allObservers) {
                    Platform.runLater(() -> o.ScoresChanged(r.getClientName(), (String) r.getData()));
                }
            }
            case shotsNum -> {
                for (var o : allObservers) {
                    Platform.runLater(() -> o.ShotsChanged(r.getClientName()));
                }
            }
            case startGame -> {
                for (var o : allObservers) {
                    Platform.runLater(() -> o.OnStartGame(r.getClientName()));
                }
            }
            case pauseGame -> {
                if(playerName.equals(r.getClientName())) {
                    for (var o : allObservers) {
                        Platform.runLater(() -> o.OnPauseGame(r.getClientName()));
                    }
                }
            }
            case winGame -> {
                for (var o : allObservers) {
                    Platform.runLater(() -> o.OnWinGame((String)r.getData()));
                }
            }
            case records->{
                var res = new ArrayList<Player>();
                for(var element : (List<Map<String, Object>>)r.getData()){
                    res.add(new Player((String)element.get("player_name"), (int)(double)((Double)element.get("win_count")) ));
                }
                for (var o : allObservers) {
                    Platform.runLater(() -> o.OnRecords(res));
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + response_type);
        }
    }
    @Override
    public String toString(){
        return "Client{" + "playerName=" + playerName + '}';
    }

    public static void main(String[] args){
        ClientFrame.StartApp();
    }
}
