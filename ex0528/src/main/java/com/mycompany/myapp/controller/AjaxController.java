package com.mycompany.myapp.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.myapp.service.ArticlesService;
import com.mycompany.myapp.service.RepliesService;
import com.mycompany.myapp.service.UsersService;
import com.mycompany.myapp.util.UserDelete;
import com.mycompany.myapp.vo.Article;
import com.mycompany.myapp.vo.Reply;
import com.mycompany.myapp.vo.User;

@Controller
@RequestMapping(value = "/ajax", method = RequestMethod.GET)
public class AjaxController {
	
	@Autowired
	private UsersService usersService;
	@Autowired
	private ArticlesService articlesService;
	@Autowired
	private RepliesService repliesService;
 	
	@ResponseBody
	@RequestMapping(value = "/checkUser")
	public int userCheck(String userId) {
		return usersService.checkUser(userId);
	}//회원가입 시 등록되었는지 판단
	
	@ResponseBody
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	public Map<String, Object> login(User user, HttpSession session) throws Exception {
		return usersService.login(user, session);
	}//로그인
	
	@ResponseBody
	@RequestMapping(value = "/article/page/{page}", method = RequestMethod.POST)
	public Map<String, Object> articleList(@PathVariable int page, @RequestBody Map<String, Object> map) {
		
		String startDate = "2020-06-17";
		String endDate = "2020-06-19";
		
		//System.out.println("@@@@@@@@@@@ startDate : "+startDate);
		///System.out.println("@@@@@@@@@@@ endDate : "+endDate);
		
		//int kmitotal = getKmiCount(startDate, endDate);
		//System.out.println("@@@@@@@@@@@ kmitotal : "+kmitotal);
		/*
		for (int i = 1; i < kmitotal+1; i++) {
			
			String userDn = getUserDn(i, startDate, endDate);
			//System.out.println("Result userDn = "+userDn);
			
			//Map<Object, String> certInfo = getCert(userDn, date);
			//System.out.println("Result cert = "+certInfo.get("cert"));
			//System.out.println("Result priKey = "+certInfo.get("priKey"));
		}
		*/
		return articlesService.articleList(page, map);
	}//게시글 리스트
	
	
	@ResponseBody
	@RequestMapping(value = "/article/{articleNo}", method = RequestMethod.DELETE)
	public int articleDelete(@PathVariable int articleNo) {
		return articlesService.articleDelete(articleNo);
	}//게시글 삭제
	
	@ResponseBody
	@RequestMapping(value = "/article/{articleNo}/{title}/{content}", method = RequestMethod.PUT)
	public Article articleUpdate(Article article, @PathVariable int articleNo, @PathVariable String title, @PathVariable String content, Model model) {
		/*
		 * @PathVariable 뒤 변수는 RequestMapping 변수와 동일하게
		 * PUT을 쓸라니깐 title, content가 없다함
		 * @PathVariable String title, @PathVariable String content를 쓰니깐 {title}, {content}가 필요하다함
		 * 나중에 map으로 받기
		*/
		article.setNo(articleNo);
		article.setTitle(title);
		article.setContent(content);
		
		articlesService.articleUpdate(article);
		return article;
	}//게시글 수정
	
	@ResponseBody
	@RequestMapping(value = "/article/{articleNo}/reply", method = RequestMethod.POST)
	public int replyRegister(Reply reply, @PathVariable int articleNo) {
		return repliesService.replyRegister(reply);
	}//댓글 등록
	
	@ResponseBody
	@RequestMapping(value = "/article/{articleNo}/reply/page/{page}", method = RequestMethod.POST)
	public Map<String, Object> replyList(@PathVariable int page, @PathVariable int articleNo) {
		return repliesService.replyList(page, articleNo);
	}//댓글 리스트
	
	@ResponseBody
	@RequestMapping(value = "/curPwd", method = RequestMethod.POST)
	public Map<String, Object> curPwd(String curPwd, HttpSession session) throws Exception {
		return usersService.curPwd(curPwd, session);
	}//현재 비밀번호 
	
	@ResponseBody
	@RequestMapping(value = "/myPage/updatePwd", method = RequestMethod.POST)
	public int updatePwd(String newPwd, HttpSession session) throws UserDelete, NoSuchAlgorithmException {
		return usersService.updatePwd(newPwd, session);
	}//비밀번호 변경
	
	@ResponseBody
	@RequestMapping(value = "/myPage/withdrawUser", method = RequestMethod.GET)
	public int withdrawUser(HttpSession session) throws UserDelete {
		return usersService.withdrawUser(session);
	}//회원탈퇴
	
	/*
	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String withdrawUser22(HttpSession session) throws Exception {
		return "hello";
	}
	
	
	// Get Total Count
	private static int getKmiCount(String startDate, String endDate) {
		int result = 0;
		//To-do 재현 
		// 입력받은 날짜에 맞춰 조회 가능 하도록. 
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String url = "jdbc:mysql://127.0.0.1:3306/test1?serverTimezone=UTC";
		String id = "kjh";
		String pw = "1234";
		
		try {
			//System.out.println("######## date :"+date);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
			
			// test DB
			//String sql = "SELECT COUNT(*) FROM articles WHERE regdate=?";
			
			String sql = "SELECT COUNT(*) FROM articles WHERE regdate BETWEEN ? AND ?";
			
			// kmi_cert 
			// String sql = "SELECT COUNT(*) FROM kmi_cert WHERE insert_date BETWEEN ? AND ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return result;
		
	}
	
	// Get User DN
	private static String getUserDn(int index, String startDate, String endDate) {
		
		String resultRownum = "";
		String resultUserDn = "";
		String resultRegdate = "";
		
		//To-do 재현
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String url = "jdbc:mysql://127.0.0.1:3306/test1?serverTimezone=UTC";
		String id = "kjh";
		String pw = "1234";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
			
			// test DB
			String sql = "SELECT b.rownum, title, regdate FROM(SELECT @rownum:=@rownum+1 as rownum, title, regdate FROM(SELECT title, regdate FROM articles WHERE (@rownum:=0)=0) a WHERE regdate BETWEEN ? AND ?) b WHERE rownum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			pstmt.setInt(3, index);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				//System.out.println("여기오니");
				
				resultRownum = rs.getString("rownum");
				resultUserDn = rs.getString("title");
				resultRegdate = rs.getString("regdate");   
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception rse) {}
			try {pstmt.close();} catch (Exception pstmte) {}
			try {conn.close();} catch (Exception conne) {}
		}
		
		return resultUserDn;
	}
	
	private Map<Object, String> getCert(String userDn, String date) {
		
		Map<Object, String> map = new ConcurrentHashMap<Object, String>();
		
		String cert = "";
		String priKey = "";
		
		//To-do 재현
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String url = "jdbc:mysql://127.0.0.1:3306/test1?serverTimezone=UTC";
		String id = "kjh";
		String pw = "1234";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, id, pw);
			
			// test DB
			String sql = "SELECT writer, content\r\n" + 
					"FROM articles\r\n" + 
					"WHERE title =?\r\n" + 
					"AND regdate =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDn);
			pstmt.setString(2, date);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				cert = rs.getString(1);
				priKey = rs.getString(2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {rs.close();} catch (Exception rse) {}
			try {pstmt.close();} catch (Exception pstmte) {}
			try {conn.close();} catch (Exception conne) {}
		}
		
		map.put("cert", cert);
		map.put("priKey", priKey);
		
		//System.out.println("######## Get Certification ########");
		//System.out.println("");
		
		return map;
	}
	*/
}
