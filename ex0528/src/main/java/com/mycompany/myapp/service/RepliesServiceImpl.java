package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.RepliesDAO;
import com.mycompany.myapp.vo.PageVO;
import com.mycompany.myapp.vo.Reply;

@Service
public class RepliesServiceImpl implements RepliesService{

	@Autowired
	private RepliesDAO repliesDAO;
	@Override
	public int replyRegister(Reply reply) {
		
		return repliesDAO.replyRegister(reply);
	}
	@Override
	public Map<String, Object> replyList(int page, int articleNo) {
		
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		
		int total = repliesDAO.replyTotalCnt(articleNo);
		PageVO pageVO = new PageVO(page, total);
		pageVO.setArticleNo(articleNo);
		
		List<Reply>	replyLists = repliesDAO.replyList(pageVO);
		
		map.put("replies", replyLists);
		map.put("total", total);
		return map;
	}
	@Override
	public List<Reply> myReplies(String userId) {
		
		return repliesDAO.myReplies(userId);
	}

}
