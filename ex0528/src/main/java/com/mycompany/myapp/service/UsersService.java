package com.mycompany.myapp.service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mycompany.myapp.vo.User;

public interface UsersService {

	public int checkUser(String userId);

	public int registerUser(User user) throws Exception;

	public Map<String, Object> login(User user, HttpSession session) throws Exception;

	public Map<String, Object> curPwd(String curPwd, HttpSession session) throws NoSuchAlgorithmException;

	public int updatePwd(String newPwd, HttpSession session) throws NoSuchAlgorithmException;

	public int withdrawUser(HttpSession session);

}
