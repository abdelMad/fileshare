package fr.fileshare.controller;

import com.google.common.collect.Lists;
import fr.fileshare.dao.*;
import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.socket.ChatClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Messages extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getRequestURI().equals("/envoyer-messages")) {
            if (UtilisateurHandler.isLoggedIn(request)) {
                Map<String, String[]> params = request.getParameterMap();
                if (params.containsKey("contactId") && params.containsKey("messageTxt")) {
                    String idRecepteur = request.getParameter("contactId"),
                            messageTxt = request.getParameter("messageTxt");

                    IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                    Utilisateur recepteur = utilisateurHandler.get(Integer.parseInt(idRecepteur));
                    Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
                    if (recepteur != null) {
                        IMessageHandler messageHandler = new MessageHandler();
                        Message message = new Message();
                        message.setDate(new Date());
                        message.setEmetteur(utilisateurCourant);
                        message.setRecepteur(recepteur);
                        message.setText(messageTxt);
                        boolean check = messageHandler.add(message);
                        request.setCharacterEncoding("utf8");
                        response.setContentType("application/json");
                        ChatClient chatClient = (ChatClient) request.getSession().getAttribute("socket");
                        System.out.println("recepteur " + recepteur.getId());
                        chatClient.sendMessage(recepteur.getId() + ";" + utilisateurCourant.getId());
                        if (check) {
                            messageHandler = new MessageHandler();
                            String jsonResponse = messageHandler.getJsonConversation(utilisateurCourant.getId(), Integer.parseInt(idRecepteur));
                            response.getWriter().println("["+jsonResponse+"]");
                        } else {
                            response.getWriter().println("[\"erreur\"]");
                        }
                    }
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Util.initClientSocket(request);
        if (request.getRequestURI().equals("/afficher-messages")) {
            if (request.getParameterMap().containsKey("contactId")) {
                request.setCharacterEncoding("utf8");
                response.setContentType("application/json");
                int idRecepteur = Integer.parseInt(request.getParameter("contactId"));
                IMessageHandler messageHandler = new MessageHandler();
                int idUtilisateurCourant =UtilisateurHandler.getLoggedInUser(request).getId();
                String jsonResponse = messageHandler.getJsonConversation(idUtilisateurCourant, idRecepteur);
                messageHandler.changerStatusMessage(idUtilisateurCourant, idRecepteur);
                response.getWriter().println("[" + jsonResponse + "]");
            }
        } else if (request.getRequestURI().equals("/afficher-messages-socket")) {
            if (UtilisateurHandler.isLoggedIn(request)) {
                ChatClient chatClient = (ChatClient) request.getSession().getAttribute("socket");
                String chatClientMsg = chatClient.getMessage();
                request.setCharacterEncoding("utf8");
                response.setContentType("application/json");
                System.out.println(chatClientMsg);
                if (chatClientMsg != null) {

                    IMessageHandler messageHandler = new MessageHandler();
                    System.out.println(chatClientMsg);
                    int idUtilisateurCourant = UtilisateurHandler.getLoggedInUser(request).getId();
                    String jsonOutput = messageHandler.getJsonUtilisateursContactes(idUtilisateurCourant, Integer.parseInt(chatClientMsg));
                    String jsonResponse = "";
                    if(request.getParameterMap().containsKey("chatActuelle")){
                        String chatActuelle = request.getParameter("chatActuelle");
                        if(chatActuelle.equals(chatClientMsg)){
                             jsonResponse = messageHandler.getJsonConversation(idUtilisateurCourant, Integer.parseInt(chatActuelle));

                        }
                    }
                    response.getWriter().println("["+jsonOutput+",["+jsonResponse+"]]");


                } else
                    response.getWriter().println("[\"vide\"]");
            }
        } else {
            if (UtilisateurHandler.isLoggedIn(request)) {

                request.setAttribute("title", "Messagerie instantanée");
                IMessageHandler messageHandler = new MessageHandler();
                Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
                Utilisateur contact = null;
                List<Utilisateur> contacts = (List<Utilisateur>) messageHandler.getUtilisateursContactes(utilisateurCourant.getId());
                request.setAttribute("utilisateur", utilisateurCourant);
                if (request.getParameterMap().containsKey("utilisateur")) {
                    try {
                        int idEmetteur = Integer.parseInt(request.getParameter("utilisateur"));
                        if (messageHandler.checkNouveauContact(utilisateurCourant.getId(), idEmetteur)) {
                            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                            contact = utilisateurHandler.get(idEmetteur);
                            Collections.reverse(contacts);
                            contacts.add(contact);
                            Collections.reverse(contacts);
                        }else{
                            if (!contacts.isEmpty()) {
                                contact = contacts.get(0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!contacts.isEmpty()) {
                        contact = contacts.get(0);
                    }
                }
                if (contact != null) {
                    List<Message> messages = (List<Message>) messageHandler.getConversation(contact.getId(), utilisateurCourant.getId());
                    request.setAttribute("messages", Lists.reverse(messages));
                    request.setAttribute("recepteur", contact);
                    request.setAttribute("contacts", contacts);
                }
                this.getServletContext().getRequestDispatcher("/views/messages.jsp").forward(request, response);
            } else {
                this.getServletContext().setAttribute("destinationUrl", request.getRequestURI());
                response.sendRedirect("/connexion");
            }
        }
    }
}
