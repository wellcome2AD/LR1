package com.server;

import com.client.Client;
import com.client.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class clientAtServer implements Runnable{
    Socket cs;
    Server s;
    DataOutputStream dos;
    DataInputStream dis;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public clientAtServer(Socket _cs, Server _ms){
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
            System.out.println("Hello");
            InputStream is = cs.getInputStream();
            System.out.println("hihi");
            dis = new DataInputStream(is);
            System.out.println("haha");
            while(true)
            {
                System.out.println("1");
                String s = dis.readUTF();
                System.out.println("2");
                Request r = gson.fromJson(s, Request.class);
                System.out.println("3");
                HandleRequest(r);
                System.out.println("Request: " + r);
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
                var client_name = (String)r.getData();
                boolean isNameUnique = true;
                for(var c : s.GetAllClients()){
                    if(c.getUserName().equals(client_name)){
                        isNameUnique = false;
                        break;
                    }
                }
                if(isNameUnique) {
                    s.AddClient(new Client(client_name));
                }
                SendToSocket(new Response(Response.respType.isNameUnique, isNameUnique));
            }
            case arrowIsShot -> {

            }
        }
    }
}
