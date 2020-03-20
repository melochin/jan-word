package me.kazechin.janword.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class TokenService {

	@Value("JWT.key")
	private String key;

	private UserDao userDao;

	public TokenService(UserDao userDao) {
		this.userDao = userDao;
	}

	public String encode(UserDetails user) {

		Date expiredDate = new Date();
		expiredDate.setTime(new Date().getTime() + 6 * 60 * 60 * 1000);

		return JWT.create()
				.withClaim("username", user.getUsername())
				.withExpiresAt(expiredDate)
				.sign(Algorithm.HMAC256(key));
	}

	public UserInfo decode(String token) {
		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(key))
				.build()
				.verify(token);

		String username = decodedJWT.getClaim("username").asString();

		return userDao.findByName(username);
	}

}
