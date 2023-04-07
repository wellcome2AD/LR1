package com.server;

import com.app.Observer;
import com.client.Client;
import com.client.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientAtServer implements Runnable{
    Socket cs;
    Server s;
    DataOutputStream dos;
    DataInputStream dis;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    final private ArrayList<Observer> allObservers = new ArrayList<>();
    public void AddObserver(Observer o) {
        allObservers.add(o);
    }
    public ClientAtServer(Socket _cs, Server _ms){
        cs = _cs;
        s = _ms;

        OutputStream os = null;
        try {
            os = cs.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dos = new DataOutputStream(os);
    }
    @Override
    public void run() {
        try {
            InputStream is = cs.getInputStream();
            dis = new DataInputStream(is);
            while(true)
            {
                ReceiveFromClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void SendToSocket(Response r){
        try {
            dos.writeUTF(gson.toJson(r));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void ReceiveFromClient() throws IOException {
        String s = dis.readUTF();
        Request r = gson.fromJson(s, Request.class);
        System.out.println("Request: " + r);
        HandleRequest(r);
    }
    private void HandleRequest(Request r){
        var request_type = r.getMessage();
        switch (request_type){
            case isNameUnique -> {
                System.out.println("HandleRequest: isNameUnique");
                var client_name = (String)r.getData();
                boolean isNameUnique = !s.FindName(client_name);
                if(isNameUnique) {
                    s.AddName(client_name);
                    for (var o : allObservers)
                        Platform.runLater(() -> o.AddPlayer(client_name));
                }
                SendToSocket(new Response(Response.respType.isNameUnique, client_name, isNameUnique));
                s.Broadcast(new Response(Response.respType.newPlayer, null, client_name));
            }
            case getAllPlayers -> {
                SendToSocket(new Response(Response.respType.allPlayers, r.getClientName(), s.GetAllNames()));
            }
            case playerIsReady -> {
                for(var o : allObservers){
                    System.out.println("PlayerIsReady");
                    Platform.runLater(() ->{ o.OnStartGame(r.getClientName()); });
                }
            }
            case pauseGame -> {
                for (var o : allObservers)
                    Platform.runLater(() ->{ o.OnPauseGame(r.getClientName());});
                s.Broadcast(new Response(Response.respType.pauseGame, null, null));
            }
            case arrowIsShot -> {
                for (var o : allObservers) {
                    Platform.runLater(() -> { o.OnShot(r.getClientName()); });
                }
            }
        }
    }
}
