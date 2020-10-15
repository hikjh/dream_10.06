package com.mycompany.myapp.service;

import java.util.List;

import com.mycompany.myapp.vo.FileLoad;

public interface FilesService {

	public List<FileLoad> fileList(int no);

	public FileLoad fileDetail(int no);

}
