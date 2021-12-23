/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.helpers;

import java.io.Serializable;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author PC
 */
public class MailTransferHelper implements Serializable {

    public static void sendVerifyCode(String email, String urlConfirm) throws Exception {
        final String username = "nhattan91099@gmail.com";
        final String password = "tandota123";
        Properties prop = System.getProperties();

        prop.put(
                "mail.smtp.auth", "true");
        prop.put(
                "mail.smtp.host", "smtp.gmail.com");
        prop.put(
                "mail.smtp.port", "587");
        prop.put(
                "mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        
        String textBody = urlConfirm;
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Confirm your booking");
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        CommandMap.setDefaultCommandMap(mc);
        message.setContent(textBody, "text/html");
        Transport.send(message);
    }
}
