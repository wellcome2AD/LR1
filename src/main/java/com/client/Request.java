package com.client;

public class Request {
    public enum message {isNameUnique, getAllPlayers, playerIsReady, pauseGame, arrowIsShot, getRecords};
    private message m;
    private String clientName;
    private Object data;
    public Request(message _m, String _clientName, Object _data) {
        m = _m;
        clientName = _clientName;
        data = _data;
    }

    public message getMessage() {
        return m;
    }
    public Object getData() {
        return data;
    }
    public String getClientName() { return clientName; }
    @Override
    public String toString(){
        return "Request{" + "m=" + m + ", clientName=" + clientName + ", data=" + data + '}';
    }
}
