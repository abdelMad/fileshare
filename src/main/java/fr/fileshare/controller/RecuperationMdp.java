package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.model.VerificationToken;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RecuperationMdp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();
        ServletContext servletContext = this.getServletContext();
        IUtilisateurHandler userHandler = new UtilisateurHandler();
        IVerificationTokenHandler verificationTokenHandler = new VerificationTokenHandler();
        if (params.containsKey("email")) {
            String email = request.getParameter("email");
            if (Util.isValidEmail(email)) {


                Utilisateur user = userHandler.get(email);
                if (user != null) {
                    verificationTokenHandler.sendVerificationMail(user, VerificationToken.RECOVERY_PWD_TOKEN, true);
                    response.sendRedirect("/");
                } else {
                    Util.addGlobalAlert(Util.DANGER, "Aucun compte n'est associé a cette adresse email!");
                    request.setAttribute("showRecoverPwdForm", "false");
                    response.sendRedirect("/connexion");
                }

            }
        } else if (params.containsKey("mdp") && params.containsKey("confirm_mdp") && params.containsKey("token")) {
            String mdp = request.getParameter("mdp"),
                    confirm_mdp = request.getParameter("confirm_mdp"),
                    token = request.getParameter("token");
            if (verificationTokenHandler.recoverPassword(token)) {
                if (confirm_mdp.trim().length() >= 8 && mdp.trim().length() >= 8) {
                    Utilisateur user = verificationTokenHandler.getUtilisateur(token);
                    user.setMdp(Util.hashString(mdp));
                    userHandler.update(user);
                    servletContext.removeAttribute("recoverPwdUser");
                    request.getSession().setAttribute("user", user);
                    Util.addGlobalAlert(Util.SUCCESS, "Votre mot de passe est modifié avec succès");
                    response.sendRedirect("/");
                }else {
                    Util.addGlobalAlert(Util.DANGER, "Une erreur est survenu!");
                    servletContext.getRequestDispatcher("/views/connexionInscription.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("showRecoverPwdForm", "true");
                Util.addGlobalAlert(Util.WARNING, "Votre mot de passe doit contenir un minimum de 8 caractères");
                servletContext.getRequestDispatcher("/views/connexionInscription.jsp").forward(request, response);

            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();
        request.setAttribute("showRecoverPwdForm", "false");
        if (params.containsKey("token")) {
            String token = request.getParameter("token");
            IVerificationTokenHandler verificationTokenHandler = new VerificationTokenHandler();
            boolean check = verificationTokenHandler.recoverPassword(token);
            if (check) {
                request.setAttribute("showRecoverPwdForm", "true");
                request.setAttribute("token", token);
            } else {
                Util.addGlobalAlert(Util.WARNING, "Le lien de recuperation de mot de passe est expiré veuillez demander un autre!");
            }
        }
        this.getServletContext().getRequestDispatcher("/views/connexionInscription.jsp").forward(request, response);
    }
}
