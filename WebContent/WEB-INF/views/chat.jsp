<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>NLP WebProject</title>

    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${pageContext.request.contextPath}/resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/resources/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${pageContext.request.contextPath}/resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/home/main">NLP WebProject</a>
            </div>
            <!-- /.navbar-header -->


            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="sidebar-search">
                            <div class="input-group custom-search-form">
                                <input type="text" class="form-control" placeholder="Search...">
                                <span class="input-group-btn">
                                <button class="btn btn-default" type="button">
                                    <i class="fa fa-search"></i>
                                </button>
                            </span>
                            </div>
                            <!-- /input-group -->
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/home/main"><i></i>Home Page</a>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Travel Assistant Chatbot</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Chat Page
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-6">

                                  <h1>Chat Area</h1>
                                  </br>
                                  <div style="margin-top:10px; margin-left:5%; margin-right: 5%;">
                                                    
                                  <div id="wrapper">
									    <div class="panel panel-default">
					                      <div class="panel-body">
					                          <p><div id="chatbox">${message}</div></p>
					                      </div>
					                	</div>
									    
									    <form role="form" name="message" action="">
                                		    <input class="form-control" name="user-message" id="user-message" type="text">
                                		    </br>
                                		    <input name="submit-message" type="submit" id="submit-message" style="width:300px" class="btn btn-outline btn-primary" value="Send">
                                		</form>
							      </div>
                                  
                                  </div>
                                  </br></br>
                                  <div class="alert alert-warning">
                                        Note: Sending the first message might take 10 seconds due to NLP initialization.
                                  </div>

      							</div>
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">

                                    <h1>Chat Information</h1>
                                    </br>
                                    <div class="panel panel-default">
					                      <div class="panel-heading">
					                          Info Panel
					                      </div>
					                      <div class="panel-body">
					                      
					                      	  <strong>Overall Probability</strong> in test dataset: <i>${overallProbability} %</i>
					                      	  </br>
					                      	  <strong>Correct Predictions</strong> in test dataset: <i>${correctPredictions} %</i>
					                      	  </br></br>
					                      	  
					                      	  <div id="category" ></div>
					                      	  <div id="probability" ></div>
					                      	  </br>
					                      	  
					                          <div class="table-responsive">
					                              <table id="info-table" class="table table-hover">
					                                  <thead>
					                                      <tr>
					                                          <th>Word</th>
					                                          <th>POS</th>
					                                          <th>NER</th>
					                                      </tr>
					                                  </thead>
					                                  <tbody>
					                                  </tbody>
					                              </table>
					                          </div>
					                          <!-- /.table-responsive -->
					                      </div>
					                      <!-- /.panel-body -->    
					                </div>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                            </div>
                            <!-- /.row (nested) -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            </br></br>
            <div style="text-align: center">Copyright 2018 Ioannis Gkourtzounis</div>
            </br></br>
        </div>
        <!-- /#page-wrapper -->
      

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/resources/vendor/jquery/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${pageContext.request.contextPath}/resources/vendor/bootstrap/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="${pageContext.request.contextPath}/resources/vendor/metisMenu/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="${pageContext.request.contextPath}/resources/dist/js/sb-admin-2.js"></script>
    
    <script type="text/javascript">

		$(document).ready(function(){
			
			$('#submit-message').click(function() {
				event.preventDefault();
				var message = $("#user-message").val();
				$.ajax({
					type:'POST',
					url:'${pageContext.request.contextPath}/home/start-chat',
					data: {message:message},
					success: function(result) {
						
						var messageInfo = eval('[' + result + ']');
						
						$('#category').html('<strong>Assigned Category</strong>: ' + '<i>' + messageInfo[0][0][4] + '</i>');
						$('#probability').html('<strong>Probability</strong>: ' + '<i>' + messageInfo[0][0][5] + ' %</i>');
						
						$('#chatbox').append('</br><strong>User</strong>: ' + messageInfo[0][0][0]);
						
						$('#info-table').html(
								'<thead><tr><th>Word</th>' +
				                '<th>POS</th><th>NER</th>' +
				                '</tr></thead><tbody></tbody>');
						
						for (var i = 0; i < messageInfo[0].length; i++) {
							
							$('#info-table').append('<tr><td>' +
									messageInfo[0][i][1] + '</td><td>' +
									messageInfo[0][i][2] + '</td><td>' +
									messageInfo[0][i][3] + '</td></tr>');
						}
						
						$('#chatbox').append('</br><strong>Bot</strong>: ' + messageInfo[0][0][6]);
						
						$('#user-message').val('');
						$('#user-message').focus();
					}
				});
			});
			
		});
		
	</script>

</body>

</html>
