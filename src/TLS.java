import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TLS {
	public void send(Properties props) throws JsonProcessingException {
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("userName"), props.getProperty("password"));
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(props.getProperty("from")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(props.getProperty("to")));
			message.setSubject(props.getProperty("subject"));
			if(props.containsKey("fileName")) {
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(props.getProperty("body"));
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(props.getProperty("fileName"));
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(props.getProperty("fileName"));
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
			} else {
				message.setText(props.getProperty("body"));
			}
			Transport.send(message);
			System.out.println("Done TLS");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
