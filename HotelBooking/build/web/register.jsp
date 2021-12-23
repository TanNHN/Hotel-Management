<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <c:if test="${sessionScope.USER != null}">
        <c:redirect url="MainController?action=Search"></c:redirect>
    </c:if>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register page</title>


    </head>
    <body>
        <a href="login.html">Login</a> 

        <a href="MainController?action=Search Hotel" style="margin-left: 40%">Main page</a>
        <br>
        <script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.2.1.min.js"></script>
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js" type="text/javascript"></script>
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/additional-methods.js"></script>
        <script type="text/javascript">
            $(function () {
                $("#registerForm").validate({
                    rules: {
                        txtUserId: {
                            required: true,
                            email: true,
                        },
                        txtName: {
                            required: true,
                            rangelength: [3, 500]
                        },
                        txtPhone: {
                            required: true,
                            phoneUK: true
                        },
                        taAddress: {
                            required: true,
                            rangelength: [10, 500]
                        },
                        txtPassword: {
                            required: true,
                            rangelength: [3, 500]
                        },
                        txtConfirmPassword: {
                            required: true,
                            equalTo: "#txtPassword"
                        }
                    }
                });
            });
        </script>
        <style type="text/css">
            #registerForm label.error {
                margin-left: 20px;
                color: red;
            }
        </style>
        <form id="registerForm" action="MainController" method="post">
            ID:<input type="text" name="txtUserId" value="${param.txtUserId}"><c:if test="${requestScope.ERROR != null}">
                <p style="color: red">${requestScope.ERROR}</p>
            </c:if>
            <br>

            Name:<input type="text" name="txtName" value="${param.txtName}"><br>
            Phone:<input type="text" name="txtPhone" value="${param.txtPhone}"><br>

            Address:<textarea name="taAddress">${param.taAddress}</textarea><br>
            Password: <input type="password" name="txtPassword" id="txtPassword" /><br>
            Confirm password: <input type="password" name="txtConfirmPassword"/><br>
            <input type="submit" name="action" value="Register">

        </form>

        <p style="color: red">${requestScope.ID_EXIST_MESSAGE}</p>
    </body>
</html>
