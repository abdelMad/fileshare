package fr.fileshare.controller;

import fr.fileshare.dao.DocumentHandler;
import fr.fileshare.dao.IDocumentHandler;
import fr.fileshare.dao.SessionFactoryHelper;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Init extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionFactoryHelper.init();
		if(UtilisateurHandler.isLoggedIn(request)) {
			IDocumentHandler documentHandler = new DocumentHandler();
			request.setAttribute("title","Accueil");
			List docs = documentHandler.getDocumentsAVoir(UtilisateurHandler.getLoggedInUser(request).getId(),10);
//			if(docs != null) {
//				for (int i = 0; i < docs.size(); i++) {
//					response.getWriter().println(docs.get(i).toString());
//				}
//			}else
//				response.getWriter().println("docs est null :/");
            request.setAttribute("docs",docs);
			this.getServletContext().getRequestDispatcher("/views/index.jsp").forward(request, response);
		}else
			response.sendRedirect("/connexion");
	}
}
