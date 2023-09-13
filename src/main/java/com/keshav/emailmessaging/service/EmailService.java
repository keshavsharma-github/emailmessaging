package com.keshav.emailmessaging.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.keshav.emailmessaging.model.EmailDetails;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	private static Logger LOGGER = LogManager.getLogger(EmailService.class);
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;
	private static final String emailSubject = "Salary Slip for month %s";

	public void sendEmail(String recipientEmail, EmailDetails emailDTO) {
		if(recipientEmail != null && recipientEmail != ""){
			try {
				LocalDate currentDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
				String currentMonth = currentDate.format(formatter);

				StringBuilder htmlTable = new StringBuilder("<table border='1'><tr><th>Name</th><th>Salary</th></tr>");
				htmlTable.append("<tr><td>").append(emailDTO.getName()).append("</td><td>").append(emailDTO.getSalary())
						.append("</td></tr></table>");

				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(sender);
				helper.setTo(recipientEmail);
				helper.setSubject(String.format(emailSubject, currentMonth));

				helper.setText(htmlTable.toString(), true); // Set the second parameter to 'true' to enable HTML content

				// Sending the mail
				javaMailSender.send(message);
				LOGGER.info("Email sent successfully to {} with email {}.", emailDTO.getName(), recipientEmail);
			} catch (Exception e) {
				LOGGER.error("Error while Sending Mail", e);
			}
		}
	}
}