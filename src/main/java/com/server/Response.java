package com.server;

public class Response {
    public enum respType{isNameUnique, newPlayer, allPlayers, bigTargetCords, smallTargetCords, arrowCords, scoresNum, shotsNum};
    private respType rType;
    private String clientName;
    private Object data;
    public Response(respType r, String _clientName, Object d){
        rType = r;
        clientName = _clientName;
        data = d;
    }
    public respType getRType() { return rType; }
    public Object getData() { return data; };
    public String getClientName() { return clientName; }
    @Override
    public String toString(){
        return "Response{" + "rType=" + rType + ", clientName= " + clientName + ", data=" + data + '}';
    }
}
