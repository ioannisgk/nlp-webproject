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
                            Home Page
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-6">

                                  <h1>Settings</h1>
                                  </br>
                                  <div style="margin-left:15%; margin-right: 15%; width:300px;">
                                  <form role="form" action="chat" method="POST">
                                    	<div class="form-group">
	                                		<select id="select-mode" class="form-control" name="selection">
												<option value="untrained">Untrained Neural Network</option>
												<option value="mlp-01layer">MLP Neural Network with 1 hidden layer</option>
												<option value="mlp-02layer">MLP Neural Network with 2 hidden layers</option>
	                                			<option value="mlp-03layer">MLP Neural Network with 3 hidden layers</option>
	                                	  	</select>
                                      	</div>
                                		</br>
                                		
                                		<div id="mlp-01layer">
                                			Number of neurons for hidden layer 1:
                                			</br></br><input class="form-control" name="mlp-01layer-layer1" type="number" min="1" max="1000">
                                			</br>Number of training iterations:
                                			</br></br><input class="form-control" name="mlp-01layer-iterations" type="number" min="1" max="10000">
                                		</div>
                                		
                                		<div id="mlp-02layer">
                                			Number of neurons for hidden layer 1:
                                			</br></br><input class="form-control" name="mlp-02layer-layer1" type="number" min="1" max="1000">
                                			</br>Number of neurons for hidden layer 2:
                                			</br></br><input class="form-control" name="mlp-02layer-layer2" type="number" min="1" max="1000">
                                			</br>Number of training iterations:
                                			</br></br><input class="form-control" name="mlp-02layer-iterations" type="number" min="1" max="10000">
                                		</div>
                                		
                                		<div id="mlp-03layer">
                                			Number of neurons for hidden layer 1:
                                			</br></br><input class="form-control" name="mlp-03layer-layer1" type="number" min="1" max="1000">
                                			</br>Number of neurons for hidden layer 2:
                                			</br></br><input class="form-control" name="mlp-03layer-layer2" type="number" min="1" max="1000">
                                			</br>Number of neurons for hidden layer 3:
                                			</br></br><input class="form-control" name="mlp-03layer-layer3" type="number" min="1" max="1000">
                                			</br>Number of training iterations:
                                			</br></br><input class="form-control" name="mlp-03layer-iterations" type="number" min="1" max="10000">
                                		</div>
                                		
                                		</br></br>
                                		<input type="submit" style="width:300px" class="btn btn-outline btn-primary btn-lg" value="Chat Now">
                                		</br></br>
                                		<input id="execute-ga" style="width:300px" class="btn btn-outline btn-primary btn-lg" value="Execute GA">
                                		
                                		</br></br>
                                		<div id="results-ga" ></div>
                                		
                                  </form>
                                	
                                  </div>
                                  </br></br>
                                  <div class="alert alert-warning">
                                        Note: Loading the chat page might take 10 seconds due to Network training.
                                  </div>

      							</div>
                                <!-- /.col-lg-6 (nested) -->
                                <div class="col-lg-6">

                                    <h1>Chatbot Information</h1>
                                    </br>
                                    <div class="well">
                                        <h4>About the chatbot</h4>
                                        <p>This web application is a chatbot prototype used for demonstrating how <strong>Natural Language Processing</strong> and <strong>Neural Networks</strong> can work together.</p>
                                        <p>The system uses the <strong>Stanford CoreNLP</strong> library so it can recognize words, POS (Part Of Speech) and NER (Name Entity Recognition) tags. The tags are used to store current important variables, such as person, location, date, duration and money. When those "slots" are filled from the user input message, and a business rule is true, the chatbot reaches its objective and presents the booking data.</p>
                                        <p>The business rules are important for leading the conversation to one direction or another. We use <strong>Neuroph</strong>, a Java Neural Network framework to create, train and test a MultiLayer Perceptron Neural Network which classifies the user message into a set of categories. If the message is assigned to a specific category, and the business rule allows it, we have control of where the conversation is heading and when to apply the NER tags knowledge mentioned previously.</p>
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
			
			$('#mlp-01layer').hide();
			$('#mlp-02layer').hide();
			$('#mlp-03layer').hide();
			
			$('#select-mode').on('change', function() {
				
				$('#mlp-01layer').hide();
				$('#mlp-02layer').hide();
				$('#mlp-03layer').hide();
			
				var mode = $('#select-mode').val();
				
				$('#' + mode).show();
			});
			
			$('#execute-ga').click(function() {
				event.preventDefault();
				$.ajax({
					type:'POST',
					url:'${pageContext.request.contextPath}/home/execute-ga',
					success: function(result) {
						
						$('#results-ga').html('<strong>GA optimization results: </strong>' + result);
						$('#select-mode').focus();
					}
				});
			});
			
		});
	</script>

</body>

</html>
