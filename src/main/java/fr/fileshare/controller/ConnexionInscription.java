package fr.fileshare.controller;

import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.utilities.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ConnexionInscription extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!UtilisateurHandler.isLoggedIn(request)) {
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
            Map<String, String[]> params = request.getParameterMap();
            // type = login or register
            if (params.containsKey("type")) {
                String type = request.getParameter("type");
                if (type.length() != 0) {
                    IUtilisateurHandler userHandler = new UtilisateurHandler();
                    if (type.equals("connexion")) {
                        boolean checkLogin = userHandler.authenticate(request, response);
                        if (checkLogin) {
                            if (Util.elementExistInEnum(this.getServletContext().getAttributeNames(), "destinationUrl")) {
                                String destinationUrl = this.getServletContext().getAttribute("destinationUrl").toString();
                                this.getServletContext().removeAttribute("destinationUrl");
                                if (destinationUrl.length() != 0) {
                                    response.sendRedirect(destinationUrl);
                                } else
                                    response.sendRedirect("/");
                            } else {
                                response.sendRedirect("/");
                            }
                        } else
                            doGet(request, response);
                    } else if (type.equals("inscription")) {
                        if (userHandler.register(request, response))
                            response.sendRedirect("/");
                        else
                            response.sendRedirect("/connexion");
                    }
                }
            } else
                response.sendRedirect("/connexion");
        } else
            response.sendRedirect("/");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (("/deconnexion").equals(request.getRequestURI())) {
            UtilisateurHandler utilisateurHandler = new UtilisateurHandler();
            utilisateurHandler.deconnexion(request, response);
        } else {
            if (!UtilisateurHandler.isLoggedIn(request)) {
                request.setAttribute("title", "Connexion | Inscription | mot de passe oublié");
                this.getServletContext().getRequestDispatcher("/views/connexionInscription.jsp").forward(request, response);
            } else {
                response.sendRedirect("/");
            }
        }

    }
}
