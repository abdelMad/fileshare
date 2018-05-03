package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class ModifierDocument extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().equals("/ajouter-au-favoris")) {
            if (request.getParameterMap().containsKey("docId")) {
                if (UtilisateurHandler.isLoggedIn(request)) {
                    Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);
                    IDocumentHandler documentHandler = new DocumentHandler();
                    Document doc = documentHandler.get(Integer.parseInt(request.getParameter("docId")));
                    Set<Document> favs = utilisateur.getFavoris();
                    favs.add(doc);
                    utilisateur.setFavoris(favs);
                    new UtilisateurHandler().update(utilisateur);
                }
            }
        } else {
            if (request.getParameterMap().containsKey("doc_id")) {
                if (UtilisateurHandler.isLoggedIn(request)) {
                    Map<String, String[]> params = request.getParameterMap();

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
                    Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
                    Document document = documentHandler.get(id);
                    if (document != null && (
                            document.getAuteur().getId() == utilisateurCourant.getId() ||
                                    document.getStatus() == Document.PUBLIC ||
                                    document.getUtilisateursAvecDroit().contains(utilisateurCourant)
                    )) {
                        if (params.containsKey("contenu")) {
                            Historique historique = new Historique();
                            String[] utilisateurs;
                            if (params.containsKey("intitule")) {

                                String intitule = request.getParameter("intitule");
                                if (intitule.length() == 0) {
                                    Util.addGlobalAlert(Util.WARNING, "Le champs intitulé est obligatoir");
                                    this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
                                    return;
                                }
                                document.setIntitule(intitule);
                            }
                            int status = -1;
                            if (params.containsKey("status")) {

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
                            }
                            if (params.containsKey("description") && document.getAuteur().getId() == utilisateurCourant.getId()) {
                                document.setDescription(request.getParameter("description"));
                            }
                            if (params.containsKey("tags") && document.getAuteur().getId() == utilisateurCourant.getId()) {
                                String tags = request.getParameter("tags");
                                Pattern pattern = Pattern.compile("^#[\\w\\d]+$|^#[\\w\\d]+( #[\\w\\d]+)*$", Pattern.CASE_INSENSITIVE);

                                if (tags.trim().length() != 0 && pattern.matcher(tags).matches()) {
                                    document.setTag(tags);
                                } else {
                                    Util.addGlobalAlert(Util.WARNING, "Veuillez entrer un tag valide (#example1 #exampl2 ...)");
                                    this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
                                }
                            }

                            historique.setContenu(request.getParameter("contenu"));
                            document.setDernierContenu(request.getParameter("contenu"));
                            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                            Set<Utilisateur> utilisateurs_autorises = new HashSet<>();
                            if (status != -1) {
                                document.setStatus(status);
                                if (status == Document.PARTAGE && params.containsKey("utilisateurs[]") && document.getAuteur().getId() == utilisateurCourant.getId()) {
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

                            document.setDateDerniereModif(new Date());
                            document.setDernierEditeur(utilisateurCourant);
                            //modification document
                            boolean checkDoc = documentHandler.update(document);

                            historique.setDocument(document);
                            historique.setDateModif(new Date());
                            historique.setEditeur(utilisateurCourant);
                            //insertion historique
                            boolean checkHist = historiqueHandler.add(historique);
                            if (checkHist && checkDoc) {
                                UtilisateurHandler.refresh(request);
                                Util.addGlobalAlert(Util.SUCCESS, "Document modifié avec succès!");
                                response.sendRedirect("/modifier-document?id=" + document.getId());
                            } else {
                                Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite! veuillez réessayer!");
                                response.sendRedirect("/");
                            }
                        } else {
                            Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite! veuillez réessayer!");
                            response.sendRedirect("/");
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
                Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);

                if ((document != null && (
                        document.getAuteur().getId() == utilisateurCourant.getId() ||
                                document.getStatus() == Document.PUBLIC ||
                                document.getUtilisateursAvecDroit().contains(utilisateurCourant)
                ))) {
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
