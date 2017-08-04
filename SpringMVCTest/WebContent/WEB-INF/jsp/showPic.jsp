<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %> 
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:message code="spring.welcome"/></title>
</head>
<body>
<img src="<%=request.getContextPath()%>/file/${allName}">
</body>
</html>