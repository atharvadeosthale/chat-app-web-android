package com.example.atharvachat;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketAdapter {
    Socket mSocket;
    {
        try
        {
            mSocket = IO.socket("http://3.6.233.23:8237");
        } catch (URISyntaxException e) {
            // exception
        }
    }
    public Socket getSocket() {
        return mSocket;
    }
}
