<%@include file="head.jsp"%>

<body>

    <script>
        $(function () {
            $("#createDate").datepicker({
                dateFormat: "dd-mm-yy"
            });
        });
    </script>
    <h1>Booking history</h1>
    <p style="color: red">${requestScope.BOOKING_SUCCESS_MESSAGE}<p>
    <p style="color: red">${requestScope.CONFIRM_SUCCESS_MESSAGE}<p>
        <c:if test="${not empty requestScope.INVALID_MESSAGES}">
            <c:forEach items="${requestScope.INVALID_MESSAGES}" var="message">
            <p style="color: red">${message} </p>
        </c:forEach>
    </c:if>
    <form action="MainController">
        Hotel name: <input type="text" name="txtHotelName" value="${param.txtHotelName}"><br>
        Booking date: <input readonly="true" type="text" id="createDate" name="txtCreateDate" 
                             value="${param.txtCreateDate}"><br>
        Page size: <select name="slPageSize">
            <option ${param.slPageSize == 20 ? 'selected' : ''} value="20">20</option>
            <option ${param.slPageSize == 50 ? 'selected' : ''} value="50">50</option>
            <option ${param.slPageSize == 100 ? 'selected' : ''} value="100">100</option>
        </select><br>
        <input type="submit" name="action" value="Search booking">
    </form>
    Total result: ${requestScope.TOTAL}<br>

    <c:if test="${empty requestScope.BOOKING_LIST}">
        No booking found
    </c:if>
    <c:if test="${not empty requestScope.INACTIVE_SUCCESS_MESSAGE}">
        ${INACTIVE_SUCCESS_MESSAGE}
    </c:if>
    <c:forEach items="${requestScope.BOOKING_LIST}" var="booking">
        <div style="margin-top: 20px; margin-left: auto;
             margin-right: auto; padding: 20px; border: solid">
            Booking id: ${booking.id}<br>
            Status: ${booking.bookingStatus.name}<br>
            Create date:<fmt:formatDate pattern = "dd-MM-yyyy HH:mm:ss" 
                            value = "${booking.createDate}" /> 
            <h1>${booking.hotel.name}</h1>
            <p style="margin-bottom: 20px">Address: <span style="font-weight: bold">${booking.hotel.address}</span></p>
            <table border="1" style="margin-bottom: 20px">
                <thead>
                    <tr>
                        <th>Room name</th>
                        <th>Room type</th>
                        <th>Price</th>
                        <th>Check in</th>
                        <th>Check out</th>
                        <th>Amount</th>
                    </tr>
                </thead>
                <c:forEach items="${booking.bookingDetails}" var="bookingDetails">
                    <tbody>
                        <tr>
                            <td>${bookingDetails.room.name}</td>
                            <td>${bookingDetails.room.roomType.name}</td>
                            <td>${bookingDetails.room.roomType.roomTypePrice.price}</td>
                            <td><fmt:formatDate pattern = "dd-MM-yyyy" 
                                            value = "${bookingDetails.checkIn}" /> </td>
                            <td><fmt:formatDate pattern = "dd-MM-yyyy" 
                                            value = "${bookingDetails.checkOut}" /> </td>
                            <td>${bookingDetails.amount}</td>
                        </tr>
                    </tbody>
                </c:forEach>
            </table>
            Total: ${booking.total}<br>
            <c:if test="${booking.bookingStatus.name == 'Active'}">
                <a href="MainController?action=Deactive booking&bookingId=${booking.id}">Deactive</a>

            </c:if>
        </div>

    </c:forEach>
    <c:set value="1" var="page"/>

    <c:if test="${param.slPageSize == null}" var="isStepNull">
        <c:set value="20" var="step"/>
    </c:if>
    <c:if test="${!isStepNull}">
        <c:set value="${param.slPageSize}" var="step"/>

    </c:if>
    <c:forEach begin="1" end="${requestScope.TOTAL}" step="${step}">

        <a href="MainController?action=Search booking&txtHotelName=${param.txtHotelName}&txtCreateDate=${param.txtCreateDate}&page=${page}&slPageSize=${requestScope.PAGE_SIZE}">${page}</a>  
        <c:set value="${page+1}" var="page"/>
    </c:forEach>
</body>
</html>
