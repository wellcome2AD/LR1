package com.server;

import com.client.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    int port = 3124;
    InetAddress ip = null;
    ExecutorService service = Executors.newCachedThreadPool();

    ArrayList<ClientAtServer> allClientsHandlers = new ArrayList<>();
    ArrayList<String> allNames = new ArrayList<>();
    void StartServer(){
        int number = 0;

        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            ServerSocket ss = new ServerSocket(port, 0, ip);
            System.out.println("Server started");

            while(true){
                ++number;
                Socket cs =  ss.accept();
                System.out.println("Client connected. Port " + cs.getPort());
                ClientAtServer c = new ClientAtServer(cs, this);
                allClientsHandlers.add(c);
                //service.execute(c);
                new Thread(c).start();
            }
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
    public void Broadcast(Response r){
        for(var cl : allClientsHandlers){
            cl.SendToSocket(r);
        }
    }
    public static void main(String[] args){
        new Server().StartServer();
    }
}
