package fr.fileshare.dao;

import fr.fileshare.model.Utilisateur;
import fr.fileshare.model.VerificationToken;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


public class UtilisateurHandler implements IUtilisateurHandler {
    public int add(Utilisateur utilisateur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        int check;
        try {
            session.beginTransaction();
            session.save(utilisateur);
            session.getTransaction().commit();
            check = 1;
        } catch (ConstraintViolationException e) {
            check = -1;
            session.getTransaction().rollback();
            e.printStackTrace();
        } catch (Exception e){
            check = 0;
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return check;

    }

    public boolean update(Utilisateur utilisateur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.update(utilisateur);
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

    public boolean delete(Utilisateur utilisateur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Utilisateur utilisateurToDelete = session.get(utilisateur.getClass(), utilisateur.getId());
            session.delete(utilisateurToDelete);
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

    public boolean authenticate(HttpServletRequest request) {
        boolean check = false;
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey("cnx_email") && params.containsKey("cnx_mdp")) {
            String email = request.getParameter("cnx_email");
            String mdp = request.getParameter("cnx_mdp");
            if (email.length() != 0 && mdp.length() != 0) {
                Session session = SessionFactoryHelper.getSessionFactory().openSession();
                try {
                    session.beginTransaction();
                    Query query = session.createQuery("from Utilisateur where email=:email AND mdp=:mdp");
                    query.setString("email", email);
                    query.setString("mdp", Util.hashString(mdp));
                    Object object = query.uniqueResult();

                    session.getTransaction().commit();

                    if (object != null) {
                        Utilisateur utilisateur = (Utilisateur) object;
                        request.getSession().setAttribute("utilisateur", utilisateur);
                        check = true;
                    } else
                        Util.addGlobalAlert(Util.DANGER, "Email ou mot de passe incorrecte");

                } catch (Exception e) {
                    session.getTransaction().rollback();
                    e.printStackTrace();
                } finally {
                    session.close();
                }
            } else
                Util.addGlobalAlert(Util.WARNING, "Veuillez fournire votre mail et mot de passe");
        } else
            Util.addGlobalAlert(Util.WARNING, "Veuillez fournire votre mail et mot de passe");
        return check;
    }

    public boolean register(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey("nom") && params.containsKey("prenom") && params.containsKey("email") && params.containsKey("mdp") && params.containsKey("confirm_mdp") ) {
            String nom = request.getParameter("nom"),
                    prenom = request.getParameter("prenom"),
                    email = request.getParameter("email"),
                    mdp = request.getParameter("mdp"),
                    confirmMdp = request.getParameter("confirm_mdp"),
                    description = "";
            if(params.containsKey("description"))
                    description = request.getParameter("description");
            if (nom.trim().length() > 0 && prenom.trim().length() > 0 && email.trim().length() > 0 && mdp.trim().length() > 0 && confirmMdp.trim().length() > 0) {
                if (mdp.equals(confirmMdp)) {
                    if(Util.isValidEmail(email)){
                        Utilisateur newUtilisateur = new Utilisateur();
                        newUtilisateur.setEmail(email);
                        newUtilisateur.setNom(nom);
                        newUtilisateur.setPrenom(prenom);
                        newUtilisateur.setEmailChecked(false);
                        newUtilisateur.setRegisterDate(new Date());
                        newUtilisateur.setMdp(Util.hashString(mdp));
                        if (description.trim().length() > 0) newUtilisateur.setDescription(description);
                        int check = add(newUtilisateur);
                        if( check == 1) {
                            request.getSession().setAttribute("utilisateur", newUtilisateur);
                            IVerificationTokenHandler verificationTokenHandler = new VerificationTokenHandler();
                            verificationTokenHandler.sendVerificationMail(newUtilisateur, VerificationToken.VALIDATION_MAIL_TOKEN,true);
                        }else if (check == -1) {
                            Util.addGlobalAlert(Util.DANGER,"L' email que vous venez d'entrer est dèja associé a un compte");
                            return false;
                        }else{
                            Util.addGlobalAlert(Util.DANGER,"Une erreur est survenu! Veuillez resseyer plustard");
                            return false;
                        }
                        return true;
                    }
                    else{
                        Util.addGlobalAlert(Util.DANGER,"Veuillez entrer un email valide");
                    }

                }else
                    Util.addGlobalAlert(Util.DANGER,"Les mots de passe entrée ne sont pas identiques!");
            }else
                Util.addGlobalAlert(Util.DANGER,"Tous les champs avec * sont obligatoires");

        }
        return false;
    }

    public Utilisateur get(int id) {
        Utilisateur utilisateur = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            utilisateur = session.get(Utilisateur.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utilisateur;
    }

    public Utilisateur get(String mail) {
        Utilisateur utilisateur = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Utilisateur  where email='" + mail + "'");
            utilisateur = (Utilisateur) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utilisateur;
    }

    /**
     * @param request The servlet request
     * @return true if the user is logged in, if not false.
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute("utilisateur") != null;
    }

    /**
     * @param request The servlet request
     * @return the logged in user. If no user is logged in return null
     */
    public static Utilisateur getLoggedInUser(HttpServletRequest request) {
        if (isLoggedIn(request))
            return (Utilisateur) request.getSession().getAttribute("utilisateur");
        return null;
    }

    public static void refresh(HttpServletRequest request) {
        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
        request.getSession().setAttribute("utilisateur",utilisateurHandler.get(getLoggedInUser(request).getId()));

    }
}
