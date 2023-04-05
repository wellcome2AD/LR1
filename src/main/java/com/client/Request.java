package com.client;

public class Request {
    public enum message {isNameUnique, arrowIsShot};
    private message m;
    private Object data;
    public Request(message _m, Object _data) {
        m = _m;
        data = _data;
    }

    public message getMessage() {
        return m;
    }
    public Object getData() {
        return data;
    }
}
