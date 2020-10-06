package com.mycompany.myapp.service;

import com.mycompany.myapp.vo.View;

public interface ViewsService {

	public int totalCnt(int no);

	public int userView(View view);

	public int registerView(View view);

}
