package me.kazechin.janword.user;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryUserDao implements UserDao, InitializingBean {

	private Map<Integer, UserInfo> userInfoMap;

	@Override
	public UserInfo findByName(String username) {
		for(Map.Entry<Integer, UserInfo> entry : userInfoMap.entrySet()) {
			if (username.equals(entry.getValue().getUsername())) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public UserInfo findById(Integer id) {
		return userInfoMap.get(id);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Map<Integer, UserInfo> map = new HashMap<>();


		map.put(0, new UserInfo(0, "ADMIN", "$2a$10$U9fekU/rfEuka6ZMIEr2Muz5V2djacf9yt8GmdiSrIlRy7cMtPBdm",
				generateAuthorities("ROLE_USER", "ROLE_ADMIN")));
		map.put(1, new UserInfo(1, "GUEST", "$2a$10$K7OR2jNCEE..RMc2LKM.I.Azf1kGa8qiPdBSZLfsjxrS504hxNme6",
				generateAuthorities("ROLE_USER")));

		this.userInfoMap = map;
	}

	private List<GrantedAuthority> generateAuthorities(String... role) {
		return Stream.of(role).map(SimpleGrantedAuthority::new).collect(toList());
	}

}
