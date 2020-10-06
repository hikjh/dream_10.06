package com.mycompany.myapp.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mycompany.myapp.dao.ArticlesDAO;
import com.mycompany.myapp.dao.FilesDAO;
import com.mycompany.myapp.dao.RepliesDAO;
import com.mycompany.myapp.dao.ViewsDAO;
import com.mycompany.myapp.util.PaginateUtil;
import com.mycompany.myapp.vo.Article;
import com.mycompany.myapp.vo.FileLoad;
import com.mycompany.myapp.vo.PageVO;
import com.mycompany.myapp.vo.User;

@Service
public class ArticlesServiceImpl implements ArticlesService {

	@Autowired
	private ArticlesDAO articlesDAO;
	
	@Autowired
	private RepliesDAO repliesDAO;

	@Autowired
	private ViewsDAO viewsDAO;
	
	@Autowired
	private FilesDAO filesDAO;
	
	@Autowired
	private PaginateUtil paginateUtil;
	//private static final String UPLOAD_PATH = "C:\\dream\\workspace\\ex0528\\src\\main\\webapp\\upload\\";
	private static final String UPLOAD_PATH = "/var/www/upload/";
	
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED) //file등록이 안될 경우 게시글 등록이 안되도록 처리 
	public int registerArticle(Article article, MultipartHttpServletRequest request, HttpSession session) throws IOException {
		
		System.out.println("##### 2 #####");
		
		List<MultipartFile> files = request.getFiles("file");
		
		//user
		User loginUser = (User) session.getAttribute("loginUser");
		
		// article -> 개발서버에서 못읽음
		// String title = new String(article.getTitle().getBytes("8859_1"), "UTF-8");
		// String content = new String(article.getContent().getBytes("8859_1"), "UTF-8");
		
		System.out.println("##### ServiceImpl VO Title : "+article.getTitle());
		System.out.println("##### ServiceImpl VO Content : "+article.getContent());
		
		System.out.println("##### ServiceImpl Request Title : "+request.getParameter("title"));
		System.out.println("##### ServiceImpl Request Content : "+request.getParameter("content"));
		
		String title = new String(request.getParameter("title").getBytes("8859_1"), "UTF-8");
		String content = new String(request.getParameter("content").getBytes("8859_1"), "UTF-8");
		
		System.out.println("##### Result title: "+title);
		System.out.println("##### Result content: "+content);
		
		article.setTitle(title);
		article.setContent(content);
		
		int result = articlesDAO.registerArticle(article); //게시글 등록;
		// file
		String filePath = UPLOAD_PATH;
		for (MultipartFile file : files) {
			
			String originalFileName = new String(file.getOriginalFilename().getBytes("8859_1"), "UTF-8");
			String fileName = new String(saveFile(file).getBytes("8859_1"), "UTF-8");
			int find = originalFileName.indexOf(".");
			
			if(originalFileName != null) {
				String extension = originalFileName.substring(find+1, originalFileName.length());
				if(result==1) {
					
					FileLoad fileLoad = new FileLoad();
					
					fileLoad.setUserNumber(loginUser.getNo());
					fileLoad.setArticleNumber(article.getNo());
					fileLoad.setFilePath(filePath);
					fileLoad.setFileName(fileName);
					fileLoad.setOriginalFileName(originalFileName);
					fileLoad.setExtension(extension);

					filesDAO.registerFile(fileLoad); //파일 등록
				}
			}
		}
		
		return result;
	}

	private String saveFile(MultipartFile file) {
		
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString()+"_"+file.getOriginalFilename();
		
		File saveFile = new File(UPLOAD_PATH, fileName);
		
		try {
			file.transferTo(saveFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fileName;
	}

	@Override
	public Map<String, Object> articleList(int page, Map<String, Object> map) {
		
		PageVO pageVO = new PageVO(page, 10);
		
		String search = map.get("search").toString();
		String searchType = map.get("searchType").toString();

		pageVO.setSearch(search);
		pageVO.setSearchType(searchType);
		
		List<Article> articleLists = articlesDAO.articleList(pageVO);
		int total = articlesDAO.totalArticleList(pageVO);
		map.put("articles", articleLists);
		map.put("paginate", paginateUtil.getPaginate(page, total, 10, 5, "/ajax/article"));
		
		return map;
	}

	@Override
	public Article articleDetail(int no) {
		return articlesDAO.articleDetail(no);
	}

	@Override
	public int articleDelete(int articleNo) {
		// 댓글, 조회수, 파일 삭제 후 게시글 삭제 
		filesDAO.fileDelete(articleNo);
		viewsDAO.viewDelete(articleNo);
		repliesDAO.replyDelete(articleNo);
		return articlesDAO.articleDelete(articleNo);
	}

	@Override
	public int articleUpdate(Article article) {
		return articlesDAO.articleUpdate(article);
	}

	@Override
	public List<Article> myArticles(String userId) {
		return articlesDAO.myArticles(userId);
	}
}
