package br.com.kuntzedev.moneymaster.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import br.com.kuntzedev.moneymaster.services.exceptions.BadFormattingException;
import br.com.kuntzedev.moneymaster.services.exceptions.EmailException;

@Service
public class MailService {
	private SpringTemplateEngine templateEngine;
	private JavaMailSender mailSender;

	private static final String ENCODING = "UTF-8";
	private static final Path TMP_PATH = Paths.get("").resolve("moneymaster/tmp");
	
	public MailService(@Qualifier("mailSender") final JavaMailSender jms, final SpringTemplateEngine ste) {
		this.mailSender = jms;
		this.templateEngine = ste;
	}
	
	public String sendHtmlEmailWithThymeleaf(String template, String to, String subject, Map<String, Object> variables) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

			helper.setTo(to);
			helper.setSubject(subject);

			Context context = new Context();
			context.setVariables(variables);

			String content = templateEngine.process(template + ".html", context);
			helper.setText(content, true);
			
			/**
			 *  Essa linha servirá para adicionar uma imagem de cabeçalho
			 *  quando ela existir.
			 *	
			 *  helper.addInline("attachment.png", resourceFile);
			 */

			mailSender.send(message);
			return "Sucesso";
		} catch (MessagingException e) {
			throw new EmailException(e.getMessage(), e);
		}
	}
	
	public String sendEmailWithAttachment(String to, String subject, byte[] file, String text, String fileName) {
		try {
			File pdfFile = formatBytesToFile(file, fileName);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

			helper.setTo(to);
			helper.setSubject(subject);

			Context context = new Context();
			context.setVariable("texto", text);

			String content = templateEngine.process("email-attachment.html", context);
			helper.setText(content, true);
			//helper.addInline("attachment.png", resourceFile);
			helper.addAttachment(pdfFile.getName(), pdfFile);

			mailSender.send(message);

			Files.delete(pdfFile.toPath());

			return "Sucesso";
		} catch (MessagingException | IOException e) {
			throw new EmailException(e.getMessage(), e);
		}
	}

	private File formatBytesToFile(byte[] file, String fileName) {
		Path tempFile = null;
		try {
			tempFile = Files.createTempFile(TMP_PATH, fileName, ".pdf");
			Files.write(tempFile, file);
		} catch (IOException e) {
			throw new BadFormattingException(e.getMessage());
		}
		return tempFile.toFile();
	}
}