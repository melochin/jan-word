package me.kazechin.janword.user;

public interface UserDao {

	UserInfo findByName(String username);

	UserInfo findById(Integer id);
}
