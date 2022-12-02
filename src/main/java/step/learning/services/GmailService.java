package step.learning.services;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GmailService implements EmailService {
    @Override
    public boolean send(String to, String subject, String text) {
        Properties gmailProperties = new Properties();
        gmailProperties.put("mail.smtp.auth", "true");
        gmailProperties.put("mail.smtp.starttls.enable", "true");
        gmailProperties.put("mail.smtp.port", "587");
        gmailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        gmailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session mailSession = Session.getInstance(gmailProperties);
        //mailSession.setDebug(true);

        try {
            Transport mailTransport = mailSession.getTransport("smtp");
            mailTransport.connect(                       // Подключения у почтовому сервису
                    "smtp.gmail.com",                    // адрес(хост) сервера
                    "javaseniorweb@gmail.com",           // ящик
                    "cuofqfghrtwadidp");                // пароль приложения

            // Создание сообщения
            MimeMessage message = new MimeMessage(mailSession);
            // от кого
            message.setFrom(new InternetAddress("javaseniorweb@gmail.com"));
            //message.addRecipient(Message.RecipientType.TO);
            message.setSubject(subject);
            message.setContent(text, "text/html; charset=utf-8");

            mailTransport.sendMessage(message, InternetAddress.parse(to));
            mailTransport.close();
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
//smtp.gmail.com javaseniorweb@gmail.com cuofqfghrtwadidp
}
