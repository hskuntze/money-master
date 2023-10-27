package br.com.kuntzedev.moneymaster.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@PropertySource(value = {"classpath:email-config.properties"})
public class EmailConfig {
	private static final String ENCODING = "UTF-8";
	
	@Value("${email.host}")
	private String emailHost;

	@Value("${email.port}")
	private Integer emailPort;
	
	@Value("${email.username}")
	private String emailUsername;
	
	@Value("${email.password}")
	private String emailPassword;
	
	@Bean
	ResourceBundleMessageSource emailMessageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("mailMessages");
		return messageSource;
	}
	
	@Bean
	ITemplateResolver thymeleafTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML");
		templateResolver.setCharacterEncoding(ENCODING);
		return templateResolver;
	}
	
	@Bean(name = "mailSender")
	JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl jms = new JavaMailSenderImpl();
		jms.setHost(emailHost);
		jms.setPort(emailPort);

		jms.setUsername(emailUsername);
		jms.setPassword(emailPassword);

		Properties props = jms.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "false");

		return jms;
	}
}