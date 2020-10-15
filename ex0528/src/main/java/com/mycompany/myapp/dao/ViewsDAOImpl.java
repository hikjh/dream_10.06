package com.mycompany.myapp.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.vo.View;

@Repository
public class ViewsDAOImpl implements ViewsDAO{

	@Autowired
	private SqlSession session;
	
	@Override
	public int totalCnt(int no) {
		return session.selectOne("views.totalCnt", no);
	}

	@Override
	public int userView(View view) {
		return session.selectOne("views.userView", view);
	}

	@Override
	public int registerView(View view) {
		return session.insert("views.registerView", view);
	}

	@Override
	public int viewDelete(int articleNo) {
		
		return session.delete("views.viewDelete", articleNo);
	}

}
