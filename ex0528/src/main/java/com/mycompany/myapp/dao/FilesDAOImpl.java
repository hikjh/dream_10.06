package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.vo.FileLoad;

@Repository
public class FilesDAOImpl implements FilesDAO{
	
	@Autowired
	private SqlSession session;

	@Override
	public int registerFile(FileLoad fileLoad) {
		return session.insert("files.registerFile", fileLoad);
	}

	@Override
	public int fileDelete(int articleNo) {
		return session.delete("files.deleteFile", articleNo);
	}

	@Override
	public List<FileLoad> fileList(int no) {
		return session.selectList("files.selectFileList", no);
	}

	@Override
	public FileLoad fileDetail(int no) {
		return session.selectOne("files.fileDetail", no);
	}

}
