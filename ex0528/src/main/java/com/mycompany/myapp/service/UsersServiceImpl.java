package com.mycompany.myapp.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.UsersDAO;
import com.mycompany.myapp.vo.User;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersDAO usersDAO;
	
	@Override
	public int checkUser(String userId) {
		return usersDAO.checkUser(userId);
	}
	
	// 회원가입
	@Override
	public int registerUser(User user) throws Exception {
		
		String userId = user.getUserId();
		String userPwd = user.getUserPwd();
		String hashedUserPwd = sha256(userPwd+userId);
		
		user.setUserPwd(hashedUserPwd);
		System.out.println("###################################");
		System.out.println("userId : "+userId);
		System.out.println("userPwd : "+userPwd);
		System.out.println("userName : "+user.getUserName());
		System.out.println("hashedUserPwd : "+hashedUserPwd);
		System.out.println("###################################");
		return usersDAO.registerUser(user);
	}
	
	// 로그인
	@Override
	public Map<String, Object> login(User user, HttpSession session) throws Exception {

		User loginUser = usersDAO.login(user);
		
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		
		if(loginUser != null) {
			//session.setAttribute("loginUser", loginUser.getUserId());
			session.setAttribute("loginUser", loginUser);
			map.put("loginUser", loginUser);
			map.put("loginUserId", loginUser.getUserId());
			map.put("loginUserPwd", loginUser.getUserPwd());
			map.put("inputPwd", sha256(user.getUserPwd()+user.getUserId()));
		} else {
			map.put("loginUser", 0);
		}
		return map;
	}
	
	// 현재비밀번호와 일치하는지
	@Override
	public Map<String, Object> curPwd(String curPwd, HttpSession session) throws NoSuchAlgorithmException {
		
		User loginUser = (User) session.getAttribute("loginUser");
		String hashPwd = sha256(curPwd+loginUser.getUserId());
		
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		
		map.put("inputPwd", hashPwd);
		map.put("dbPwd", loginUser.getUserPwd());
		
		return map;
	}
	
	// 비밀번호 변경
	@Override
	public int updatePwd(String newPwd, HttpSession session) throws NoSuchAlgorithmException {

		User loginUser = (User) session.getAttribute("loginUser");
		String hashPwd = sha256(newPwd+loginUser.getUserId());
		
		loginUser.setUserPwd(hashPwd);
		return usersDAO.updatePwd(loginUser);
	}
	
	@Override
	public int withdrawUser(HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		return usersDAO.withdrawUser(loginUser);
	}
	
	
	// Message Digest
	private String sha256(String userPwd) throws NoSuchAlgorithmException{
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(userPwd.getBytes());
		
		return byteToHex1(md.digest());
	}

	// 바이트를 헥사값으로 변환
	private String byteToHex1(byte[] digest) {
		
		StringBuilder builder = new StringBuilder();
		for (byte b : digest) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	
}
