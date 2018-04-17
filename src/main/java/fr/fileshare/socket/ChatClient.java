package fr.fileshare.socket;

import fr.fileshare.model.Message;

import java.net.*;
import java.io.*;

public class ChatClient implements Runnable{
    protected DataInputStream i;
    protected DataOutputStream o;
    protected int  utilisateurId;
    protected String message;
    protected Thread listener;
    private static ChatClient chatClient;
    public ChatClient (int utilisateurId,Socket s) throws IOException{
        this.i = new DataInputStream (new BufferedInputStream (s.getInputStream()));
        this.o = new DataOutputStream (new BufferedOutputStream (s.getOutputStream()));
        this.utilisateurId = utilisateurId;
        listener = new Thread (this);
        listener.start ();
    }
    public static ChatClient getInstance(int utilisateurId, Socket s) throws IOException{
        if(chatClient == null)
            chatClient = new ChatClient(utilisateurId,s);

        return chatClient;

    }

    public void run() {
        try {
            while (true) {

                     String[] line = i.readUTF().split(";");
                     int id = Integer.parseInt(line[0]);
                     if(id == utilisateurId) {
                         message = line[1];
                         System.out.println(line[1]);
                     }else
                        System.out.println("not concerned mtf");
            }
        } catch (IOException ex) {
            ex.printStackTrace ();
        } finally {
            listener = null;
            try  {
                o.close ();
            } catch (IOException ex) {
                ex.printStackTrace ();
            }
        }
    }
    public boolean sendMessage (String msg) {
            try {
                o.writeUTF (msg);
                o.flush ();
                return true;
            } catch (IOException ex) {
                ex.printStackTrace();
                listener.stop ();
            }

        return false;
    }
    public String getMessage(){
        String msg = message;
        message = null;
        return msg;
    }
    public void close(){
        listener = null;
        try  {
            o.close ();
        } catch (IOException ex) {
            ex.printStackTrace ();
        }

    }
}
