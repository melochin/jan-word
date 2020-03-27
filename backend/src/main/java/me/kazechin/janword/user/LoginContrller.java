package me.kazechin.janword.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
	public Map<String, Object> login(@RequestBody Map<String, String> map) {
		String username = map.get("username");
		String password = map.get("password");

		UserInfo userInfo = userDao.findByName(username);

		if (new BCryptPasswordEncoder(10)
				.matches(password, userInfo.getPassword()) == false) {
			throw new RuntimeException("password is wrong");
		}


		Map<String, Object> res = new HashMap<>();

		List<String> roles = userInfo.getAuthorities()
				.stream()
				.map(a -> a.getAuthority().split("_")[1])
				.collect(toList());
		res.put("token", tokenService.encode(userInfo));
		res.put("username", username);
		res.put("role", roles);
		return res;
	}

}
