package com.mycompany.myapp.dao;



import com.mycompany.myapp.vo.User;

public interface UsersDAO {

	public int checkUser(String userId);

	public int registerUser(User user);

	public User login(User user);

	public int updatePwd(User loginUser);

	public int withdrawUser(User loginUser);

}
