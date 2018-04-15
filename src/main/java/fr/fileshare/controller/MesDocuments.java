package fr.fileshare.controller;

import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class MesDocuments extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);
            if("/documents-partages".equals(request.getRequestURI())){
                request.setAttribute("title", "Documents partagés");
                Set<Document> documents = utilisateur.getDocumentsAutorises();
                request.setAttribute("documents", documents);

            }else {
                Set<Document> documents = utilisateur.getDocuments();
                request.setAttribute("title", "Mes documents");
                request.setAttribute("documents", documents);
            }
            this.getServletContext().getRequestDispatcher("/views/mesDocuments.jsp").forward(request, response);

        }else{
            this.getServletContext().setAttribute("destinationUrl", request.getRequestURI());
            response.sendRedirect("/connexion");
        }
    }
}
