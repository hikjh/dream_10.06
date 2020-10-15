package com.mycompany.myapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mycompany.myapp.util.MessageUtil;
import com.mycompany.myapp.util.UserDelete;
import com.mycompany.myapp.util.UserLogin;
import com.mycompany.myapp.util.UserSign;

@ControllerAdvice
public class Error {
	
	@ExceptionHandler(UserLogin.class)
	public String nullex1(Exception e) {
		
		System.out.println("");
		return "error";
	}
	
	@ExceptionHandler(UserSign.class)
	public String nullex2(Exception e, Model model) {
		
		System.out.println("3. ControllerAdvice - UserSignExceptionHandler");
		model.addAttribute("errCode", 10001);
		model.addAttribute("errMsg", MessageUtil.getMessage("10001"));
		return "error";
	}
	
	@ExceptionHandler(UserDelete.class)
	public String nullex3(Exception e) {
		
		System.out.println("#################### ControllerAdvice : 사용자 해지 ####################");
		return "error";
	}
}
