package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.Util;
import org.docx4j.wml.U;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Historique extends HttpServlet {
    private int MAX_RESULTS = 30;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
            if (request.getParameterMap().containsKey("idH")) {
                try {
                    Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);
                    int idH = Integer.parseInt(request.getParameter("idH"));
                    IHistoriqueHandler historiqueHandler = new HistoriqueHandler();
                    IDocumentHandler documentHandler = new DocumentHandler();
                    fr.fileshare.model.Historique historique = historiqueHandler.get(idH);
                    request.setCharacterEncoding("utf8");
                    response.setContentType("application/json");
                    Date derniereModif = new Date();
                    // modification document
                    Document doc = historique.getDocument();
                    doc.setDernierContenu(historique.getContenu());
                    doc.setDateDerniereModif(derniereModif);
                    doc.setVersion(historique.getVersion());
                    if (documentHandler.update(doc)) {
                        response.getWriter().println("[\"true\"]");
                    } else
                        response.getWriter().println("[\"false\"]");
                } catch (Exception e) {
                    response.getWriter().println("[\"false\"]");
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
            String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "";
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 1) {
                Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);
                request.setAttribute("title", "Historique");
                IHistoriqueHandler historiqueHandler = new HistoriqueHandler();
                int start = 0;
                int end = MAX_RESULTS;
                List<Document> docsModifies = historiqueHandler.getDocsModifies(utilisateur.getId(), start, end);
                request.setAttribute("documents", docsModifies);
                Util.addGlobalAlert(Util.INFO, "Veuillez selectionner un document pour consulter son historique!");
                this.getServletContext().getRequestDispatcher("/views/historique.jsp").forward(request, response);
            } else if (pathParts.length == 2) {
                String param = pathParts[1];
                int idDoc;
                try {

                    idDoc = Integer.parseInt(param);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("/");
                    return;
                }
                int start = 0;
                int end = MAX_RESULTS;
                Utilisateur utilisateur = UtilisateurHandler.getLoggedInUser(request);

                IHistoriqueHandler historiqueHandler = new HistoriqueHandler();
                List<fr.fileshare.model.Historique> historiques = historiqueHandler.getHistorique(utilisateur.getId(), idDoc, start, end);
                request.setAttribute("historiques", historiques);
                request.setAttribute("title", "Historique du document " + historiques.get(0).getDocument().getIntitule());
                this.getServletContext().getRequestDispatcher("/views/historique.jsp").forward(request, response);
            }
        } else {
            request.getSession().setAttribute("destinationUrl", request.getRequestURI());
            response.sendRedirect("/connexion");
        }
    }
}
