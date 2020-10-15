<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.mycompany.myapp.vo.User"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>상세</title>
    <link rel="stylesheet" href="css/articleDetail.css">
</head>
<body>
	<c:choose>
		<c:when test="${loginUser.userId == article.writer}">
			<form id="articleDetailForm" action="/article" method="post">
				<input id="articleNo" type="hidden" value="${article.no}">
		        <div id="titleWrap">제목 <input id="title" name="title" value="${article.title}"></div>
		        <span id="writer">작성자: ${article.writer}</span>
		        <span><button id="updateBtn" class="btn" type="button">수정</button></span>
	        	<span><button id="deleteBtn" class="btn" type="button">삭제</button></span>
        		<div id="regdate">작성일: <fmt:formatDate value="${article.regdate}" pattern="yyyy-MM-dd"/></div>
        		<c:if test="${article.modifyDate != null}">
        			<div id="regdate">수정일: <fmt:formatDate value="${article.modifyDate}" pattern="yyyy-MM-dd"/></div>
        		</c:if>
		        <textarea id="content" name="content">${article.content}</textarea>
	        	<!-- <input id="file" name="file" value="${file.originalFileName}"/> -->
		    </form>
	    </c:when>
	    <c:otherwise>
	    	<form id="articleDetailForm" action="/article" method="post">
	    		<input id="articleNo" type="hidden" value="${article.no}">
		        <div id="titleWrap">제목 <input id="title" name="title" placeholder="제목 작성" value="${article.title}" readonly="readonly"></div>
		        <div id="writer">작성자: ${article.writer}</div>
		        <div id="regdate">작성일: <fmt:formatDate value="${article.regdate}" pattern="yyyy-MM-dd"/></div>
		        <textarea id="content"  name="content" placeholder="내용 작성" readonly="readonly">${article.content}</textarea>
		    </form>
	    </c:otherwise>
    </c:choose>
    <ul id="fileDownload">
 		<c:forEach var="file" items="${files}" varStatus="status">
			<li value="${file.originalFileName}">
				${file.originalFileName}
				<input type="hidden" value="${file.no}">
			</li>
		</c:forEach>
 	</ul>
    <button id="cancelBtn" class="btn" type="button">목록으로</button>
    <button id="replyBtn" class="btn" type="button">댓글등록</button>
    <span>조회수: ${article.view} </span>
    <ul id="replyList">
		        	
    </ul>
   	<form id="replyForm" action="" method="">
   		<fieldset>
   			<legend>댓~글</legend>
        	<div id="replyWriter">작성자: ${loginUser.userId}</div>
        	<textarea id="replyContent"  name="content" placeholder="내용 작성"></textarea>
        	<button id="replyRegisterBtn" class="btn" type="button">등록</button>
        	<button id="replyCancelBtn" class="btn" type="button">취소</button>
        </fieldset>
    </form>	
</body>
<script src="/js/jquery.js"></script>
<script src="/js/moment-with-locales.js"></script>
<script>
	// 수정버튼 클릭 시
	$("#updateBtn").click(function () {
		var articleNo = $("#articleNo").val();
		var title = $("#title").val();
		var content = $("#content").val();
		$.ajax({
			url: "/ajax/article/"+articleNo+"/"+title+"/"+content,
			dataType: "json",
			data: {articleNo : articleNo,
					title : title,
					content : content,
					},
			type: "put",
			erorr: function () {
				alert("ERROR");
			},
			success: function (data) {
				
				console.log("여기 : "+data.type);
				
				if(confirm("수정하시겠습니까?") == true) {
					//$("#modifyDate").val("수정일: "+moment(data.regdate).format("YYYY-MM-DD"));
					//location.href = "/article/"+articleNo+"/"+data.type;
					$("#articleDetailForm").submit();
				} else {
					return false;
				}
			}
		});//ajax() end
	});//updateBtn click() end
	
	// 삭제버튼 클릭 시
	$("#deleteBtn").click(function () {
		var articleNo = $("#articleNo").val();
		//console.log(articleNo);
		$.ajax({
			url: "/ajax/article/"+articleNo,
			dataType: "json",
			data: {articleNo : articleNo},
			type: "delete",
			erorr: function () {
				alert("ERROR");
			},
			success: function (data) {
				if(confirm("삭제하시겠습니까?")) {
					$("#articleDetailForm").submit();
				} else {
					return false;
				}
			}
		});//ajax() end
	});//deleteBtn click() end
	
	// 목록으로 버튼 클릭 시
	$("#cancelBtn").click(function () {
		$("#articleDetailForm").submit();
	});//cancelBtn click() end
	
	// 댓글이나쓰자 버튼 클릭 시
	$("#replyBtn").click(function () {
		$("#replyForm").css("display","block");
	});//replyBtn click() end
	
	// 댓글 등록 버튼 클릭 시
	$("#replyRegisterBtn").click(function () {
		var articleNo = $("#articleNo").val();
		var content = $("#replyContent").val();
		var writer = $("#replyWriter").text().substr(5);
		if(content.length > 50) {
			alert("댓글을 50자 이내로 작성해주세요");
			return false;
		}
		$.ajax({
			url: "/ajax/article/"+articleNo+"/reply",
			dataType: "json",
			data: {
				"articleNo": articleNo,
				"content": content,
				"writer": writer
			},
			type: "POST",
			error: function () {
				alert("ERROR");
			},
			success: function (data) {
				alert("댓글을 등록하였습니다");
				$("#replyForm").css("display","none");
				$("#replyList").empty();
				$("#replyContent").val("");
				getReplyList();	
			}
		});
	});
	
	//댓글 리스트
	var page = 1;
	function getReplyList(){
		
		var articleNo = $("#articleNo").val();
		$.ajax({
			url: "/ajax/article/"+articleNo+"/reply/page/"+page,
			dataType: "json",
			data: {
				"articleNo" : articleNo
			},
			type: "get",
			error: function () {
				alert("ERROR");
			},
			success: function (data) {
				//console.log(data);
				for(i = 0; i < data.total; i++) {
					$("<li>").text(data.replies[i].writer+": "+data.replies[i].content).appendTo("#replyList");
 				}
			}
		});
	}
	getReplyList();
	
	// 댓글 등록 취소 버튼 클릭 시
	$("#replyCancelBtn").click(function () {
		$("#replyForm").css("display","none");
	});
	
	$("#fileDownload").on("click", "li", function () {
		var fileNo = $(this).children().val();
		location.href = "/file/"+fileNo;
	});
</script>
</html>