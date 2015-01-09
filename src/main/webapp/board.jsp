<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ToyBoard</title>
</head>
<body>
	<h1>Your name is: ${user.name}</h1>
	<c:forEach items="${user.cards.keySet()}" var="phase">
		<c:forEach items="${user.cards.get(phase)}" var="card">
			<p>Card name is <em>${card.name}</em></p>
			<p>Card description <em>is ${card.description}</em></p>
			<p>Card phase is <em>${card.phase}</em>
		</c:forEach>
	</c:forEach>
	
</body>
</html>