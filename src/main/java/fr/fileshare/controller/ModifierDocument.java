package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import fr.fileshare.model.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class ModifierDocument extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getParameterMap().containsKey("doc_id")) {
            if (UtilisateurHandler.isLoggedIn(request)) {
                Map<String, String[]> params = request.getParameterMap();

                System.out.println("Im here");
                IDocumentHandler documentHandler = new DocumentHandler();
                IHistoriqueHandler historiqueHandler = new HistoriqueHandler();
                int id = -1;
                try {
                    id = Integer.parseInt(request.getParameter("doc_id"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Util.addGlobalAlert(Util.DANGER, "Une erreur est survenu!");
                    response.sendRedirect("/");
                }

                Document document = documentHandler.get(id);
                if (document != null && document.getAuteur().getId() == UtilisateurHandler.getLoggedInUser(request).getId()) {

                    if (params.containsKey("intitule") && params.containsKey("status")) {
                        Historique historique = new Historique();

                        String intitule = request.getParameter("intitule");
                        String[] utilisateurs;
                        document.setIntitule(intitule);
                        int status = -1;
                        try {
                            status = Integer.parseInt(request.getParameter("status"));
                            if (status != Document.PARTAGE && status != Document.PRIVE && status != Document.PUBLIC) {
                                Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite");
                                response.sendRedirect("/");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite");
                            response.sendRedirect("/");
                        }
                        if (params.containsKey("description")) {
                            document.setDescription(request.getParameter("description"));
                        }
                        if (params.containsKey("tags")) {
                            document.setTag(request.getParameter("tags"));
                        }
                        if (params.containsKey("contenu")) {
                            historique.setContenu(request.getParameter("contenu"));
                            document.setDernierContenu(request.getParameter("contenu"));
                        }
                        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                        Set<Utilisateur> utilisateurs_autorises = new HashSet<>();
                        if(status != -1) {
                            document.setStatus(status);
                            if (status == Document.PARTAGE && params.containsKey("utilisateurs[]")) {
                                utilisateurs = request.getParameterValues("utilisateurs[]");
                                for (int i = 0; i < utilisateurs.length; i++) {
                                    Utilisateur utilisateur = utilisateurHandler.get(utilisateurs[i]);
                                    if (utilisateur != null) {
                                        System.out.println(utilisateur.getNom());
                                        utilisateurs_autorises.add(utilisateur);
                                    }
                                }
                                document.setUtilisateursAvecDroit(utilisateurs_autorises);
                            }
                        }
                        Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
                        document.setDateDerniereModif(new Date());
                        document.setDernierEditeur(utilisateurCourant);
                        //modification document
                        boolean checkDoc =documentHandler.update(document);

                        historique.setDocument(document);
                        historique.setDateModif(new Date());
                        historique.setEditeur(utilisateurCourant);
                        //insertion historique
                        boolean checkHist =historiqueHandler.add(historique);
                        if (checkHist && checkDoc) {
                            UtilisateurHandler.refresh(request);
                            Util.addGlobalAlert(Util.SUCCESS, "Document modifié avec succès!");
                            response.sendRedirect("/modifier-document?id=" + document.getId());
                        }else{
                            Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite! veuillez réessayer!");
                            response.sendRedirect("/");
                        }

                    }

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }

            } else {
                this.getServletContext().setAttribute("destinationUrl", request.getRequestURI() + "?id=" + request.getParameter("id"));
                response.sendRedirect("/connexion");
            }
        } else {
            response.sendRedirect("/connexion");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameterMap().containsKey("id")) {
            if (UtilisateurHandler.isLoggedIn(request)) {
                IDocumentHandler documentHandler = new DocumentHandler();
                int id = -1;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Util.addGlobalAlert(Util.DANGER, "Une erreur est survenu!");
                    response.sendRedirect("/");
                }

                Document document = documentHandler.get(id);
                if (document != null && document.getAuteur().getId() == UtilisateurHandler.getLoggedInUser(request).getId()) {
                    request.setAttribute("document", document);
                    request.setAttribute("doc_id", id);
                    request.setAttribute("title", "Modifier document");
                    this.getServletContext().getRequestDispatcher("/views/modifierDocument.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }

            } else {
                this.getServletContext().setAttribute("destinationUrl", request.getRequestURI() + "?id=" + request.getParameter("id"));
                response.sendRedirect("/connexion");
            }
        } else {
            response.sendRedirect("/connexion");
        }
    }
}
