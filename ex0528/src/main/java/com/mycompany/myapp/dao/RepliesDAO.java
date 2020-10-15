package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.vo.PageVO;
import com.mycompany.myapp.vo.Reply;

public interface RepliesDAO {

	public int replyRegister(Reply reply);

	public List<Reply> replyList(PageVO pageVO);

	public int replyTotalCnt(int articleNo);

	public int replyDelete(int articleNo);

	public List<Reply> myReplies(String userId);

}
