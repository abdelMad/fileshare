package fr.fileshare.dao;

import fr.fileshare.model.Document;

import java.util.List;

public interface IDocumentHandler {
    boolean add(Document document);

    boolean update(Document document);

    boolean delete(Document document);

    Document get(int id);

    List getDocumentsAVoir(int id_utilisateur, int maxResultat);

}

