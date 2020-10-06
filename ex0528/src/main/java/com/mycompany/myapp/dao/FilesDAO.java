package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.vo.FileLoad;

public interface FilesDAO {

	public int registerFile(FileLoad fileLoad);

	public int fileDelete(int articleNo);

	public List<FileLoad> fileList(int no);

	public FileLoad fileDetail(int no);

}
