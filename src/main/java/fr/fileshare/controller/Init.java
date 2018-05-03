package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.utilities.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Init extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionFactoryHelper.init();
        IDocumentHandler documentHandler = new DocumentHandler();
        request.setAttribute("title", "Accueil");
        List docs = documentHandler.getDocumentsAVoir(UtilisateurHandler.getLoggedInUser(request), 0, 10);
        request.setAttribute("docs", docs);
        this.getServletContext().getRequestDispatcher("/views/index.jsp").forward(request, response);

    }
}
