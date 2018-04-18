package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;

import java.util.List;

public interface IDocumentHandler {
    boolean add(Document document);

    boolean update(Document document);

    boolean delete(Document document);

    Document get(int id);

    List getDocumentsAVoir(Utilisateur utilisateur,String intitule,String tags, int maxResultat);

}

