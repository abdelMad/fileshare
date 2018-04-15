package fr.fileshare.controller;

import fr.fileshare.dao.SessionFactoryHelper;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Init extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionFactoryHelper.init();
		if(UtilisateurHandler.isLoggedIn(request)) {
			this.getServletContext().getRequestDispatcher("/views/index.jsp").forward(request, response);
		}else
			response.sendRedirect("/connexion");
	}
}
