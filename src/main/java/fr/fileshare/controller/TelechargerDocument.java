package fr.fileshare.controller;

import fr.fileshare.dao.DocumentHandler;
import fr.fileshare.dao.IDocumentHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Document;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TelechargerDocument extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameterMap().containsKey("id")) {
//            if (UtilisateurHandler.isLoggedIn(request)) {

                IDocumentHandler documentHandler = new DocumentHandler();
                Document document = null;
                try {
                    int doc_id = Integer.parseInt(request.getParameter("id"));
                    document = documentHandler.get(doc_id);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(document != null && document.getDernierContenu().length()>0) {
                    response.setContentType("text/doc");
                    response.setHeader("Content-Disposition",
                            "attachment;filename="+document.getIntitule()+".doc");
                    StringBuffer sb = new StringBuffer(document.getDernierContenu());
                    InputStream in = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
                    ServletOutputStream out = response.getOutputStream();

                    byte[] outputByte = new byte[4096];
                    while (in.read(outputByte, 0, 4096) != -1) {
                        out.write(outputByte, 0, 4096);
                    }
                    in.close();
                    out.flush();
                    out.close();
                }

//            } else {
//                this.getServletContext().setAttribute("destinationUrl", request.getRequestURI()+"?id="+request.getParameter("id"));
//                response.sendRedirect("/connexion");
//            }
        }else{
            response.sendRedirect("/");
        }
    }
}
