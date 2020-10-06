package com.mycompany.myapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.myapp.service.FilesService;
import com.mycompany.myapp.vo.FileLoad;

@Controller
public class FileController {
	
	@Autowired 
	private FilesService filesService;

	/*
	 * @RequestMapping(value = "/file/{no}") public void fileDownload(@PathVariable
	 * int no, FileLoad fileload, HttpServletRequest req, HttpServletResponse res)
	 * throws IOException {
	 */
	@RequestMapping(value = "/file/{no}")
	public void fileDownload(@PathVariable int no, FileLoad fileload, HttpServletRequest req, HttpServletResponse res) throws IOException {
		fileload = filesService.fileDetail(no);
		
		File file = new File(fileload.getFilePath()+"\\"+fileload.getFileName());	//실제 업로드된 경로 
		String userAgent = req.getHeader("User-Agent");
		
		res.reset();
		res.setContentType("application/octet-stream; ");
		res.setHeader("Content-Disposition", "attachment;filename=\""+fileload.getOriginalFileName()+"\";");	// 파일명 설정
		res.setHeader("Cache-Control", "private;");	
		
		FileInputStream fis = new FileInputStream(file);
		ServletOutputStream so = res.getOutputStream();

		byte[] data = new byte[4096];
		
		while (fis.read(data, 0, 4096) != -1) {
			so.write(data, 0 , 4096);
		}
		fis.close();
		so.flush();
		so.close();
	}
}
