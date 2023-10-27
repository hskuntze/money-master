package br.com.kuntzedev.moneymaster.controllers.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import br.com.kuntzedev.moneymaster.dtos.UserBasicDTO;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = -3395129749918866768L;
	
	private UserBasicDTO user;
	private Locale locale;
	private String appUrl;

	public OnRegistrationCompleteEvent(UserBasicDTO user, Locale locale, String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public UserBasicDTO getUser() {
		return user;
	}

	public void setUser(UserBasicDTO user) {
		this.user = user;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
}