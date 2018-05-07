package fr.fileshare.controller;

import fr.fileshare.dao.DocumentHandler;
import fr.fileshare.dao.IDocumentHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Document;
import org.apache.commons.io.FileUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class TelechargerDocument extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(
                request.getHeader("X-Requested-With"));
        if (ajax) {
            if (request.getParameterMap().containsKey("id")) {
                IDocumentHandler documentHandler = new DocumentHandler();
                int doc_id = Integer.parseInt(request.getParameter("id"));
                Document document = documentHandler.get(doc_id);
                File file = new File(this.getServletContext().getRealPath("downloads") + "/" + document.getIntitule() + ".docx");
                file.delete();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean ajax = "XMLHttpRequest".equals(
                request.getHeader("X-Requested-With"));
        if (ajax) {
            if (request.getParameterMap().containsKey("id")) {
                IDocumentHandler documentHandler = new DocumentHandler();
                Document document = null;
                try {
                    int doc_id = Integer.parseInt(request.getParameter("id"));
                    document = documentHandler.get(doc_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                request.setCharacterEncoding("utf8");
                response.setContentType("application/json");
                if (document != null && document.getDernierContenu() != null) {
                    //convert document to word..
                    try {
                        String html = "<html><head><title>Import me</title></head><body>" + document.getDernierContenu() + "</body></html>";
                        System.out.println(document.getDernierContenu());
                        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
                        AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
                        afiPart.setBinaryData(html.getBytes());
                        afiPart.setContentType(new ContentType("text/html"));
                        Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);

// .. the bit in document body
                        CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
                        ac.setId(altChunkRel.getId());
                        wordMLPackage.getMainDocumentPart().addObject(ac);

// .. content type
                        wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");
                        wordMLPackage.save(new java.io.File(this.getServletContext().getRealPath("downloads") + "/" + document.getIntitule() + ".docx"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().println("[\"error\"]");
                        return;
                    }

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put("/downloads/" + document.getIntitule() + ".docx");
                    response.getWriter().println(jsonArray.toString());
                } else
                    response.getWriter().println("[\"error\"]");

            }
        }
    }
}
