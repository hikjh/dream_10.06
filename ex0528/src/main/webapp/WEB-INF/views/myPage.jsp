<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.mycompany.myapp.vo.User"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>MyPage</title>
    <link rel="stylesheet" href="css/myPage.css">
</head>
<body>
	<div id="wrap">
	<input id="loginUser" name="loginUser" type="hidden" value="${loginUser.no}">
	    <h1>마이페이지</h1>
	    <button><a href="/article">목록으로</a></button>
	    <div>
	        <div class="tab">
	            <span><a href="" id="article">게시글</a></span>
	            <span><a href="" id="reply">댓글</a></span>
	            <span><a href="" id="changePwd">비밀번호변경</a></span>
	        </div>
	        <div class="my_articles">
	            <table>
	                <thead>
	                    <tr>
	                        <td>제목&nbsp;</td>
	                        <td>등록일&nbsp;</td>
	                        <td>수정일</td>
	                    </tr>
	                </thead>
	                <tbody id="articleInfo">
	                <c:forEach var="myArticle" items="${myArticles}" varStatus="status">
	                    <tr>
	                        <td>
	                        	<input type="hidden" value="${myArticle.no}"/>
	                        	<a href="">${myArticle.title}</a>&nbsp;
                        	</td>
	                        <td><fmt:formatDate value="${myArticle.regdate}" pattern="yyyy-MM-dd"/>&nbsp;</td>
	                        <c:choose>
	                        	<c:when test="${myArticle.modifyDate == null}">
	                        		<td>없음</td>
	                        	</c:when>
	                        	<c:otherwise>
	                        		<td><fmt:formatDate value="${myArticle.modifyDate}" pattern="yyyy-MM-dd"/></td>
	                        	</c:otherwise>
	                        </c:choose>
	                    </tr>
	                </c:forEach>
	                </tbody>
	            </table>
	        </div>
	        <div class="my_replies">
	            <table>
	                <thead>
	                <tr>
	                    <td>게시글제목&nbsp;</td>
	                    <td>댓글내용&nbsp;</td>
	                    <td>등록일</td>
	                </tr>
	                </thead>
	                <tbody>
	                <c:forEach var="myReply" items="${myReplies}" varStatus="status">
		                <tr>
		                	<!-- <td><input id="myReplyNo" name="myReplyNo" type="hidden" value="${myReply.no}"/></td> -->
		                    <td><a>${myReply.articleTitle}</a>&nbsp;</td>
		                    <td>${myReply.content }&nbsp;</td>
		                    <td><fmt:formatDate value="${myReply.regdate}" pattern="yyyy-MM-dd"/></td>
		                </tr>
	                </c:forEach>
	                </tbody>
	            </table>
	        </div>
	        <div class="change_pwd">
	            <div>
	                <div>현재 비밀번호</div>
	                <div><input id="curPwd" type="password" name="curPwd" value="" placeholder="현재 비밀번호 입력"/></div>
	                <div>새 비밀번호</div>
	                <div><input id="newPwd" type="password" name="newPwd" value="" placeholder="새 비밀번호 입력"/></div>
	                <div>새 비밀번호 확인</div>
	                <div><input id="newPwdCheck" type="password" name="newPwdCheck" value="" placeholder="새 비밀번호 확인"/></div>
	            </div>
	            <button id="changePwdBtn" type="button">변경</button>
	            <button id="deleteUserBtn" type="button">회원탈퇴</button>
	            <button type="button"><a href="/myPage">취소</a></button>
	        </div>
	    </div>
    </div>
</body>
<script src="js/jquery.js"></script>
<script>
	var pwdExp = /^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
	
    // 게시글 눌렀을 때
    $("#article").click(function () {
        $(".my_replies").hide();
        $(".change_pwd").hide();
        $(".my_articles").show();
        return false;
    });

    // 댓글 눌렀을 때
    $("#reply").click(function () {
        $(".my_articles").hide();
        $(".change_pwd").hide();
        $(".my_replies").show();
        return false;
    });

    // 비밀번호 변경 눌렀을 때
    $("#changePwd").click(function () {
        $(".my_articles").hide();
        $(".my_replies").hide();
        $(".change_pwd").show();
        return false;
    });
    
	// input 값이 있는지 없는지
	function checkInput(e) {
		if($("#curPwd").val()== "") {
			alert("현재비밀번호를 입력해주세요");
			return false;
		} 
		if($("#newPwd").val()== "") {
			alert("새 비밀번호를 입력해주세요");
			return false;
		} 
		if($("#newPwdCheck").val()== "") {
			alert("새 비밀번호 확인을 입력해주세요");
			return false;
		}
		return true;
	}
	
	// 현재비밀번호와 일치하는지
	var result;
    function curPwd() {
    	var curPwd = $("#curPwd").val();
    	result = "";
		$.ajax({
			url: "/ajax/curPwd",
			dataType: "JSON",
			data: {
				curPwd: curPwd
			},
			async: false,
			type: "POST",
			error: function () {
				alert("error");
			},
			success: function (data) {
				if(data.inputPwd != data.dbPwd) {
					alert("현재 비밀번호와 일치하지 않습니다");
					$("#curPwd").val("");
					result = "F";
					//return false;
				} else {
					if(!newPwd()) return false;
					if(!newPwdCheck()) return false;
					//return true;
					result = "S";
				}
			}
		});
		return result;
    }
    
    // 새로운 비밀번호가 영+숫+특 8~12인지
    function newPwd() {
		if(!pwdExp.test($("#newPwd").val())) {
			alert("영+숫+특 8~12로 입력해주세요");
			$("#newPwd").val("");
			return false;
		}
		return true;
	}
    
    // 변경한 비밀번호와 일치하는지
    function newPwdCheck() {
		if($("#newPwd").val() != $("#newPwdCheck").val()) {
			alert("입력한 비밀번호가 일치하지 않습니다");
			$("#newPwdCheck").val("");
			return false;
		}
		return true;
	}
    
    // 변경버튼 클릭 시 유효성검사 후 비밀번호 변경
    $("#changePwdBtn").click(function () {
    	if(!checkInput()) return false;
    	if(curPwd() != "S") {
    		return false;
    	}
    	var newPwd = $("#newPwd").val();
    	var loginUserNo = $("#loginUser").val();
    	
    	if(confirm("변경하시겠습니까?") == true) {
    		$.ajax({
        		url: "/ajax/myPage/updatePwd",
        		dataType: "json",
        		data: {
        			"newPwd" : newPwd,
        		},
        		type: "POST",
        		error: function () {
    				alert("ERROR");
    			},
    			success: function (data) {
    				if(data == 1) {
    					alert("변경됨");	
    				}
    			}
        	}); //ajax() end
    	} //if() end
    }); //changePwdBtn click() end
    
    // 내가 작성한 게시글 클릭 시 상세페이지로
    $("#articleInfo").on("click", "td a", function (e) {
    	e.preventDefault();
    	
    	var articleNo = $(this).prev().val();
    	var userNo = $("#loginUser").val();
    	
    	location.href = "/article/"+articleNo+"/"+userNo;
   	});
    
    // 회원탈퇴
    $("#deleteUserBtn").click(function () {
    	if(confirm("탈퇴하시겠습니까") == true) {
    		$.ajax({
    			url:"/ajax/myPage/withdrawUser",
    			dataType:"json",
    			type:"GET",
    			error: function (data) {
    				alert("ERROR")
    			},
    			success: function () {
    				alert("탈퇴되었습니다");
    			}
    		}); //ajax() end
    	}
	}); //deleteUserBtn end
</script>
</html>
