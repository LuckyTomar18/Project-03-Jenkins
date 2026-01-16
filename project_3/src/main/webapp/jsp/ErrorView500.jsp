<%@page import="in.co.rays.project_3.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isErrorPage="true"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Server Error</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.ERROR_CTL%>/img/logo.png">

<style>
body {
	font-family: Arial, Helvetica, sans-serif;
	background: #f4f6f9;
	margin: 0;
	padding: 0;
}

.error-container {
	width: 60%;
	margin: 100px auto;
	background: #ffffff;
	padding: 30px;
	border-radius: 8px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	text-align: center;
}

.error-code {
	font-size: 48px;
	color: #e74c3c;
	margin-bottom: 10px;
}

.error-title {
	font-size: 22px;
	color: #333;
	margin-bottom: 20px;
}

.error-message {
	background: #fbeaea;
	color: #c0392b;
	padding: 15px;
	border-radius: 5px;
	margin-bottom: 15px;
	word-wrap: break-word;
}

.exception-type {
	font-size: 30px;
	color: #555;
}

.back-btn {
	display: inline-block;
	margin-top: 20px;
	padding: 10px 20px;
	background: #3498db;
	color: #fff;
	text-decoration: none;
	border-radius: 5px;
}

.back-btn:hover {
	background: #2980b9;
}
</style>

</head>

<body>

	<div class="error-container">

		<%
			if (exception != null) {
		%>
		<div class="error-code">500</div>
		<div class="error-title">Internal Server Error</div>

		<div class="error-message">
			<b>Error Message:</b><br>
			<%=exception.getMessage()%>
		</div>

		<div class="exception-type">
			<b>Exception Type:</b>
			<span style="color: red"><%=exception.getClass().getName()%></span>
		</div>

		<%
			} else {
		%>
		<div class="error-code">Error</div>
		<div class="error-title">Unknown Server Error</div>
		<%
			}
		%>

		<a href="<%=ORSView.WELCOME_CTL%>" class="back-btn">Go to Home</a>

	</div>

</body>
</html>
