<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ToyBoard</title>

<!-- Bootstrap core CSS -->
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="bootstrap/css/custom.css" rel="stylesheet">

<script src="../../assets/js/ie-emulation-modes-warning.js"></script>
</head>
<body>
	<h1>Your name is: ${user.name}</h1>
	
	<div class="container">
		<form class="form-horizontal" role="form" id="createCard" method="get" action="board">
			<input type="hidden" name="action" value="createCard">
			<div class="form-group">
				<label for="inputCardName" class="col-sm-2 control-label">Название</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="inputCardName"
						placeholder="Название" name="cardName">
				</div>
			</div>
			<div class="form-group">
				<label for="inputDescription" class="col-sm-2 control-label">Описание</label>
				<div class="col-sm-10">
					<textarea class="form-control" rows="3" id="inputDescription"
						placeholder="Описание" name="description"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="choosePriority" class="col-sm-2 control-label">Приоритет</label>
				<div class="col-sm-10">
					<select class="form-control" id="choosePriority" name="priority">
						<option value="danger">Высокий</option>
						<option selected value="success">Средний</option>
						<option value="default">Низкий</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-default">Создать</button>
				</div>
			</div>
		</form>

		<div class="row">
			<div class="col-md-3">
				<p>Запланированные</p>
				<c:forEach items='${user.board.getCardsByPhase("planned")}' var="card">
					<div class="panel panel-${card.priority}">
						<div class="panel-heading card-heading">
							<h3 class="panel-title">${card.name}</h3>
							<div class="row">
								<div class="col-md-10 col-xs-10"><h6>${card.datef}</h6></div>
								<div class="col-md-2 col-xs-2"><h5><span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span></h5></div>
							</div>
						</div>	
						<div class="panel-body collapsible" id="${card.name}-body">
							<p>${card.description}</p>
							<div class="btn-group" role="group" aria-label="plan-btns" style="float:right;">
						      
						      <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button>
						      <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>
						      <form action="board" method="post">
						      <input type="hidden" name="thisCard" value="${card.name}">
						      <input type="hidden" name="action" value="toInProgress">
						      <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span></button>
						      </form>
						  </div>
						</div>
					</div>
				</c:forEach>
			</div>
			<div class="col-md-3">
				<p>В процессе</p>
				<c:forEach items='${user.board.getCardsByPhase("inProgress")}' var="card">
					<div class="panel panel-${card.priority}">
						<div class="panel-heading card-heading">
							<h3 class="panel-title">${card.name}</h3>
							<div class="row">
								<div class="col-md-10 col-xs-10"><h6>${card.datef}</h6></div>
								<div class="col-md-2 col-xs-2"><h5><span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span></h5></div>
							</div>
						</div>	
						<div class="panel-body collapsible" id="${card.name}-body">
							<p>${card.description}</p>
							<div class="btn-group" role="group" aria-label="plan-btns" style="float:right;">
						      
						      <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>
						      <form action="board" method="post">
						      <input type="hidden" name="thisCard" value="${card.name}">
						      <input type="hidden" name="action" value="toInProgress">
						      <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span></button>
						      </form>
						  </div>
						</div>
					</div>
				</c:forEach>
			</div>
			<div class="col-md-3">
				<p>Выполненные</p>
			</div>
			<div class="col-md-3">
				<p>Отклоненные</p>
			</div>
		</div>
	</div>

	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>

	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>

	<script>
		//add card to table via jquery
		//	$('#createCard').submit(function(e){
		//		var $form = $(this);
		//		var url = $form.attr("action");
		//		var data = $form.serialize();
		//		$post(url, data).done(function(data) {
		//			
		//		});
		//		
		//		e.preventDefault();
		//	});
		
		// open card description and collapse others
		$('.card-heading').click(function() {
			if($(this).next('.collapsible').css('display') != 'block'){
				$('.active').slideUp('fast').removeClass('active');
				$(this).find('span')
					.removeClass('.glyphicon-collapse-down')
					.addClass('.glyphicon-collapse-up');
				$(this).next('.collapsible').addClass('active').slideDown('slow');
		    } else {
				$('.active').slideUp('fast').removeClass('active');
			}
		});
	</script>
</body>
</html>