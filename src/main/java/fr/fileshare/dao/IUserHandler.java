package fr.fileshare.dao;

import fr.fileshare.model.User;

import javax.servlet.http.HttpServletRequest;

public interface IUserHandler {
    /**
     * Add a given user to the database
     *
     * @param user the user to add
     * @return true if the user is successfully added, if not false
     */
    boolean add(User user);

    /**
     * update a given user to the database
     *
     * @param user the user to update
     * @return true if the user is successfully updated, if not false
     */
    boolean update(User user);

    /**
     * remove a given user to the database
     *
     * @param user the user to remove
     * @return true if the user is successfully remove, if not false
     */
    boolean delete(User user);

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
    User get(int id);

    /**
     * get user by mail
     *
     * @param mail user mail
     * @return if the mail match user return the user if not retur null
     */
    User get(String mail);


}
