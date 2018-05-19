package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class NouveauDocument extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
            Map<String, String[]> params = request.getParameterMap();
            if (params.containsKey("intitule") && params.containsKey("status")) {
                String intitule = request.getParameter("intitule");
                if (intitule.length() > 0) {
                    String[] utilisateurs;
                    String tags = "";
                    IDocumentHandler documentHandler = new DocumentHandler();
                    Document doc = new Document();
                    doc.setIntitule(intitule);
                    int status = -1;
                    if (params.containsKey("tags")) {
                        tags = request.getParameter("tags");
                    }
                    Pattern pattern = Pattern.compile("^#[\\w\\d]+$|^#[\\w\\d]+( #[\\w\\d]+)*$", Pattern.CASE_INSENSITIVE);

                    if (tags.length() == 0 || pattern.matcher(tags).matches()) {
                        doc.setTag(tags);

                        try {
                            status = Integer.parseInt(request.getParameter("status"));
                            if (status != Document.PARTAGE && status != Document.PRIVE && status != Document.PUBLIC) {
                                Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite");
                                this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Util.addGlobalAlert(Util.DANGER, "Une erreur s'est produite");
                            this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
                        }
                        if (params.containsKey("description")) {
                            doc.setDescription(request.getParameter("description"));
                        }

                        if (status != -1) {
                            doc.setStatus(status);
                            if (status == Document.PARTAGE && params.containsKey("utilisateurs[]")) {
                                utilisateurs = request.getParameterValues("utilisateurs[]");
                                IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                                Set<Utilisateur> utilisateurs_autorises = new HashSet<>();
                                for (int i = 0; i < utilisateurs.length; i++) {
                                    Utilisateur utilisateur = utilisateurHandler.get(utilisateurs[i]);
                                    if (utilisateur != null) {
                                        utilisateurs_autorises.add(utilisateur);
                                    }
                                }

                                doc.setUtilisateursAvecDroit(utilisateurs_autorises);
                            }
                        }
                        String lectureS = "off";
                        if (params.containsKey("lectureS")) {
                            lectureS = request.getParameter("lectureS");
                        }
                        doc.setReadOnly(lectureS.equals("on"));
                        doc.setDatePublixation(new Date());
                        doc.setDateDerniereModif(new Date());
                        Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
                        doc.setAuteur(utilisateurCourant);
                        doc.setDernierEditeur(utilisateurCourant);
                        if (documentHandler.add(doc)) {
                            UtilisateurHandler.refresh(request);
                            Util.addGlobalAlert(Util.SUCCESS, "Document crée avec succès!");
                            response.sendRedirect("/modifier-document?id=" + doc.getId());
                        }


                    } else {
                        Util.addGlobalAlert(Util.WARNING, "Veuillez entrer un tag valide (#example1 #exampl2 ...)");
                        this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
                    }
                }
            } else {
                Util.addGlobalAlert(Util.WARNING, "Le champs intitulé est obligatoir");
                this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
            }
        } else {
            request.getSession().setAttribute("destinationUrl", request.getRequestURI());
            response.sendRedirect("/connexion");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            request.setAttribute("title", "Nouveau Document");
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
            this.getServletContext().getRequestDispatcher("/views/nouveauDocument.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("destinationUrl", request.getRequestURI());
            response.sendRedirect("/connexion");
        }
    }
}
