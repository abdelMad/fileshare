package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DocumentHandler implements IDocumentHandler {
    public boolean add(Document document) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.save(document);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return check;
    }

    public boolean update(Document document) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.update(document);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    public boolean delete(Document document) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Document documentASupprimer = session.get(document.getClass(), document.getId());
            session.delete(documentASupprimer);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;

    }

    public Document get(int id) {
        Document document = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            document = session.get(Document.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return document;
    }

    public List<Document> getDocumentsAVoir(Utilisateur utilisateurCourant, int debut, int fin) {
        List<Document> documents = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            if (utilisateurCourant != null) {
                int id_utilisateur = utilisateurCourant.getId();
                query = session.createSQLQuery("SELECT * from(SELECT * FROM fileshare.document WHERE status =0 OR auteur = :id_utilisateur\n" +
                        "UNION\n" +
                        "select document.* from fileshare.document join document_utilisateur on document_utilisateur.document_id=document_utilisateur.document_id where  document_utilisateur.utilisateur_id=:id_utilisateur\n" +
                        ") fichiers_autorise\n" +
                        "order by document_id Desc")
                        .addEntity("document", Document.class);
                query.setParameter("id_utilisateur", id_utilisateur);
            }else{
                 query = session.createQuery(" FROM Document  WHERE status = 0");
            }

            query.setFirstResult(debut);
            query.setMaxResults(fin);
            documents = (List<Document>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return documents;
    }

    public List<Document> getDocumentsFavoris(Utilisateur utilisateurCourant, int debut, int fin) {
        List<Document> documents = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            int id_utilisateur = utilisateurCourant.getId();
            query = session.createSQLQuery("SELECT distinct document.* FROM fileshare.document JOIN fileshare.favoris ON favoris.document_id = document.document_id WHERE\n" +
                    "favoris.utilisateur_id = :id_utilisateur ORDER BY document.document_id")
                    .addEntity("document", Document.class);
            query.setParameter("id_utilisateur", id_utilisateur);

            query.setFirstResult(debut);
            query.setMaxResults(fin);
            documents = (List<Document>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return documents;
    }

}
