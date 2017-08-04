<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %> 
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:message code="spring.welcome"/></title>
<script>
var ctx = "/SpringMVCTest";
</script>
</head>
<body>
<s:message code="spring.welcome"/>
${demoStr}
欢迎
<br/>
<form action="/SpringMVCTest/login" method="post" enctype="multipart/form-data">
name:<input type="text" name="name">
password:<input type="text" name="password">
<input type="submit" value="submit">
</form>
</body>
</html>