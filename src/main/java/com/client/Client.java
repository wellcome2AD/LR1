package com.client;

import com.app.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.server.Response;
import com.server.ServerFrameController;
import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import static com.client.Request.message.isNameUnique;

public class Client {
    private String user_name;
    private FrameController fc;
    private int scores, shots;
    private Socket socketAtClient;
    private int port = 3124;
    private InetAddress ip;
    private DataInputStream dis;
    private DataOutputStream dos;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Client(String _user_name, FrameController _fc) {
        user_name = _user_name;
        fc = _fc;
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
            System.out.println("Response: " + r);
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
                    MyDialog dialog = new MyDialog("Такое имя уже есть, введите другое");
                    dialog.getResult().ifPresent(name -> user_name = name);
                    SendToServer(new Request(isNameUnique, null, user_name));
                    HandleResponse(ReceiveFromServer());
                }
                else{
                    user_name = (String) r.getClientName();
                    fc.AddPlayer(user_name); // добавляем себя самого первого в список всех игроков
                }
            }
            case newPlayer -> {
                fc.AddPlayer(r.getClientName());
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
                fc.IncreaseScores((Integer) r.getData());
            }
            case shotsNum -> {
                fc.IncreaseShots();
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

        ClientFrame.StartApp();
    }
}
