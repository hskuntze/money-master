package br.com.kuntzedev.moneymaster.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig {
	@Value("${jwt.secret}")
	private String secret;

	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	JwtAccessTokenConverter converter() {
		JwtAccessTokenConverter tc = new JwtAccessTokenConverter();
		tc.setSigningKey(secret);
		return tc;
	}

	@Bean
	JwtTokenStore store() {
		return new JwtTokenStore(converter());
	}
	
//	@Bean
//	MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//	  return registry -> registry.config().commonTags("application", "money-master");
//	}
}