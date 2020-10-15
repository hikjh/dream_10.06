<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>ERROR PAGE</title>
</head>
<body>
	<div>${errMsg}</div>
	<spring:message code="errSign"></spring:message>
	<div>에러코드 : ${errCode}</div>
	<a href="/">로그인 화면으로</a>
</body>
</html>