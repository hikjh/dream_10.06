package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.vo.Article;
import com.mycompany.myapp.vo.PageVO;

public interface ArticlesDAO {

	public int registerArticle(Article article);

	public List<Article> articleList(PageVO pageVO);

	public int totalArticleList(PageVO pageVO);

	public Article articleDetail(int no);

	public int articleDelete(int articleNo);

	public int articleUpdate(Article article);

	public List<Article> myArticles(String userId);

}
