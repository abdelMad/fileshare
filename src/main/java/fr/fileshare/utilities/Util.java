package fr.fileshare.utilities;

import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.socket.ChatClient;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * global alert status: success,info,warning,danger
     */
    public static final int SUCCESS = 0;
    public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int DANGER = 3;
    /**
     * All application global alerts
     */
    public static ArrayList<GlobalAlert> alerts = new ArrayList<GlobalAlert>();


    /**
     * @param stringToHash String to hash
     * @return return stringToHash hashed to MD5
     */
    public static String hashString(String stringToHash) {
        String generatedString = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(stringToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedString = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedString;
    }

    /**
     * @param toEmail the recipient
     * @param subject mail subject
     * @param body    mail body
     * @return true if the email is sent successfully, if not return false.
     */
    public static boolean sendEmail(String toEmail, String subject, String body) {
        try {
            String smtpHostServer = "localhost";
            Properties props = System.getProperties();

            props.put("mail.smtp.host", smtpHostServer);

            javax.mail.Session session = javax.mail.Session.getInstance(props, null);
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@coursefacile.fr", "NoReply-CourseFacile"));

            msg.setReplyTo(InternetAddress.parse("no_reply@coursefacile.fr", false));

            msg.setSubject(subject, "UTF-8");

            msg.setContent(body, "text/html");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     * @param status  global alert status: UTIL.SUCCESS, UTIL.INFO, UTIL.WARNING, UTIL.DANGER
     * @param message
     */
    public static void addGlobalAlert(int status, String message) {
        if (message.length() == 0) return;
        GlobalAlert alert = new GlobalAlert(status, message);
        alerts.add(alert);
    }

    /**
     * show bootstrap dismissible alert in the top of the page
     *
     * @return html markup String
     */
    public static String showGlobalAlerts() {

        String markup = "";
        for (int i = 0; i < alerts.size(); i++) {
            GlobalAlert alert = alerts.get(i);
            markup += "<div class=\"alert " + alert.getStatus() + " alert-dismissible\" role=\"alert\">\n" +
                    "        <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span\n" +
                    "                aria-hidden=\"true\">&times;</span></button>\n" +
                    "        " + alert.getMessage() + "\n" +
                    "    </div>\n";
            alerts.remove(i);
        }
        return markup;

    }

    /**
     * check whether the email is valid or not
     *
     * @param mail the mail to check
     * @return true or false
     */
    public static boolean isValidEmail(String mail) {
        String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    /**
     * check if the element exist in String enum
     *
     * @param enumeration String enum
     * @param element     key of the element we want to search
     * @return true or false
     */
    public static boolean elementExistInEnum(Enumeration<String> enumeration, String element) {
        while (enumeration.hasMoreElements()) {
            if (enumeration.nextElement().equals(element))
                return true;
        }
        return false;
    }

    /**
     * get property from params.properties file
     *
     * @param paramName parameter name
     * @return the of the given parameter
     */
    public static String getProperty(String paramName) {
        String param = "";
        try {
            Properties prop = new Properties();
            String propFileName = "params.properties";
            InputStream inputStream = Util.class.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            }
            param = prop.getProperty(paramName);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return param;

    }

    /**
     * print the style or js in the specific url
     *
     * @param request servlet request
     * @param url     the specific url regular expression
     * @param fileUrl the css file url
     * @return link tag if url match the requested uri if not an empty string
     */
    public static String includeCssOrJs(HttpServletRequest request, String url, String fileUrl) {
        String currentUrl = request.getAttribute("javax.servlet.forward.request_uri").toString();

        String tag = "";
        String type = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        if (type.equals("css"))
            tag = "<link href= \" " + fileUrl + "\" rel=\"stylesheet\">";
        else if (type.equals("js"))
            tag = "<script type=\"text/javascript\" src=\"" + fileUrl + "\"></script>";

        if (url.equals("*"))
            return tag;
        url.replace("*", "[\\w\\d]*");
        Pattern pattern = Pattern.compile(url, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(currentUrl);
        if (matcher.matches()) {
            return tag;
        }
        return "";
    }

}

/**
 * Bootstrap alert
 */
class GlobalAlert {

    private int status;
    private String message;

    GlobalAlert(int status, String message) {
        this.status = status;
        this.message = message;
    }

    String getStatus() {
        String status = "";
        switch (this.status) {
            case Util.SUCCESS:
                status = "alert-success";
                break;
            case Util.INFO:
                status = "alert-info";
                break;
            case Util.WARNING:
                status = "alert-warning";
                break;
            case Util.DANGER:
                status = "alert-danger";
                break;
        }
        return status;
    }

    public String getMessage() {
        return message;
    }
}