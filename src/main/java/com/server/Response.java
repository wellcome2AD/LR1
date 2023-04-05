package com.server;

public class Response {
    public enum respType{isNameUnique, bigTargetCords, smallTargetCords, arrowCords, scoresNum, shotsNum};
    private respType rType;
    private Object data;
    public Response(respType r, Object d){
        rType = r;
        data = d;
    }
    public respType getRType() { return rType; }
    public Object getData() { return data; };
}
