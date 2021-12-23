<%@include file="head.jsp"%>
<body>
    <h1>HOTEL ${param.hotelName}</h1>
    Check in: ${requestScope.CHECK_IN}<br>
    Check out:${requestScope.CHECK_OUT}<br>
    <form action="MainController" method="post">

        <table border="1" style="margin-top: 30px">
            <thead>
                <tr>
                    <th>Room type</th>
                    <th>Price</th>
                    <th>Room Available</th>
                    <th>Quantity</th>
                </tr>
            </thead>
            <tbody>

                <c:forEach items="${requestScope.ROOM_TYPE_LIST}" var="room_type">
                    <tr>
                        <td>${room_type.name}</td>
                        <td>${room_type.roomTypePrice.price}</td>
                        <td>${room_type.totalRoom}</td>
                        <td><input required="true" value="0" name="txtQuantity${room_type.id}" type="number" min="0" max="${room_type.totalRoom}"></td>
                        <!--<td><a href="MainController?action=Add to cart&roomId=${room.id}&hotelId=${room.hotel.id}&txtCheckIn=${param.txtCheckIn}&txtCheckOut=${param.txtCheckOut}">Add to cart</a></td>-->
                    </tr>

                </c:forEach>
            </tbody>
        </table>
        <input type="hidden" value="${requestScope.CHECK_IN}" name="txtCheckIn">
        <input type="hidden" value="${requestScope.CHECK_OUT}" name="txtCheckOut">
        <input type="hidden" value="${param.hotelName}" name="hotelName">

        <input type="hidden" value="${requestScope.HOTEL_ID}" name="hotelId">
        <input type="submit" name="action" value="Add to cart">
    </form>
    <p style="color: red">${requestScope.ADD_TO_CART_SUCCESS_MESSAGE}</p>

</body>
</html>
