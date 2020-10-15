package com.mycompany.myapp.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mycompany.myapp.service.ArticlesService;
import com.mycompany.myapp.service.FilesService;
import com.mycompany.myapp.service.ViewsService;
import com.mycompany.myapp.util.UserSign;
import com.mycompany.myapp.vo.Article;
import com.mycompany.myapp.vo.FileLoad;
import com.mycompany.myapp.vo.User;
import com.mycompany.myapp.vo.View;

@Controller
public class ArticleController {
	
	@Autowired
	private ArticlesService articlesService;
	@Autowired
	private ViewsService viewsService;
	@Autowired
	private FilesService filesService;
	
	@RequestMapping(value = "/article")
	public String board() {
		return "article";
	}

	@RequestMapping(value = "/articleRegister", method = RequestMethod.POST) //2020.06.17 @RequestMapping value, method 수정
	public String registerArticle(Article article, MultipartHttpServletRequest request, HttpSession session) throws IOException, UserSign {
		
		System.out.println("##### 1 #####");
		User loginUser = (User) session.getAttribute("loginUser");
		System.out.println("##### Controller user : "+session.getAttribute("loginUser"));

		if(loginUser != null) {
			
			System.out.println("##### Controller VO Title : "+article.getTitle());
			System.out.println("##### Controller VO Content : "+article.getContent());
			
			System.out.println("##### Controller Request Title : "+request.getParameter("title"));
			System.out.println("##### Controller Request Content : "+request.getParameter("content"));
			
			article.setWriter(loginUser.getUserId());
			articlesService.registerArticle(article, request, session);
			return "redirect:article";
		} 
		else {
			System.out.println("1. Controller");
			throw new UserSign();
		}
	}
	
	@RequestMapping(value = "/article/{no}/{userNo}", method = RequestMethod.GET)
	public String articleDetail(@PathVariable int no, @PathVariable int userNo, Model model) {
		
		View view = new View();
		view.setArticleNum(no);
		view.setUserNum(userNo);
		
		 //사용자가 조회를 하였는지 판단
		int userCntView = viewsService.userView(view);
		
		if(userCntView == 0) {
			// 안했으면 조회 Count 증가
			viewsService.registerView(view);
		}
		
		// int totalCntView = viewsService.totalCnt(no);
		Article article = articlesService.articleDetail(no);
		List<FileLoad> files = filesService.fileList(no);
		
		model.addAttribute("article", article);
		model.addAttribute("files", files);
		
		return "articleDetail";
	}
	
}
