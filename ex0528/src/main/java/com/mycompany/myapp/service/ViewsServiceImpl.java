package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.ViewsDAO;
import com.mycompany.myapp.vo.View;

@Service
public class ViewsServiceImpl implements ViewsService{

	@Autowired
	private ViewsDAO viewsDAO;
	
	@Override
	public int totalCnt(int no) {
		return viewsDAO.totalCnt(no);
	}

	@Override
	public int userView(View view) {
		return viewsDAO.userView(view);
	}

	@Override
	public int registerView(View view) {
		return viewsDAO.registerView(view);
		
	}

}
