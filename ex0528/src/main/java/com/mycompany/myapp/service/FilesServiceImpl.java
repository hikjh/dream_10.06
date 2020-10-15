package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.FilesDAO;
import com.mycompany.myapp.vo.FileLoad;

@Service
public class FilesServiceImpl implements FilesService{
	
	@Autowired
	private FilesDAO filesDAO;

	@Override
	public List<FileLoad> fileList(int no) {
		return filesDAO.fileList(no);
	}

	@Override
	public FileLoad fileDetail(int no) {
		// TODO Auto-generated method stub
		return filesDAO.fileDetail(no);
	}

}
