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
	<div>�����ڵ� : ${errCode}</div>
	<a href="/">�α��� ȭ������</a>
</body>
</html>