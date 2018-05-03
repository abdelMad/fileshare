package fr.fileshare.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler {
    public static void main(String[] args) throws IOException {

        String host = "localhost";
        int port = 4444;
        Socket s = new Socket(host, port);
        new ChatClient(3,s);
    }
}
