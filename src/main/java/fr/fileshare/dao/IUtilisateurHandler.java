package fr.fileshare.dao;

import fr.fileshare.model.Utilisateur;

import javax.servlet.http.HttpServletRequest;

public interface IUtilisateurHandler {
    /**
     * Add a given utilisateur to the database
     *
     * @param utilisateur the utilisateur to add
     * @return 1: if the utilisateur is successfully added, -1: if the email already exists, error 0
     */
    int add(Utilisateur utilisateur);

    /**
     * update a given utilisateur to the database
     *
     * @param utilisateur the utilisateur to update
     * @return true if the utilisateur is successfully updated, if not false
     */
    boolean update(Utilisateur utilisateur);

    /**
     * remove a given utilisateur to the database
     *
     * @param utilisateur the utilisateur to remove
     * @return true if the utilisateur is successfully remove, if not false
     */
    boolean delete(Utilisateur utilisateur);

    /**
     * authenticate a user via request parameters
     *
     * @param request the servlet request
     * @return return true if the user is successfully logged in if not false.
     */
    boolean authenticate(HttpServletRequest request);

    /**
     * register a user vie request parameters
     *
     * @param request the servlet request
     * @return return true if the user is successfully registered if not false.
     */
    boolean register(HttpServletRequest request);

    /**
     * get user by id
     *
     * @param id user id
     * @return if the id match user return the user if not retur null
     */
    Utilisateur get(int id);

    /**
     * get user by mail
     *
     * @param mail user mail
     * @return if the mail match user return the user if not retur null
     */
    Utilisateur get(String mail);

}
