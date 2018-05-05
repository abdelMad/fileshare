package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Init extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String,String[]> params = request.getParameterMap();
        String intitule = "",tags = "";
        SessionFactoryHelper.init();
        IDocumentHandler documentHandler = new DocumentHandler();
        request.setAttribute("title", "Accueil");
        if(params.containsKey("intitule"))
            intitule = request.getParameter("intitule");
        if(params.containsKey("tags"))
            tags = request.getParameter("tags");

        List<Document> docs = documentHandler.getDocumentsAVoir(UtilisateurHandler.getLoggedInUser(request), 0, 10, intitule, tags);
        request.setAttribute("docs", docs);
        if (UtilisateurHandler.isLoggedIn(request))
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
        this.getServletContext().getRequestDispatcher("/views/index.jsp").forward(request, response);

    }
}
