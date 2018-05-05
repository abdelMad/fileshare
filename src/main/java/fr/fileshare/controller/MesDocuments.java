package fr.fileshare.controller;

import fr.fileshare.dao.DocumentHandler;
import fr.fileshare.dao.IDocumentHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MesDocuments extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            List<Document> documents;
            Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);
            IDocumentHandler documentHandler = new DocumentHandler();
            if (request.getRequestURI().equals("/documents-favoris")) {
                documents = documentHandler.getDocumentsFavoris(utilisateur, -1, -1);
                request.setAttribute("title", "Mes Favoris");

            }else {
                documents = documentHandler.getMesDocuments(utilisateur.getId());
                request.setAttribute("title", "Mes documents");
            }

            request.setAttribute("documents", documents);
            this.getServletContext().getRequestDispatcher("/views/mesDocuments.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("destinationUrl", request.getRequestURI());
            response.sendRedirect("/connexion");
        }
    }
}
