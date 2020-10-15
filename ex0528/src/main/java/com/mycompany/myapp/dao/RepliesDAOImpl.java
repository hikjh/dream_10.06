package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.vo.PageVO;
import com.mycompany.myapp.vo.Reply;

@Repository
public class RepliesDAOImpl implements RepliesDAO{
	
	@Autowired
	private SqlSession session;
	
	@Override
	public int replyRegister(Reply reply) {
		return session.insert("replies.replyRegister", reply);
	}

	@Override
	public List<Reply> replyList(PageVO pageVO) {
		return session.selectList("replies.replyList", pageVO);
	}

	@Override
	public int replyTotalCnt(int articleNo) {
		return session.selectOne("replies.replyTotalCnt", articleNo);
	}

	@Override
	public int replyDelete(int articleNo) {
		return session.delete("replies.deleteReply",articleNo);
	}

	@Override
	public List<Reply> myReplies(String userId) {
		// TODO Auto-generated method stub
		return session.selectList("replies.myReply", userId);
	}

}
