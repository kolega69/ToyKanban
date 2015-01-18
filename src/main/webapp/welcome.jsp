<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Удачная регистрация</title>

<!-- Bootstrap core CSS -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="bootstrap/css/custom.css" rel="stylesheet">

<!-- Fonts -->
<link href='http://fonts.googleapis.com/css?family=Exo+2:400,200,600&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
</head>
<body>
  <div class="container">
    <nav class="navbar  navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <a class="navbar-brand" href="#">ToyKanban</a>
      </div>
    </div>
    </nav>
    <div class="jumbotron">
      <h3>Поздравляем с успешной регистрацией!</h3>
      <p>Имя: ${user.name}</p>
      <p>Email: ${user.email}</p>
      <p>Дата регистрации: ${user.datef}</p>

      <p>
        <a href="login.html" class="btn btn-default btn-lg">Войти</a>
      </p>
    </div>
  </div>
</body>
</html>