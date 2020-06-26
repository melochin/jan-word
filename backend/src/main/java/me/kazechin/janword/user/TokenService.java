package me.kazechin.janword.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kazechin.janword.config.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TokenService {

	public static final String USERNAME = "username";

	private TokenConfig tokenConfig;

	private UserDao userDao;

	@Autowired
	public TokenService(UserDao userDao, TokenConfig tokenConfig) {
		this.userDao = userDao;
		this.tokenConfig = tokenConfig;
	}

	public String encode(UserDetails user) {
		LocalDateTime date = LocalDateTime.now().plusMinutes(tokenConfig.getDuration());

		return JWT.create()
				.withClaim(USERNAME, user.getUsername())
				.withExpiresAt(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))
				.sign(Algorithm.HMAC256(tokenConfig.getSecret()));
	}

	public UserInfo decode(String token) {

		if (tokenConfig.getSpecial() != null && tokenConfig.getSpecial().equals(token)) {
			return userDao.findByName(tokenConfig.getUsername());
		}

		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(tokenConfig.getSecret()))
				.build()
				.verify(token);

		String username = decodedJWT.getClaim(USERNAME).asString();

		return userDao.findByName(username);
	}

}
