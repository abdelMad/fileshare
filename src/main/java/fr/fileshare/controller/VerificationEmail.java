package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.model.VerificationToken;
import fr.fileshare.utilities.Util;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class VerificationEmail extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = this.getServletContext();
        Map<String, String[]> params = request.getParameterMap();
        IVerificationTokenHandler verificationTokenHandler = new VerificationTokenHandler();
        if (UtilisateurHandler.isLoggedIn(request)) {
            Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
            int validationStatus =  verificationTokenHandler.validateMail(request);
            switch (validationStatus){
                case -1:
                    Util.addGlobalAlert(Util.SUCCESS,"Votre email est dèja validé");
                    break;
                case 0:
                    Util.addGlobalAlert(Util.WARNING,"Votre lien de validation est expiré! Un nouveau lien a été envoyer à votre adresse mail");
                    verificationTokenHandler.sendVerificationMail(utilisateurCourant, VerificationToken.VALIDATION_MAIL_TOKEN, false);
                    break;
                case 1:
                    Util.addGlobalAlert(Util.SUCCESS,"Votre email a été activer avec succès");
                    break;
            }
            request.setAttribute("utilisateur", utilisateurCourant);


            response.sendRedirect("/");
        } else {
            Util.addGlobalAlert(Util.WARNING,"Vous devez vous connecter pour pouvoir continuer");
            request.getSession().setAttribute("destinationUrl", request.getRequestURL().append('?').append(request.getQueryString()));
            servletContext.getRequestDispatcher("/connexion").forward(request, response);
        }


    }
}
