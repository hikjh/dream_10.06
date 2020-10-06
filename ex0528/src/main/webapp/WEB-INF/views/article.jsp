<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.mycompany.myapp.vo.User"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Board</title>
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/article.css">
</head>
<body>
<input id="loginUser" type="hidden" name="loginUser" value="${loginUser.no}"/>	
    <div id="boardForm">
        <h1>게시판</h1>
        <form action="/session" method="get">
        	<button id="logout" type="submit">로그아웃</button>
        	<button id="myPageBtn" type="button"><a href="/myPage">마이페이지</a></button>
        </form>
        <div id="topBox">
            <div id="searchBox">
               	<select id="searchType" >
               		<option value="T" selected="selected">제목</option>
               		<option value="W" >작성자</option>
               	</select>
                <input id="search" name="search" placeholder="게시글을 검색해보세요">
                <button id="searchBtn" type="button"><span>검색</span></button>
            </div>
            <button id="writeBtn">글쓰기</button>
        </div>
        <div id="listBox">
        	<div id="articleListHead">
	       		<span id="" class="head_no">번호</span>
	   			<span id="" class="head_title">제목</span>
	   			<span id="" class="head_writer">작성자</span>
	   			<span id="" class="head_regdate">등록일</span>
	   			<span id="" class="head_view">조회수</span>
	       	</div>
            <ul id="articleList"></ul>
        </div>
        <div id="paginate"></div>
        <form id="articleForm" action="/articleRegister" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
            <div id="titleWrap">제목 <input id="title" name="title" placeholder="제목 작성" value=""></div>
            <div id="writer">작성자 ${loginUser.userId}</div>
            <div id="regdate"></div>
            <textarea id="content"  name="content" placeholder="내용 작성"></textarea>
            <span>
            	<input id="file" name="file" type="file" multiple="multiple" value="1"/>
            </span>
            <button id="registerBtn" class="btn" type="button">등록</button>
            <button id="cancelBtn" class="btn" type="button">취소</button>
        </form>
    </div>
</body>
<script type="text/template" id="articleListTmp">
<@if(articles.length==0) {@>
	<div class="no_review">게시글이 없어요</div>
<@} else {@>
	<@_.each(articles, function(article, index){@>
		<li>
			<span id="articleNum" class="article_no"><@=article.num@></span>
			<input id="articleNo" class="article_no" type="hidden" name="articleNo" value="<@=article.no@>">
   			<a href=""><span id="articleTitle" class="article_title"><@=article.title@></span></a>
   			<span id="articleWriter" class="writer"><@=article.writer@></span>
   			<span id="articleRegdate" class="regdate"><@=moment(article.regdate).format("YYYY-MM-DD")@></span>
   			<span id="articleViews"><@=(article.view)@></span>
		</li>
	<@})@>
<@}@>
<@=paginate@>
</script>
<script src="/js/jquery.js"></script>
<script src="/js/underscore-min.js"></script>
<script src="/js/moment-with-locales.js"></script>
<script>
	_.templateSettings = {
        interpolate: /\<\@\=(.+?)\@\>/gim,
        evaluate: /\<\@([\s\S]+?)\@\>/gim,
        escape: /\<\@\-(.+?)\@\>/gim
    };
	var articleTmp = _.template($("#articleListTmp").html());
	
	// 게시글 리스트 
	var page = 1;
	function getArticleList() {
		
		var searchType = $("#searchType option:selected").val();
		var search = $("#search").val();
		var obj = {"search" : search,
				"searchType" : searchType};
		
		//console.log(searchType);
		//console.log(search);
		
		$.ajax({
			url: "/ajax/article/page/"+page,
			dataType: "json",
			data: JSON.stringify(obj),
			type: "post",
			contentType: "application/json",
			error: function () {
				alert("ERROR");
			},
			success: function (data) {
				console.log(data.articles);
				//console.log(data.paginate);
				$("#articleList").empty().prepend(articleTmp({"articles": data.articles, "paginate": data.paginate}));
			}
		});
	} //getArticleList() end
	
	//초기화면에 리스트 불러오기 
	getArticleList(); 
	
	//페이지 클릭
	$("#articleList").on("click",".paginate a",function (e) {
		e.preventDefault();
		page = this.dataset.no;
		getArticleList();
	});
	
	//글쓰기 popup
    $("#writeBtn").click(function () {
        $("#articleForm").css("display","block");
    });
	
  	//등록 버튼 클릭
    $("#registerBtn").click(function () {
    	var title = $("#title").val();
        var content = $("#content").val();

        if(title == "") {
            alert("제목을 입력해주세요");
            return false;
        } else if (content == "") {
            alert("내용을 입력해주세요");
            return false;
        } else if(content.length > 500) {
        	alert("500자 이하로 쓰세요");
        	return false;
        }
        $("#articleForm").submit();
    });

  	//취소 버튼 클릭
    $("#cancelBtn").click(function () {
    	$("#title").val("");
    	$("#content").val("");
    	$("#articleForm").css("display","none");
    });
  	
  	//게시글 상세페이지로~~가즈아~
    $("#articleList").on("click","li a",function (e) {
		e.preventDefault();
		var no = $(this).prev().val();
		var userNum = $("#loginUser").val();
		//var type = "S";
		location.href = "/article/"+no+"/"+userNum;
    });
    
    //검색 버튼 클릭
	$("#searchBtn").click(function () {
		page = 1;
		getArticleList();
	});
    
</script>
</html>
