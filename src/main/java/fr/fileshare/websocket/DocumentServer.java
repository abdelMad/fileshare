package fr.fileshare.websocket;


import fr.fileshare.dao.*;
import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.JsonHelper;
import fr.fileshare.utilities.Util;
import org.json.JSONArray;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/document-modif/{doc}/{idU}")
public class DocumentServer {
    private static final Logger LOGGER =
            Logger.getLogger(ChatServer.class.getName());
    private static HashMap<String, Document> docsSession = new HashMap<>();
    private static HashMap<String, Utilisateur> uSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("doc") final String doc, @PathParam("idU") final String idU) {
        LOGGER.log(Level.INFO, "New connection with client: {0}",
                session.getId());
        LOGGER.log(Level.INFO, doc);
        LOGGER.log(Level.INFO, idU);
        IDocumentHandler documentHandler = new DocumentHandler();

        Document docPOJO = documentHandler.get(Integer.parseInt(doc));
        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
        Utilisateur utilisateurCourant = utilisateurHandler.get(Integer.parseInt(idU));
        uSession.put(session.getId(), utilisateurCourant);
        docsSession.put(session.getId(), docPOJO);
        try {
            JSONArray usersArray = new JSONArray();
            JsonHelper jsonHelper = new JsonHelper();
            System.out.println(session.getId());
            usersArray.put("users");
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen() && docsSession.get(s.getId()) != null && docsSession.get(s.getId()).getId() == Integer.parseInt(doc)) {
                    usersArray.put(jsonHelper.encodeUtilisateur(uSession.get(s.getId())));
                }
            }
            usersArray.put(jsonHelper.encodeUtilisateur(utilisateurCourant));
            System.out.println(usersArray.toString());
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen() && docsSession.get(s.getId()) != null && docsSession.get(s.getId()).getId() == Integer.parseInt(doc)) {
                    s.getBasicRemote().sendText(usersArray.toString());
                }
            }
            session.getBasicRemote().sendText(usersArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JsonHelper jsonHelper = new JsonHelper();
        HashMap<String, String> doc = jsonHelper.decodeDoc(message);
        for (String name : docsSession.keySet()) {

            String key = name.toString();
            String value = docsSession.get(name).toString();
            System.out.println("key " + key + "value " + value);


        }
        for (String name : doc.keySet()) {

            String key = name.toString();
            String value = doc.get(name).toString();
            System.out.println("key " + key + "value " + value);


        }
        String documentText = doc.get("txt");
        String idDocument = doc.get("idDoc");
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen() && docsSession.get(s.getId()) != null && docsSession.get(s.getId()).getId() == Integer.parseInt(idDocument) && s.getId() != session.getId()) {
                    s.getBasicRemote().sendText(jsonHelper.encodeDoc(doc));
                    System.out.println(jsonHelper.encodeDoc(doc));
                }

                IDocumentHandler documentHandler = new DocumentHandler();
                Utilisateur utilisateurCourant = uSession.get(session.getId());


                //Historique
                Document docPOJO = docsSession.get(session.getId());
                docPOJO.setDernierEditeur(utilisateurCourant);
                docPOJO.setDateDerniereModif(new Date());
                docPOJO.setDernierContenu(documentText);
                documentHandler.update(docPOJO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.log(Level.INFO, "Close connection for client: {0}",
                session.getId());
        IHistoriqueHandler historiqueHandler = new HistoriqueHandler();
        IDocumentHandler documentHandler = new DocumentHandler();

        //add historique
        Historique historique = new Historique();
        Document docPOJO = docsSession.get(session.getId());
        String version = Util.generateUniqueToken();
        historique.setVersion(version);
        historique.setContenu(docPOJO.getDernierContenu());
        historique.setEditeur(uSession.get(session.getId()));
        historique.setDateModif(docPOJO.getDateDerniereModif());
        historique.setDocument(docPOJO);
        historiqueHandler.add(historique);
        docsSession.remove(session.getId());

        //set document version

        docPOJO.setVersion(version);
        documentHandler.update(docPOJO);

        //remove session reference
        uSession.remove(session.getId());


    }

    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
}