package com.mycompany.myapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mycompany.myapp.vo.Article;

public interface ArticlesService {

	public int registerArticle(Article article, MultipartHttpServletRequest request, HttpSession session) throws IOException;
	
	public Map<String, Object> articleList(int page, Map<String, Object> map);

	public Article articleDetail(int no);

	public int articleDelete(int articleNo);

	public int articleUpdate(Article article);

	public List<Article> myArticles(String userId);

	
}
