package fr.fileshare.controller;

import fr.fileshare.dao.*;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.model.VerificationToken;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@MultipartConfig
public class Profil extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (UtilisateurHandler.isLoggedIn(request)) {
            Utilisateur utilisateurCourant = UtilisateurHandler.getLoggedInUser(request);
            if (request.getRequestURI().equals("/modifier-image-profil")) {
                Part imageProfile = request.getPart("imageProfile");

                if (imageProfile != null) {

                    try {
                        boolean check = true;
                        if (!utilisateurCourant.getImage().isEmpty()) {
                            System.out.println(getServletConfig().getServletContext().getRealPath("") + utilisateurCourant.getImage());
                            File file = new File(getServletConfig().getServletContext().getRealPath("") + utilisateurCourant.getImage());
                            System.out.println("fileExists:=" + file.exists());
                            check = file.delete();
                            System.out.println("check:=" + check);
                        }
                        if (check) {
                            UUID uuid = UUID.randomUUID();
                            String fileName = utilisateurCourant.getNom() + uuid.toString() + ".png";
                            InputStream imageProfileInputStream = imageProfile.getInputStream();
                            byte[] buffer = new byte[imageProfileInputStream.available()];
                            imageProfileInputStream.read(buffer);
                            File targetFile = new File(getServletConfig().getServletContext().getRealPath("uploads") + "/" + fileName);
                            OutputStream outStream = new FileOutputStream(targetFile);
                            outStream.write(buffer);
                            UtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                            utilisateurCourant.setImage("/uploads/" + fileName);
                            if (utilisateurHandler.update(utilisateurCourant))
                                response.getWriter().println("/uploads/" + fileName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().println("false");
                    }
                } else {
                    response.getWriter().println(request.getParameterNames().hasMoreElements());
                    response.getWriter().println("Im submitted dunno what to do :(");
                }
            } else {
                Map<String, String[]> params = request.getParameterMap();

                if (params.containsKey("key") && params.containsKey("value")) {
                    String key = request.getParameter("key");
                    String value = request.getParameter("value");
                    UtilisateurHandler utilisateurHandler = new UtilisateurHandler();

                    switch (key) {
                        case "nom":
                            utilisateurCourant.setNom(value);
                            utilisateurHandler.update(utilisateurCourant);
                            request.getSession().setAttribute("utilisateur", utilisateurCourant);
                            response.getWriter().println("true");
                            break;
                        case "prenom":
                            utilisateurCourant.setPrenom(value);
                            utilisateurHandler.update(utilisateurCourant);
                            request.getSession().setAttribute("utilisateur", utilisateurCourant);
                            response.getWriter().println("true");
                            break;
                        case "email":
                            utilisateurCourant.setEmail(value);
                            utilisateurCourant.setEmailChecked(false);
                            if (utilisateurHandler.update(utilisateurCourant)) {
                                request.getSession().setAttribute("utilisateur", utilisateurCourant);
                                IVerificationTokenHandler verificationTokenHandler = new VerificationTokenHandler();
                                verificationTokenHandler.sendVerificationMail(utilisateurCourant, VerificationToken.VALIDATION_MAIL_TOKEN, false);
                                response.getWriter().println("true");
                            }
                            break;
                        case "description":
                            utilisateurCourant.setDescription(value);
                            utilisateurHandler.update(utilisateurCourant);
                            request.getSession().setAttribute("utilisateur", utilisateurCourant);
                            response.getWriter().println("true");

                            break;
                        case "verify":
                            System.out.println(utilisateurCourant.getMdp());
                            System.out.println(Util.hashString(value));
                            if (utilisateurCourant.getMdp().equals(Util.hashString(value)))
                                response.getWriter().println("true");
                            else
                                response.getWriter().println(utilisateurCourant.getEmail());
                            break;
                    }
                } else {
                    response.getWriter().println("false");
                }
            }
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "";
        System.out.println(pathInfo);
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 2) {
            String param = pathParts[1];
            int id = -1;
            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
            System.out.println(param);
            try {
                id = Integer.parseInt(param);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("/");
                return;
            }
            Utilisateur profilUtilisateur = utilisateurHandler.get(id);
            request.setAttribute("title", "Profil");
            if (profilUtilisateur.equals(UtilisateurHandler.getLoggedInUser(request)))
                this.getServletContext().getRequestDispatcher("/views/profil.jsp").forward(request, response);
            else {
                request.setAttribute("profilUtilisateur", profilUtilisateur);
                this.getServletContext().getRequestDispatcher("/views/profilPublic.jsp").forward(request, response);
            }
        } else if (pathParts.length < 2) {
            if (UtilisateurHandler.isLoggedIn(request)) {
                request.setAttribute("utilisateur", UtilisateurHandler.getLoggedInUser(request));
                request.setAttribute("title", "Profil");
                this.getServletContext().getRequestDispatcher("/views/profil.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("destinationUrl", request.getRequestURI());
                response.sendRedirect("/connexion");
            }
        }
    }
}
