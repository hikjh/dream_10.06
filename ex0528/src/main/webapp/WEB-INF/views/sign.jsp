<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="css/sign.css">
</head>
<body>
<form id="signForm" action="/sign" method="post">
    <fieldset>
        <legend>회원가입</legend>
        <div class="user_info">
            <div>이름</div>
            <input id="userName" name="userName" type="text" placeholder="name">
            <div>아이디</div>
            <input id="userId" name="userId" type="text" placeholder="id">
            <span><button id="checkId" type="button">중복체크</button></span>
            <div>비밀번호</div>
            <input id="userPwd" name="userPwd" type="password" placeholder="password">
            <div>비밀번호 확인</div>
            <input id="checkPwd" name="checkPwd" type="password" placeholder="check password">
            <div class="msg"></div>
        </div>
        <div>
            <button id="signBtn" class="btn" type="submit">등록</button>
            <button id="cancelBtn" class="btn"><a href="/">취소</a></button>
            <input id="checkUser" name="checkUser" type="hidden" value="${checkUser}">
        </div>
    </fieldset>
</form>
</body>
<script src="/js/jquery.js"></script>
<script>

    var $msg = $(".msg");
    var nameExp = /^[가-힣]{2,6}$/;
    var idExp = /^(?=.*?[a-z])(?=.*?[0-9]).{4,16}$/;
    var pwdExp = /^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
	var result = "";
	
    function testName() {
        var userName = $("#userName").val();
		
        if(userName == "") {
            $msg.addClass("ok").text("이름 입력");
        } else if(!nameExp.test(userName)) {
            // console.log(nameExp.test(userName));
            $msg.addClass("ok").text("이름은 한글 2~6 글자로 입력");
        } else {
            $(".msg").removeClass("ok").text("");
            return true;
        }
    } // testName() end;

    function testId() {
        var userId = $("#userId").val();
        if(userId == "") {
            $msg.addClass("ok").text("아이디 입력");
        } else if(!idExp.test(userId)) {
            console.log(idExp.test(userId));
            $msg.addClass("ok").text("아이디는 영어 혹은 숫자 4~16 글자로 입력");
        } else {
            $msg.removeClass("ok").text("");
            return true;
        }
    }// testId() end;

    function testPwd() {
        var userPwd = $("#userPwd").val();
        if(userPwd == "") {
            $msg.addClass("ok").text("비밀번호 입력");
        } else if(!pwdExp.test(userPwd)) {
            $msg.addClass("ok").text("비밀번호는 영어 숫자 특수문자 포함 8~16 글자로 입력");
        } else {
            $msg.removeClass("ok").text("");
            return true;
        }
    }// testPwd() end;

    function testCheckPwd() {
        var userPwd = $("#userPwd").val();
        var checkPwd = $("#checkPwd").val();
        
        if(checkPwd == "") {
            $msg.addClass("ok").text("비밀번호 확인 입력");
        } else if(userPwd != checkPwd) {
            $msg.addClass("ok").text("입력한 비밀번호가 일치하지 않음");
        } else {
            $msg.removeClass("ok").text("");
            return true;
        }
    }// testCheckPwd() end;
    
    function checkIdDuplicate() {
    	
    	if(!testId()) return false;
        var userId = $("#userId").val();
        
        $.ajax({
           url: "/ajax/checkUser",
           dataType: "json",
           data: {userId : userId},
           type: "GET",
           error: function () {
               alert("ERROR");
           },
           success: function (data) {
        	   if(data > 0) {
        		   alert("이미존재하는 아이디입니다");
        		   $("#userId").val("");
        		   result = "F";
        	   } else {
        		   alert("사용가능");
        		   result = "S";
        	   }
           }
        }); //checkId ajax() end
    }//checkIdDuplicate() end
    
    $("#checkId").click(function () {
    	checkIdDuplicate();
	});
    
    $("#signBtn").click(function () {
        if(!testName()) return false;       //testName()이 true가 아니면 return 막아줌
        if(!testId()) return false;         //testId()이 true가 아니면 return 막아줌
        if(!testPwd()) return false;        //testPwd()이 true가 아니면 return 막아줌
        if(!testCheckPwd()) return false;   //testCheckPwd()이 true가 아니면 return 막아줌
        if(result == null || result == '' || result == 'F') {
        	alert("중복 체크 누르세요");
        	result = '';
        	return false;
        }
    }); // signBtn click() end
</script>
</html>