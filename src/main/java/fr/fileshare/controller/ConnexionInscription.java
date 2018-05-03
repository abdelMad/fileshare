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
        // type = login or register
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey("type")) {
            String type = request.getParameter("type");
            if (type.length() != 0) {
                IUtilisateurHandler userHandler = new UtilisateurHandler();
                if (type.equals("connexion")) {
                    boolean checkLogin = userHandler.authenticate(request);
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
                    if (userHandler.register(request))
                        response.sendRedirect("/");
                    else
                        response.sendRedirect("/connexion");
                }
            }
        } else
            response.sendRedirect("/connexion");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (("/deconnexion").equals(request.getRequestURI())) {
            request.getSession().removeAttribute("utilisateur");
            response.sendRedirect("/");
        } else {
            request.setAttribute("title","Connexion | Inscription | mot de passe oublié");
            this.getServletContext().getRequestDispatcher("/views/connexionInscription.jsp").forward(request, response);
        }

    }
}
