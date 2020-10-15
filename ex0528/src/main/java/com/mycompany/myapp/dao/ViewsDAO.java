package com.mycompany.myapp.dao;

import com.mycompany.myapp.vo.View;

public interface ViewsDAO {

	public int totalCnt(int no);

	public int userView(View view);

	public int registerView(View view);

	public int viewDelete(int articleNo);

}
