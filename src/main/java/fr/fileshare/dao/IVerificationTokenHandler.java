package fr.fileshare.dao;

import fr.fileshare.model.User;

import javax.servlet.http.HttpServletRequest;

public interface IVerificationTokenHandler {
    /**
     * send a verification email to the user so he can validate his email or recover his maile
     *
     * @param user the recipient
     * @param verificationType verification type  VALIDATION_MAIL_TOKEN  or RECOVERY_PWD_TOKEN
     * @return return true if the message is sent, if not false
     */
    boolean sendVerificationMail(User user, int verificationType);

    /**
     * after the click on the validation link this function validate the email
     *
     * @param request the servlet request
     * @return true if the validation process is done, if not false
     */
    boolean validateMail(HttpServletRequest request);

    /**
     * after the click on the recover password link this function give the access to recover password
     *
     * @param token the token
     * @return true if the recover process is done, if not false
     */
    boolean recoverPassword(String token);

}