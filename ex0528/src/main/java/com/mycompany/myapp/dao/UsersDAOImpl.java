package com.mycompany.myapp.dao;


import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.vo.User;

@Repository
public class UsersDAOImpl implements UsersDAO{

	@Autowired
	private SqlSession session;
	
	@Override
	public int checkUser(String userId) {
		return session.selectOne("users.checkUser", userId);
	}

	@Override
	public int registerUser(User user) {
		return session.insert("users.registerUser", user);
	}

	@Override
	public User login(User user) {
		return session.selectOne("users.login", user);
	}

	@Override
	public int updatePwd(User loginUser) {
		return session.update("users.updatePwd", loginUser);
	}

	@Override
	public int withdrawUser(User loginUser) {
		return session.update("users.withdrawUser", loginUser);
	}


}
