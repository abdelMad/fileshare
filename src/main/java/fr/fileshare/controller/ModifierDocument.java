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
        if (request.getRequestURI().equals("/supprimer-favoris")) {
            if (request.getParameterMap().containsKey("idDoc")) {
                if (UtilisateurHandler.isLoggedIn(request)) {
                    request.setCharacterEncoding("utf8");
                    response.setContentType("application/json");
                    try {
                        int idDoc = Integer.parseInt(request.getParameter("idDoc"));
                        IDocumentHandler documentHandler = new DocumentHandler();
                        if (documentHandler.supprimerFavoris(UtilisateurHandler.getLoggedInUser(request).getId(), idDoc)) {
                            response.getWriter().println("[\"true\"]");
                            Util.addGlobalAlert(Util.SUCCESS, "Le Document est retiré des favoris avec succès!");
                        } else
                            response.getWriter().println("[\"false\"]");

                    } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().println("[\"false\"]");

                    }
                }
            }
        } else if (request.getRequestURI().equals("/supprimer-document")) {
            if (request.getParameterMap().containsKey("idDoc")) {
                if (UtilisateurHandler.isLoggedIn(request)) {
                    request.setCharacterEncoding("utf8");
                    response.setContentType("application/json");
                    try {
                        int idDoc = Integer.parseInt(request.getParameter("idDoc"));
                        IDocumentHandler documentHandler = new DocumentHandler();
                        Document document = documentHandler.get(idDoc);
                        if (document.getAuteur().getId() == UtilisateurHandler.getLoggedInUser(request).getId()) {
                            if (documentHandler.delete(idDoc)) {
                                response.getWriter().println("[\"true\"]");
                                Util.addGlobalAlert(Util.SUCCESS, "Le Document est supprimé avec succès!");
                            } else
                                response.getWriter().println("[\"false\"]");
                        } else
                            response.getWriter().println("[\"false\"]");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().println("[\"false\"]");

                    }
                }
            }
        } else if (request.getRequestURI().equals("/modifier-favoris")) {
            if (request.getParameterMap().containsKey("idDoc")) {
                if (UtilisateurHandler.isLoggedIn(request)) {
                    try {
                        int idDoc = Integer.parseInt(request.getParameter("idDoc"));
                        Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);
                        IDocumentHandler documentHandler = new DocumentHandler();
                        Document doc = documentHandler.get(idDoc);
                        Set<Document> favs = utilisateur.getFavoris();
                        request.setCharacterEncoding("utf8");
                        response.setContentType("application/json");
                        String status;
                        if (documentHandler.estFavoris(idDoc, utilisateur.getId())) {
                            documentHandler.supprimerFavoris(utilisateur.getId(), idDoc);
                            status = "off";
                        } else {
                            documentHandler.ajouterFavoris(utilisateur.getId(), idDoc);
                            status = "on";
                        }
                        utilisateur.setFavoris(favs);
                        if (new UtilisateurHandler().update(utilisateur)) {
                            response.getWriter().println("[\"" + status + "\"]");
                        } else
                            response.getWriter().println("[\"false\"]");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().println("[\"false\"]");

                    }
                }
            }
        } else if (request.getRequestURI().equals("/modifier-document")) {
            if (request.getParameterMap().containsKey("doc_id")) {
                if (UtilisateurHandler.isLoggedIn(request)) {
                    Map<String, String[]> params = request.getParameterMap();

                    IDocumentHandler documentHandler = new DocumentHandler();
                    Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
                    int id = -1;
                    try {
                        id = Integer.parseInt(request.getParameter("doc_id"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Util.addGlobalAlert(Util.DANGER, "Une erreur est survenu!");
                        response.sendRedirect("/");
                        return;
                    }
                    Document document = documentHandler.get(id);

                    request.setAttribute("estFavoris", documentHandler.estFavoris(id, utilisateurCourant.getId()));
                    request.setAttribute("doc_id", id);
                    request.setAttribute("document", document);
                    request.setAttribute("doc_id", id);
                    request.setAttribute("title", "Modifier document");

                    if (document != null && document.getAuteur().getId() == utilisateurCourant.getId()) {
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
                                return;
                            }
                        }
                        if (params.containsKey("description") && document.getAuteur().getId() == utilisateurCourant.getId()) {
                            document.setDescription(request.getParameter("description"));
                        }
                        if (params.containsKey("tags") && document.getAuteur().getId() == utilisateurCourant.getId()) {
                            String tags = request.getParameter("tags");
                            Pattern pattern = Pattern.compile("^#[\\w\\d]+$|^#[\\w\\d]+( #[\\w\\d]+)*$", Pattern.CASE_INSENSITIVE);
                            System.out.println(tags.trim().length());
                            if (pattern.matcher(tags).matches()) {
                                document.setTag(tags);
                            } else if (tags.trim().length() != 0) {
                                Util.addGlobalAlert(Util.WARNING, "Veuillez entrer un tag valide (#example1 #exampl2 ...)");
                                this.getServletContext().getRequestDispatcher("/views/modifierDocument.jsp").forward(request, response);
                                return;
                            }
                        }

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
                        String lectureS = "off";
                        if (params.containsKey("lectureS")) {
                            lectureS = request.getParameter("lectureS");
                        }
                        System.out.println(request.getParameter("lectureS"));
                        System.out.println(lectureS);
                        System.out.println(lectureS.equals("on"));
                        document.setReadOnly(lectureS.equals("on"));
                        //modification document
                        boolean checkDoc = documentHandler.update(document);

                        if (checkDoc) {
                            UtilisateurHandler.refresh(request);
                            Util.addGlobalAlert(Util.SUCCESS, "Document modifié avec succès!");
                            response.sendRedirect("/modifier-document?id=" + document.getId());
                        } else {
                            Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite! veuillez réessayer!");
                            response.sendRedirect("/");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }

                } else {
                    request.getSession().setAttribute("destinationUrl", request.getRequestURI() + "?id=" + request.getParameter("id"));
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
                    request.setAttribute("estFavoris", documentHandler.estFavoris(id, utilisateurCourant.getId()));
                    request.setAttribute("title", "Modifier document");
                    this.getServletContext().getRequestDispatcher("/views/modifierDocument.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }

            } else {
                request.getSession().setAttribute("destinationUrl", request.getRequestURI() + "?id=" + request.getParameter("id"));
                response.sendRedirect("/connexion");
            }
        } else {
            response.sendRedirect("/connexion");
        }
    }
}
