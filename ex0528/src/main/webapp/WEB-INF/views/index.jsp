<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
<img src="/upload/030b8825-66c3-4b08-8c61-b768fd770237_sgh3.jpg">
    <form id="loginForm" action="/article" method="post">
        <fieldset>
            <legend>로그인</legend>
            <div class="user_info">
                <div>아이디</div>
                <input id="userId" class="input_info" name="userId" type="text" placeholder="id">
                <div>비밀번호</div>
                <input id="userPw" class="input_info" name="userPw" type="password" placeholder="password">
                <!-- <input type="hidden" id="loginUser" name="loginuser" value="${loginUser}"> -->
                <div class="msg"></div>
            </div>
            <div>
                <button id="loginBtn" class="btn" type="button">로그인</button>
                <button id="signBtn" class="btn" type="button"><a href="/sign">회원가입</a></button>
            </div>
        </fieldset>
    </form>
</body>
<script src="/js/jquery.js"></script>
<script>
    $("#loginBtn").click(function () {
        var userId = $("#userId").val();
        var userPwd = $("#userPw").val();

        if(userId == "" || userPwd == "") {
            // userId 또는 userPw를 입력하지 않을 경우
            $(".msg").addClass("ok").text("아이디랑 비밀번호 둘다 입력해주세요");
            return false;
        }
        $.ajax({
            url: "/ajax/session",
            dataType: "json",
            data: {
            	userId : userId,
            	userPwd : userPwd
            	},
            type: "POST",
            error: function () {
                alert("ERROR");
            },
            success: function (data, status, code) {
            	console.log(data.loginUserPwd);
            	console.log(data.inputPwd);
            	if(data.loginUserId == null) {
            		// userId가 등록되어 있지 않을 경우
            		$(".msg").addClass("ok").text("등록되지 않은 ID입니다");
            		$(".input_info").val("");
            		return false;
            		
            	} else {
            		if(data.loginUserId != userId || data.loginUserPwd != data.inputPwd) {
                		// 입력한 userId 또는 userPwd가 다를 경우
                		
                		$(".msg").addClass("ok").text("아이디 또는 비밀번호가 틀렸습니다");
                		$(".input_info").val("");
                		return false;
                		
                	} else {
                		//$(".msg").removeClass("ok").text("");
                		$("#loginForm").submit();
                	}
            	}
            }
         }); // ajax() end
    }); // loginBtn click() end
</script>
</html>
