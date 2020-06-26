package me.kazechin.janword.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// TODO 打包到新的工具中
@ConfigurationProperties("kazechin.token")
public class TokenConfig {

	private String secret;

	private Long duration = new Long(30);

	private String special;

	private String username;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
