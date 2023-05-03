package com.server;

import com.app.FrameController;
import com.app.Observer;
import com.database.PlayerService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {
    public final PlayerService player_service = new PlayerService();
    final private ArrayList<ClientAtServer> allClientsHandlers = new ArrayList<>();
    final private ArrayList<String> allNames = new ArrayList<>();
    final private ArrayList<Observer> allObservers = new ArrayList<>();
    final private FrameController fc;
    public Server(FrameController _fc){
        fc = _fc;
    }
    public void AddObserver(Observer o) {
        allObservers.add(o);
    }
    void StartServer(){
        int port = 3125;
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            ServerSocket ss = new ServerSocket(port, 0, ip);
            System.out.println("Server started");

            new Thread(() -> {
                int number = 0;
                while (true) {
                    if(number < 4) {
                        Socket cs = null;
                        try {
                            cs = ss.accept();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        ++number;
                        System.out.println("Client #" + number + " is connected. Port " + cs.getPort());
                        ClientAtServer c = new ClientAtServer(cs, this);
                        allClientsHandlers.add(c);
                        for (var o : allObservers) {
                            c.AddObserver(o);
                        }
                        new Thread(c).start();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void AddName(String s){
        allNames.add(s);
    }
    public boolean FindName(String name){
        return allNames.contains(name);
    }
    public ArrayList<String> GetAllNames() { return allNames; }
    public void Broadcast(Response r){
        for(var cl : allClientsHandlers){
            cl.SendToSocket(r);
        }
    }
    public static void main(String[] args){
        ServerFrame.StartApp();
    }
}
