package me.kazechin.janword.controller;

import me.kazechin.janword.user.UserInfo;

import java.util.List;
import java.util.Map;

public interface MemoryControllerInter {

	Map<String, Object> list(UserInfo userInfo);

	void finish(UserInfo userInfo);

	boolean remember(UserInfo userInfo, Integer id);

	void forget(UserInfo userInfo, Integer id);

}
