package fr.fileshare.dao;

import fr.fileshare.model.Document;

public interface IDocumentHandler {
    boolean add(Document document);

    boolean update(Document document);

    boolean delete(Document document);

    Document get(int id);
}
