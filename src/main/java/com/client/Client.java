package com.client;

import com.app.MainFrame;
import com.app.MyDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.server.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static com.client.Request.message.isNameUnique;

public class Client {
    String user_name;
    int scores, shots;
    Socket socketAtClient;
    int port = 3124;
    InetAddress ip;
    DataInputStream dis;
    DataOutputStream dos;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Client(String _user_name) {
        user_name = _user_name;
        scores = shots = 0;
    }
    public String getUserName(){
        return user_name;
    }
    public int getScores(){
        return scores;
    }
    public int getShots(){
        return shots;
    }
    public void ConnectClient(){
        try {
            ip = InetAddress.getLocalHost();
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
        Response r = null;
        try {
            InputStream is = socketAtClient.getInputStream();
            dis = new DataInputStream(is);
            while(dis.available() == 0)
            {
                Thread.sleep(10);
            }
            r = gson.fromJson(dis.readUTF(), Response.class);
            System.out.println("Request: " + dis.readUTF());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return r;
    }
    public void HandleResponse(Response r){
        var response_type = r.getRType();
        System.out.println(response_type);
        switch (response_type){
            case isNameUnique -> {
                if((Boolean)r.getData() == false) {
                    MyDialog dialog = new MyDialog();
                    dialog.getResult().ifPresent(name -> user_name = name);
                    SendToServer(new Request(isNameUnique, user_name));
                    HandleResponse(ReceiveFromServer());
                }
            }
            case bigTargetCords -> {
                break;
            }
            case smallTargetCords -> {
                break;
            }
            case arrowCords -> {
                break;
            }
            case scoresNum -> {
                break;
            }
            case shotsNum -> {
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + response_type);
        }
    }
    @Override
    public String toString(){
        return "Client{" + "user_name=" + user_name + ", scores=" + scores + ", shots=" + shots + '}';
    }

    public static void main(String[] args){
        /*
        *
        System.out.println("Input user name");
        Scanner sc = new Scanner(System.in);
        String user_name = sc.nextLine();
        Client cl = new Client(user_name);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str_obj = gson.toJson(cl);

        System.out.println(str_obj);

        Client cl1 = gson.fromJson(str_obj, Client.class);
        System.out.println(cl1);

        cl.ConnectClient();
        * */

        MainFrame.StartApp();
    }
}
