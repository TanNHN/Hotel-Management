<%-- 
    Document   : login
    Created on : Sep 14, 2021, 12:50:00 AM
    Author     : PC
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <c:if test="${sessionScope.USER != null}">
        <c:redirect url="MainController?action=Search"></c:redirect>
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page</title>
    <a href="MainController?action=Search Hotel" style="margin-left: 40%">Main page</a>

</head>
<body>
    <form id="loginForm" action="MainController" method="post">
        UserId: <input type="text" name="txtUserId" value="${param.txtUserId}"/><br>
        Password: <input type="password" name="txtPassword"/><br>
        <c:if test="${requestScope.LOGIN_FAIL != null}">
            <p style="color: red">${requestScope.LOGIN_FAIL}</p>
        </c:if>
        <c:if test="${requestScope.ACCOUNT_VERIFIED_MESSAGE != null}">
            <p style="color: red">${requestScope.ACCOUNT_VERIFIED_MESSAGE}</p>
        </c:if>
        <input type="submit" name="action" value="Login">
    </form>
    or <a href="register.html">Register</a>
</body>
</html>
