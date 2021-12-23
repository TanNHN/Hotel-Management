<%-- 
    Document   : head
    Created on : Sep 29, 2021, 6:08:22 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hotel Booking</title>
        <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/additional-methods.js"></script>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <c:if test="${sessionScope.USER != null}" var="authenticated">
            Welcome, ${sessionScope.USER.name}
        <a href="MainController?action=Log out" style="float: right">Log out</a>
        <a href="MainController?action=Search booking" style="margin-right: 20px">My order</a>

    </c:if>
    <c:if test="${!authenticated}">
        <a href="login.html">Login</a> or <a href="register.html">Register</a>
    </c:if>
    <a href="MainController?action=Search Hotel" style="margin-left: 40%">Main page</a>
    <a href="cart.jsp" style="float: right; margin-right: 30px">My cart</a>

    <hr>
</head>

