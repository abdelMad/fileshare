package fr.fileshare.controller;

import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Profil extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "";
        System.out.println(pathInfo);
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            String param = pathParts[1];
            int id = -1;
            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
            System.out.println(param);
            try {
                id = Integer.parseInt(param);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("/");
                return;
            }
            Utilisateur profilUtilisateur = utilisateurHandler.get(id);
            request.setAttribute("title", "Profil");
            if (profilUtilisateur.equals(UtilisateurHandler.getLoggedInUser(request)))
                this.getServletContext().getRequestDispatcher("/views/profil.jsp").forward(request, response);
            else {
                request.setAttribute("profilUtilisateur", profilUtilisateur);
                this.getServletContext().getRequestDispatcher("/views/profilPublic.jsp").forward(request, response);
            }
        } else if (pathParts.length < 2) {
            if (UtilisateurHandler.isLoggedIn(request)) {
                request.setAttribute("title", "Profil");
                this.getServletContext().getRequestDispatcher("/views/profil.jsp").forward(request, response);
            } else {
                this.getServletContext().setAttribute("destinationUrl", request.getRequestURI());
                response.sendRedirect("/connexion");
            }
        }
    }
}
