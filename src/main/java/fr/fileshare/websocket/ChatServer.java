package fr.fileshare.websocket;

import fr.fileshare.dao.*;
import fr.fileshare.dao.MessageHandler;
import fr.fileshare.model.Document;
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

@ServerEndpoint(value = "/chat/{userId}/{doc}")
public class ChatServer {
    private static final Logger LOGGER =
            Logger.getLogger(ChatServer.class.getName());
    private static HashMap<String, String> sessions = new HashMap<>();
    private static HashMap<String, String> docSessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") final String userId, @PathParam("doc") final String doc) {
        sessions.put(session.getId(), userId);
        if (!doc.equals("-1")) {
            docSessions.put(session.getId(), doc);
        }
        LOGGER.log(Level.INFO, "New connection with client: {0}",
                session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonHelper jsonHelper = new JsonHelper();
        HashMap<String, String> chatMessage = jsonHelper.decodeMessage(message);
//        for (String name : sessions.keySet()) {
//
//            String key = name.toString();
//            String value = sessions.get(name).toString();
//            System.out.println("key " + key + "value " + value);
//
//
//        }
        for (String name : chatMessage.keySet()) {

            String key = name.toString();
            String value = chatMessage.get(name).toString();
            System.out.println("key " + key + "value " + value);


        }
        IUtilisateurHandler userHandler = new UtilisateurHandler();
        Utilisateur sender = userHandler.get(Integer.parseInt(chatMessage.get("senderId")));
        //destination groupe ou utilisateur
        String msgType = chatMessage.get("type");
        LOGGER.log(Level.INFO, "MSG type: {0}",
                msgType);
        try {

            if (chatMessage.get("sender").equals("moi")) {
                System.out.println(jsonHelper.encodeMessage(chatMessage));
                IMessageHandler messageHandler = new MessageHandler();
                Message newMessage = new Message();
                newMessage.setText(chatMessage.get("message"));
                if (msgType.equals("solo")) {
                    Utilisateur receiver = userHandler.get(Integer.parseInt(chatMessage.get("receiver")));
                    newMessage.setRecepteur(receiver);
                } else if (msgType.equals("grp")) {
                    Document document = new DocumentHandler().get(Integer.parseInt(chatMessage.get("receiver")));
                    newMessage.setGroupe(document);
                }

                newMessage.setEmetteur(sender);
                newMessage.setDate(new Date());
                messageHandler.add(newMessage);
            }
            for (Session s : session.getOpenSessions()) {

                if (s.isOpen()) {
                    if (msgType.equals("solo")) {

                        if (chatMessage.get("receiver").equals(sessions.get(s.getId()))) {
                            LOGGER.log(Level.INFO, "New message from Client [{0}]: {1} to client " + chatMessage.get("receiver"), new Object[]{session.getId(), chatMessage.get("message")});
                            chatMessage.put("sender", sender.getNom());
                            s.getBasicRemote().sendText(jsonHelper.encodeMessage(chatMessage));
                        }
                    } else if (msgType.equals("grp")) {
                        if (chatMessage.get("receiver").equals(docSessions.get(s.getId())) && !chatMessage.get("senderId").equals(sessions.get(s.getId()))) {
                            chatMessage.put("sender", sender.getNom());
                            s.getBasicRemote().sendText(jsonHelper.encodeMessage(chatMessage));
                        }
                    }
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