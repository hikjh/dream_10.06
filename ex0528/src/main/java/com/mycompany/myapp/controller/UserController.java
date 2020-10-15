package com.mycompany.myapp.controller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mycompany.myapp.service.ArticlesService;
import com.mycompany.myapp.service.RepliesService;
import com.mycompany.myapp.service.UsersService;
import com.mycompany.myapp.util.UserSign;
import com.mycompany.myapp.vo.Article;
import com.mycompany.myapp.vo.Reply;
import com.mycompany.myapp.vo.User;

@Controller
public class UserController {
	
	@Autowired
	private UsersService usersService;
	@Autowired
	private ArticlesService articlesService;
	@Autowired
	private RepliesService repliesService;
	
	
	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public String signUp() {
		return "sign";
	}
	
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public String signUp(User user) throws Exception {
		//user = null;
		if(user != null) {
			String userName = new String(user.getUserName().getBytes("8859_1"), "UTF-8");
			user.setUserName(userName);
			usersService.registerUser(user);
		} else {
			System.out.println("1. UserController");
			throw new UserSign();
		}
		return "index";
	}

	@RequestMapping(value = "/session", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.removeAttribute("loginUser"); // 세션 한개 삭제
		session.invalidate(); 				  // 세션 초기화
		return "index";
	}
	
	@RequestMapping(value = "/myPage", method = RequestMethod.GET)
	public String myPage(Model model, HttpSession session) {
		
		User loginUser = (User) session.getAttribute("loginUser");
		
		List<Article> myArticles = articlesService.myArticles(loginUser.getUserId());
		List<Reply> myReplies = repliesService.myReplies(loginUser.getUserId());
		model.addAttribute("myArticles", myArticles);
		model.addAttribute("myReplies", myReplies);
		return "myPage";
	}
}
