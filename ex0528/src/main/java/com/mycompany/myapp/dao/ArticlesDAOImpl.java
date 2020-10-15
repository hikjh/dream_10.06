package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.vo.Article;
import com.mycompany.myapp.vo.PageVO;

@Repository
public class ArticlesDAOImpl implements ArticlesDAO {

	@Autowired
	private SqlSession session;
	
	@Override
	public int registerArticle(Article article) {
		System.out.println("##### 3 #####");
		System.out.println("##### DAOImpl VO Title : "+article.getTitle());
		System.out.println("##### DAOImpl VO Content : "+article.getContent());
		
		return session.insert("articles.registerArticle", article);
	}

	@Override
	public List<Article> articleList(PageVO pageVO) {
		return session.selectList("articles.articleList", pageVO);
	}

	@Override
	public int totalArticleList(PageVO pageVO) {
		return session.selectOne("articles.totalArticleList", pageVO);
	}

	@Override
	public Article articleDetail(int no) {
		return session.selectOne("articles.articleDetail", no);
	}

	@Override
	public int articleDelete(int articleNo) {
		return session.delete("articles.deleteArticle", articleNo);
	}

	@Override
	public int articleUpdate(Article article) {
		return session.update("articles.updateArticle", article);
	}

	@Override
	public List<Article> myArticles(String userId) {
		return session.selectList("articles.myArticle", userId);
	}

}
