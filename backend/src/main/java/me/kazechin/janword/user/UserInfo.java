package me.kazechin.janword.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfo implements UserDetails {

	private Integer userId;

	private String password;

	private String username;

	private List<GrantedAuthority> authorityList;

	public UserInfo(Integer userId, String username, List<GrantedAuthority> authorityList) {
		this(userId, username, null, authorityList);
	}

	public UserInfo(Integer userId, String username, String password, List<GrantedAuthority> authorityList) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorityList = authorityList;
	}

	public UserInfo(Integer userId, String username) {
		this(userId, username, null, null);
	}

	public Integer getUserId() {
		return userId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorityList;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

}
