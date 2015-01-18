<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
      <div class="btn-group navbar-right">
        <button type="button" class="btn btn-default dropdown-toggle navbar-btn user-btn" data-toggle="dropdown" aria-expanded="false">
          <span class="glyphicon glyphicon-user" aria-hidden="true"></span> ${user.name} <span class="caret"></span>
        </button>
        <ul class="dropdown-menu" role="menu">
          <li><a>${user.email}</a></li>
          <li class="divider"></li>
          <li><a id="logout" href="#">Выйти <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span></a></li>
        </ul>
      </div>
    </div>
    </nav>

    <!-- Put cards on the board -->
    <!-- TODO: add table columns in the loop -->
    <table class="table-bordered kanban-table table">
      <thead>
        <tr>
          <th><div class="th-text">Запланированные</div>
            <button type="button" class="btn btn-default btn-sm btn-add" data-toggle="modal" data-target="#createCardModal">
              <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            </button></th>
          <th>В работе</th>
          <th>Выполненные</th>
          <th>Отклоненные</th>
        </tr>
      </thead>
      <tr>
        <td><c:forEach items='${user.board.getCardsByPhase("planned")}' var="card">
            <div class="panel panel-${card.priority}">
              <div class="panel-heading card-heading">
                <h3 class="panel-title cardName">${card.name}</h3>
                <input type="hidden" class="card-id" name="cardId" value="${card.id}">
                <div class="row">
                  <div class="col-xs-9 date">
                    <h6>${card.datef}</h6>
                  </div>
                  <div class="col-xs-3">
                    <h5 class="text-right">
                      <span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span>
                    </h5>
                  </div>
                </div>
              </div>
              <div class="panel-body collapsible" id="${card.name}-body">
                <p class="description">${card.description}</p>

                <form action="board" method="post">
                  <input type="hidden" name="thisCardId" value="${card.id}">
                  <div class="btn-group pull-right" role="group">
                    <div class="btn-group" role="group">
                      <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <c:choose>
                          <c:when test="${card.priority=='success'}">
                            <span class="glyphicon glyphicon-adjust" aria-hidden="true"></span>
                            <c:set var="firstPrior" value="fire" />
                            <c:set var="secondPrior" value="record" />
                            <c:set var="firstWord" value="Высокий" />
                            <c:set var="secondWord" value="Низкий" />
                          </c:when>
                          <c:when test="${card.priority=='danger'}">
                            <span class="glyphicon glyphicon-fire" aria-hidden="true"></span>
                            <c:set var="firstPrior" value="adjust" />
                            <c:set var="secondPrior" value="record" />
                            <c:set var="firstWord" value="Средний" />
                            <c:set var="secondWord" value="Низкий" />
                          </c:when>
                          <c:otherwise>
                            <span class="glyphicon glyphicon-record" aria-hidden="true"></span>
                            <c:set var="firstPrior" value="fire" />
                            <c:set var="secondPrior" value="adjust" />
                            <c:set var="firstWord" value="Высокий" />
                            <c:set var="secondWord" value="Средний" />
                          </c:otherwise>
                        </c:choose>
                      </button>
                      <ul class="dropdown-menu" role="menu">
                        <li><a class="${firstPrior}" href="#">${firstWord} 
                          <span class="glyphicon glyphicon-${firstPrior}" aria-hidden="true"></span></a>
                        </li>
                        <li><a class="${secondPrior}" href="#">${secondWord} 
                          <span class="glyphicon glyphicon-${secondPrior}" aria-hidden="true"></span></a>
                        </li>
                      </ul>
                    </div> <!-- end dropdown -->
                    <button type="button" class="btn btn-default btn-edit">
                      <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                    </button>
                    <button type="submit" class="btn btn-default" name="action" value="toRejected">
                      <span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span>
                    </button>
                    <button type="submit" class="btn btn-default" name="action" value="toInProgress">
                      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </c:forEach></td>
        <td><c:forEach items='${user.board.getCardsByPhase("inProgress")}' var="card">
            <div class="panel panel-${card.priority}">
              <div class="panel-heading card-heading">
                <h3 class="panel-title">${card.name}</h3>
                <div class="row">
                  <div class="col-xs-9 date">
                    <h6>${card.datef}</h6>
                  </div>
                  <div class="col-xs-3">
                    <h5 class="text-right">
                      <span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span>
                    </h5>
                  </div>
                </div>
              </div>
              <div class="panel-body collapsible" id="${card.name}-body">
                <p>${card.description}</p>
                <form action="board" method="post">
                  <input type="hidden" name="thisCardId" value="${card.id}">
                  <div class="btn-group pull-right" role="group">
                    <div class="btn-group" role="group">
                      <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <c:choose>
                          <c:when test="${card.priority=='success'}">
                            <span class="glyphicon glyphicon-adjust" aria-hidden="true"></span>
                            <c:set var="firstPrior" value="fire" />
                            <c:set var="secondPrior" value="record" />
                            <c:set var="firstWord" value="Высокий" />
                            <c:set var="secondWord" value="Низкий" />
                          </c:when>
                          <c:when test="${card.priority=='danger'}">
                            <span class="glyphicon glyphicon-fire" aria-hidden="true"></span>
                            <c:set var="firstPrior" value="adjust" />
                            <c:set var="secondPrior" value="record" />
                            <c:set var="firstWord" value="Средний" />
                            <c:set var="secondWord" value="Низкий" />
                          </c:when>
                          <c:otherwise>
                            <span class="glyphicon glyphicon-record" aria-hidden="true"></span>
                            <c:set var="firstPrior" value="fire" />
                            <c:set var="secondPrior" value="adjust" />
                            <c:set var="firstWord" value="Высокий" />
                            <c:set var="secondWord" value="Средний" />
                          </c:otherwise>
                        </c:choose>
                      </button>
                      <ul class="dropdown-menu" role="menu">
                        <li><a class="${firstPrior}" href="#">${firstWord} 
                          <span class="glyphicon glyphicon-${firstPrior}" aria-hidden="true"></span></a>
                         </li>
                        <li><a class="${secondPrior}" href="#">${secondWord} 
                          <span class="glyphicon glyphicon-${secondPrior}" aria-hidden="true"></span></a>
                        </li>
                      </ul>
                    </div>
                    <!-- end dropdown -->
                    <button type="submit" class="btn btn-default" name="action" value="toRejected">
                      <span class="glyphicon glyphicon-thumbs-down" aria-hidden="true"></span>
                    </button>
                    <button type="submit" class="btn btn-default" name="action" value="toExecuted">
                      <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </c:forEach></td>
        <td><c:forEach items='${user.board.getCardsByPhase("executed")}' var="card">
            <div class="panel panel-${card.priority}">
              <div class="panel-heading card-heading">
                <h3 class="panel-title">${card.name}</h3>
                <div class="row">
                  <div class="col-xs-9 date">
                    <h6>${card.datef}</h6>
                  </div>
                  <div class="col-xs-3">
                    <h5 class="text-right">
                      <span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span>
                    </h5>
                  </div>
                </div>
              </div>
              <div class="panel-body collapsible" id="${card.name}-body">
                <p>${card.description}</p>
                <form action="board" method="post">
                  <input type="hidden" name="thisCardId" value="${card.id}">
                  <div class="btn-group pull-right" role="group">
                    <button type="submit" class="btn btn-default" name="action" value="removeCard">
                      <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </c:forEach></td>
        <td><c:forEach items='${user.board.getCardsByPhase("rejected")}' var="card">
            <div class="panel panel-${card.priority}">
              <div class="panel-heading card-heading">
                <h3 class="panel-title">${card.name}</h3>
                <div class="row">
                  <div class="col-md-9 date">
                    <h6>${card.datef}</h6>
                  </div>
                  <div class="col-md-3">
                    <h5 class="text-right">
                      <span class="glyphicon glyphicon-collapse-down" aria-hidden="true"></span>
                    </h5>
                  </div>
                </div>
              </div>
              <div class="panel-body collapsible" id="${card.name}-body">
                <p>${card.description}</p>
                <form action="board" method="post">
                  <input type="hidden" name="thisCardId" value="${card.id}">
                  <div class="btn-group pull-right" role="group">
                    <button type="submit" class="btn btn-default" name="action" value="removeCard">
                      <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </c:forEach></td>
      </tr>
    </table>
    <!-- Modal for card creating -->
    <div class="modal fade" id="createCardModal" tabindex="-1" role="dialog" aria-labelledby="createCardLabel" aria-hidden="true">
      <div class="modal-dialog">
        <form role="form" id="createCard" method="get" action="board"> <!-- Form wrapping modal content and body -->
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
              <h4 class="modal-title" id="createCardModalLabel">Создать новую здачу</h4>
            </div>
            <div class="modal-body">
              <input type="hidden" name="action" value="createCard">
              <div class="form-group">
                <label for="inputCardName">Название</label> 
                <input type="text" class="form-control" id="inputCardName" placeholder="Название" name="cardName">
              </div>
              <div class="form-group">
                <label for="inputDescription">Описание</label>
                <textarea class="form-control" rows="3" id="inputDescription" placeholder="Описание" name="description"></textarea>
              </div>

              <label class="radio-inline text-danger"> 
                <input type="radio" name="priority" id="choosePriority1" value="danger"> 
                <span class="glyphicon glyphicon-fire" aria-hidden="true"></span> Высокий
              </label> 
              <label class="radio-inline text-success"> 
                 <input type="radio" name="priority" id="choosePriority2" value="success" checked> 
                 <span class="glyphicon glyphicon-adjust" aria-hidden="true"></span> Средний
              </label> 
              <label class="radio-inline text-muted"> 
                <input type="radio" name="priority" id="choosePriority3" value="default"> 
                <span class="glyphicon glyphicon-record" aria-hidden="true"></span> Низкий
              </label>
            </div>
            
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
              <button type="submit" class="btn btn-primary">Создать</button>
            </div>
          </div>
          <!-- modal content -->
        </form>
      </div>
      <!-- modal dialog end -->
    </div>
  </div>
  <!-- end container -->

  <!-- Modal for card editing -->
  <div class="modal fade" id="editCardModal" tabindex="-1" role="dialog" aria-labelledby="editCardLabel" aria-hidden="true">
    <div class="modal-dialog">
      <form role="form" id="editCard" method="get" action="board">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
            <h4 class="modal-title" id="editCardModalLabel">Редактировать данные</h4>
          </div>
          <div class="modal-body">
            <input type="hidden" name="action" value="updateInfo"> 
            <input type="hidden" id="cardId" name="thisCardId">
            <div class="form-group">
              <label for="inputNewCardName">Название</label> 
              <input type="text" class="form-control" id="inputNewCardName" name="newCardName">
            </div>
            <div class="form-group">
              <label for="inputNewDescription">Описание</label>
              <textarea class="form-control" rows="3" id="inputNewDescription" name="newDescription"></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
            <button type="submit" class="btn btn-primary">Обновить</button>
          </div>
        </div>
        <!-- modal content -->
      </form>
    </div>
    <!-- modal dialog end -->
  </div>

  <form id="form-logout" action="logout" method="post">
    <input type="hidden" name="action" value="logout">
  </form>


  <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
  <script src="bootstrap/js/bootstrap.min.js"></script>

  <script>
	    //todo add card to table via jquery

	    // open card description and collapse others
	    $('.card-heading').click(function() {
		if ($(this).next('.collapsible').css('display') != 'block') {
		    $('.active').slideUp('fast').removeClass('active');
		    $(this).find('span')
		    	.removeClass('.glyphicon-collapse-down')
		    	.addClass('.glyphicon-collapse-up');
		    $(this).next('.collapsible')
		    	.addClass('active')
		    	.slideDown('slow');
		} else {
		    $('.active').slideUp('fast').removeClass('active');
		}
	    });
		
	    // open modal window and set card data values to form
	    $('.btn-edit').click(function() {
		var cardId = $(this).closest('.panel').find('.card-id').val();
		var cardName = $(this).closest('.panel').find('.cardName').text();
		var cardDescription = $(this).closest('form').siblings('.description').text();

		var $form = $('#editCard');
		$form.find('#inputNewCardName').val(cardName);
		$form.find('#inputNewDescription').val(cardDescription);
		$form.find('#cardId').val(cardId);
		$('#editCardModal').modal('show');
	    });
		
	    // submit update card form with new priority parameter
	    var changePriority = function($obj, priority) {
		var input = $('<input>')
			.attr("type", "hidden")
			.attr("name", "newPriority").val(priority);
		var action = $('<input>')
			.attr("type", "hidden")
			.attr("name", "action").val("changePriority");

		var $form = $obj.closest('form');
		$form.append($(input)).append($(action));
		$form.submit();
	    }

	    $('.fire').click(function() {
		changePriority($(this), "danger");
	    });

	    $('.adjust').click(function() {
		changePriority($(this), "success");
	    });

	    $('.record').click(function() {
		changePriority($(this), "default");
	    });

	    $('#logout').click(function() {
		$('#form-logout').submit();
	    });
	</script>
</body>
</html>