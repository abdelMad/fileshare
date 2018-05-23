package fr.fileshare.controller;

import fr.fileshare.dao.IMessageHandler;
import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.MessageHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.JsonHelper;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Chat extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
            Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
            IMessageHandler messageHandler = new MessageHandler();
            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
            switch (request.getRequestURI()) {
                case "/conversation":
                    JsonHelper jsonHelper = new JsonHelper();
                    int idContact = -1;
                    int start = -1;
                    int end = -1;
                    try {
                        idContact = Integer.parseInt(request.getParameter("idContact"));
                        start = Integer.parseInt(request.getParameter("start"));
                        end = Integer.parseInt(request.getParameter("end"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Utilisateur utilisateur = utilisateurHandler.get(idContact);
                    List<Message> messages = messageHandler.getMessages(utilisateurCourant.getId(), idContact, start, end);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(request.getParameter("idContact"));
                    jsonArray.put(utilisateur.getNom());
                    jsonArray.put(Integer.toString(utilisateurCourant.getId()));
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        if (messages.get(i).getEmetteur().getId() == utilisateurCourant.getId())
                            messages.get(i).getEmetteur().setNom("moi");

                        jsonArray.put(jsonHelper.encodeMessage(messages.get(i)));
                    }
                    request.setCharacterEncoding("utf8");
                    response.setContentType("application/json");
                    response.getWriter().println(jsonArray.toString());
                    break;
                case "/contacts":
                    List<Utilisateur> utilisateurs = messageHandler.getContacts(utilisateurCourant.getId());
                    JSONArray uJsonArray = new JSONArray();
                    JsonHelper uJsonHelper = new JsonHelper();

                    for (int i = 0; i < utilisateurs.size(); i++)
                        uJsonArray.put(uJsonHelper.encodeUtilisateur(utilisateurs.get(i)));

                    request.setCharacterEncoding("utf8");
                    response.setContentType("application/json");
                    response.getWriter().println(uJsonArray.toString());

                    break;
                case "/contact":
                    if (request.getParameterMap().containsKey("u")) {
                        int idUser = -1;
                        try {
                            idUser = Integer.parseInt(request.getParameter("u"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Utilisateur u = utilisateurHandler.get(idUser);
                        uJsonHelper = new JsonHelper();
                        request.setCharacterEncoding("utf8");
                        response.setContentType("application/json");
                        response.getWriter().println(uJsonHelper.encodeUtilisateur(u));
                    }

                    break;
            }
        } else {
            response.sendRedirect("/connexion");
        }
    }
}
