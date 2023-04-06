package com.server;

import com.client.Client;
import com.client.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;

public class ClientAtServer implements Runnable{
    Socket cs;
    Server s;
    DataOutputStream dos;
    DataInputStream dis;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
                String s = dis.readUTF();
                Request r = gson.fromJson(s, Request.class);
                System.out.println("Request: " + r);
                HandleRequest(r);
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
    private void HandleRequest(Request r){
        var request_type = r.getMessage();
        switch (request_type){
            case isNameUnique -> {
                System.out.println("HandleRequest: isNameUnique");
                var client_name = (String)r.getData();
                boolean isNameUnique = !s.FindName(client_name);
                if(isNameUnique) {
                    s.AddName(client_name);
                }
                SendToSocket(new Response(Response.respType.isNameUnique, client_name, isNameUnique));
                s.Broadcast(new Response(Response.respType.newPlayer, null, client_name));
            }
            case getAllPlayers -> {
                SendToSocket(new Response(Response.respType.allPlayers, r.getClientName(), s.allNames));
            }
            case arrowIsShot -> {
                s.Broadcast(new Response(Response.respType.arrowCords, r.getClientName(), new Pair<Float, Float>(0.0F, 0.0F))); // to do
            }
        }
    }
}
