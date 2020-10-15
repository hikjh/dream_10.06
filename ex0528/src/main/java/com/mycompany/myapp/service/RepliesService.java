package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;

import com.mycompany.myapp.vo.Reply;

public interface RepliesService {

	public int replyRegister(Reply reply);

	public Map<String, Object> replyList(int page, int articleNo);

	public List<Reply> myReplies(String userId);

}
