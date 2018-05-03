package fr.fileshare.websocket;


import fr.fileshare.dao.*;
import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.JsonHelper;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/document-modif/{doc}")
public class DocumentServer {
    private static final Logger LOGGER =
            Logger.getLogger(ChatServer.class.getName());
    private static HashMap<String, Document> docsSession = new HashMap<>();
    private static HashMap<String, Utilisateur> uSession = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("doc") final String doc) {
        LOGGER.log(Level.INFO, "New connection with client: {0}",
                session.getId());
        IDocumentHandler documentHandler = new DocumentHandler();

        Document docPOJO = documentHandler.get(Integer.parseInt(doc));
        docsSession.put(session.getId(), docPOJO);
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
                if (s.isOpen() && docsSession.get(s.getId()).getId() == Integer.parseInt(idDocument) && s.getId() != session.getId()) {
                    s.getBasicRemote().sendText(jsonHelper.encodeDoc(doc));
                    System.out.println(jsonHelper.encodeDoc(doc));
                }
                IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                IDocumentHandler documentHandler = new DocumentHandler();
                Utilisateur utilisateurCourant;
                if (docsSession.containsKey(session.getId())) {
                    utilisateurCourant = uSession.get(session.getId());
                } else {
                    utilisateurCourant = utilisateurHandler.get(Integer.parseInt(doc.get("idU")));
                    uSession.put(session.getId(), utilisateurCourant);

                }
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
        Historique historique = new Historique();
        historique.setDateModif(new Date());
        Document docPOJO = docsSession.get(session.getId());
        historique.setContenu(docPOJO.getDernierContenu());
        historique.setEditeur(uSession.get(session.getId()));
        historique.setDocument(docPOJO);
        historiqueHandler.add(historique);
        docsSession.remove(session.getId());
        uSession.remove(session.getId());
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
}