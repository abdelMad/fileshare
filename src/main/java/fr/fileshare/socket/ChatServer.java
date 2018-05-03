package fr.fileshare.socket;
import java.net.*;
import java.io.*;
import java.util.*;
public class ChatServer {

    public ChatServer (int port) throws IOException {
        ServerSocket server = new ServerSocket (port);
        while (true) {
            Socket client = server.accept ();
            System.out.println ("Accepted from " + client.getInetAddress ());
            ChatHandler chatHandler = new ChatHandler (client);
            chatHandler.start ();
        }
    }
    public static void main (String args[]) throws IOException {

        new ChatServer (4444);
    }
}
