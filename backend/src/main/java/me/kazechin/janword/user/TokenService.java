package me.kazechin.janword.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.kazechin.janword.config.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class TokenService {

	@Autowired
	private TokenConfig tokenConfig;

	private UserDao userDao;

	public TokenService(UserDao userDao) {
		this.userDao = userDao;
	}

	public String encode(UserDetails user) {
		LocalDateTime date = LocalDateTime.now().plusMinutes(tokenConfig.getDuration());

		return JWT.create()
				.withClaim("username", user.getUsername())
				.withExpiresAt(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()))
				.sign(Algorithm.HMAC256(tokenConfig.getSecret()));
	}

	public UserInfo decode(String token) {

		if (tokenConfig.getSpecial() != null && tokenConfig.getSpecial().equals(token)) {
			return getSpecialUser();
		}

		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(tokenConfig.getSecret()))
				.build()
				.verify(token);

		String username = decodedJWT.getClaim("username").asString();

		return userDao.findByName(username);
	}

	private UserInfo getSpecialUser() {
		List<GrantedAuthority> authorities = Stream.of(new String[]{"ROLE_USER", "ROLE_ADMIN"})
				.map(SimpleGrantedAuthority::new)
				.collect(toList());

		return new UserInfo(0, "ADMIN", authorities);
	}

}
