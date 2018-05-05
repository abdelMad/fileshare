package fr.fileshare.websocket;

import fr.fileshare.dao.IMessageHandler;
import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.MessageHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.JsonHelper;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/chat/{userId}")
public class ChatServer {
    private static final Logger LOGGER =
            Logger.getLogger(ChatServer.class.getName());
    private static HashMap<String, String> sessions = new HashMap<String, String>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") final String userId) {
        sessions.put(session.getId(), userId);
        LOGGER.log(Level.INFO, "New connection with client: {0}",
                session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonHelper jsonHelper = new JsonHelper();
        HashMap<String, String> chatMessage = jsonHelper.decodeMessage(message);
        for (String name : sessions.keySet()) {

            String key = name.toString();
            String value = sessions.get(name).toString();
            System.out.println("key " + key + "value " + value);


        }
        for (String name : chatMessage.keySet()) {

            String key = name.toString();
            String value = chatMessage.get(name).toString();
            System.out.println("key " + key + "value " + value);


        }
        IUtilisateurHandler userHandler = new UtilisateurHandler();
        Utilisateur sender = userHandler.get(Integer.parseInt(chatMessage.get("senderId")));

        try {
            if (chatMessage.get("sender").equals("moi")) {
                System.out.println(jsonHelper.encodeMessage(chatMessage));
                IMessageHandler messageHandler = new MessageHandler();
                Message newMessage = new Message();
                newMessage.setText(chatMessage.get("message"));
                Utilisateur receiver = userHandler.get(Integer.parseInt(chatMessage.get("receiver")));
                newMessage.setEmetteur(sender);
                newMessage.setRecepteur(receiver);
                newMessage.setDate(new Date());
                messageHandler.add(newMessage);
            }
            for (Session s : session.getOpenSessions()) {

                if (s.isOpen() && chatMessage.get("receiver").equals(sessions.get(s.getId()))) {
                    LOGGER.log(Level.INFO, "New message from Client [{0}]: {1} to client " + chatMessage.get("receiver"), new Object[]{session.getId(), chatMessage.get("message")});
                    chatMessage.put("sender", sender.getPrenom() + " " + sender.getNom());
                    s.getBasicRemote().sendText(jsonHelper.encodeMessage(chatMessage));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.log(Level.INFO, "Close connection for client: {0}",
                session.getId());
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
}