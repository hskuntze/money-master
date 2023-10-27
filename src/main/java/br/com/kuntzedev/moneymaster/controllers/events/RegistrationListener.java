package br.com.kuntzedev.moneymaster.controllers.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.com.kuntzedev.moneymaster.dtos.UserBasicDTO;
import br.com.kuntzedev.moneymaster.services.MailService;
import br.com.kuntzedev.moneymaster.services.TokenService;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MailService mailService;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		Map<String, Object> variables = new HashMap<>();
		UserBasicDTO user = event.getUser();
		
		String token = UUID.randomUUID().toString();
		tokenService.insertVerificationToken(user, token);
		
		String to = user.getEmail();
		String subject = "Confirm your registration to MoneyMaster!";
		String confirmationUrl = event.getAppUrl() + "/users/confirm?token=" + token;
		
		variables.put("text", "Welcome to our app!");
		variables.put("linkText", "Click here to confirm!");
		variables.put("urlConfirmation", confirmationUrl);
		
		mailService.sendHtmlEmailWithThymeleaf("email-confirmation", to, subject, variables);
	}
}