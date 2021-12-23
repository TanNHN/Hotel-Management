<%@include file="head.jsp"%>

<body>
    <c:if test="${not empty requestScope.MESSAGE_LIST}">
        <c:forEach items="${requestScope.MESSAGE_LIST}" var="message">
            <p style="color: red">${message}</p>

        </c:forEach>
    </c:if>
    <c:if test="${not empty sessionScope.CART}" var="hasCart">
        <c:set var="total" value="0"/>

        <form action="MainController" method="post">
            <c:forEach items="${sessionScope.CART}" var="cartItem">
                <div style="margin-top: 20px; margin-left: auto; width: 70%;
                     margin-right: auto; padding: 20px; border: solid">
                    <h3>${cartItem.hotel.name}</h3>
                    Room type: ${cartItem.roomType.name}<br>
                    Available: ${cartItem.available}<br>
                    Price: ${cartItem.roomType.roomTypePrice.price}<br>
                    Room quantity: <input name="quantityHotelId${cartItem.hotel.id}RoomTypeId${cartItem.roomType.id}" type="number" value="${cartItem.quantity}" required="true"><br>
                    

                    Check in: <fmt:formatDate pattern = "dd-MM-yyyy" value = "${cartItem.checkIn}" /><br>
                    Check out: <fmt:formatDate pattern = "dd-MM-yyyy" value = "${cartItem.checkOut}" /><br>
                    Amount: ${cartItem.amount}
                    <form action="MainController" method="post">
                        <table border="1">
                            <thead>
                                <tr>
                                    <th>Room name</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${cartItem.roomType.roomList}" var="room">

                                    <tr>
                                        <td>${room.name}</td>

                                    </tr>

                                </c:forEach>

                                <c:set var="total" value="${total + cartItem.amount}" />

                            </tbody>
                        </table>
                        <a  onclick="if (confirm('Remove this room?'))
                                    commentDelete(1);
                                return false" href="MainController?action=Delete cart item&hotelId=${cartItem.hotel.id}&roomTypeId=${cartItem.roomType.id}">Delete</a>
                </div>
            </c:forEach>

            Total: ${total}<br>


            <input type="submit" name="action" value="Update cart"><br>
            <input type="submit" name="action" value="Check out">
        </form>


    </c:if>

    <c:if test="${!hasCart}">
        Your cart is empty
    </c:if>
</body>
</html>
