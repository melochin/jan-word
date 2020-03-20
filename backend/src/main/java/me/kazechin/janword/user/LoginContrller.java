package me.kazechin.janword.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
public class LoginContrller {

	private UserDao userDao;

	private TokenService tokenService;

	public LoginContrller(UserDao userDao, TokenService tokenService) {
		this.userDao = userDao;
		this.tokenService = tokenService;
	}

	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> map) {
		String username = map.get("username");
		String password = map.get("password");

		UserInfo userInfo = userDao.findByName(username);

		if (new BCryptPasswordEncoder(10)
				.matches(password, userInfo.getPassword()) == false) {
			throw new RuntimeException("password is wrong");
		}

		return tokenService.encode(userInfo);
	}

}
