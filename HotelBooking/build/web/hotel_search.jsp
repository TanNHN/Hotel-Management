<%@include file="head.jsp"%>


<body>
    <script>
        $(function () {
            $("#checkOutDatePicker").datepicker({
                dateFormat: "dd-mm-yy",
                minDate: new Date(),
                defaultDate: new Date()

            });
            $("#checkInDatePicker").datepicker({
                dateFormat: "dd-mm-yy",
                minDate: new Date(),
                defaultDate: new Date()
            });

        });
    </script>
    <h1>Hotel</h1>
    <form action="MainController" >
        Hotel name: <input type="text" name="txtName" value="${param.txtName}"><br>
        Location: <input type="text" name="txtAddress" value="${param.txtAddress}"><br>
        Total room: <input type="number" name="txtTotalRoom" value="${param.txtTotalRoom}"><br>
        Check in:  <input readonly="true" type="text" value="${requestScope.CHECK_IN}" id="checkInDatePicker" name="txtCheckIn"><br>
        Check out: <input readonly="true" type="text" value="${requestScope.CHECK_OUT}" id="checkOutDatePicker" name="txtCheckOut"><br>
        Page size: <select name="slPageSize">
            <option ${param.slPageSize == 20 ? 'selected' : ''} value="20">20</option>
            <option ${param.slPageSize == 50 ? 'selected' : ''} value="50">50</option>
            <option ${param.slPageSize == 100 ? 'selected' : ''} value="100">100</option>
        </select><br>
        <input type="submit" name="action" value="Search Hotel">
    </form>
    Total result: ${requestScope.TOTAL}<br>

    <c:if test="${empty requestScope.HOTEL_LIST}" var="emptyHotelList">
        No hotel found
    </c:if>
    <c:if test="${!emptyHotelList}">
        <c:forEach var="hotel" items="${requestScope.HOTEL_LIST}">
            <div style="margin-top: 20px; margin-left: auto; width: 70%;
                 margin-right: auto; padding: 20px; border: solid">
                <h2><a style="text-decoration: none; color: black" href="MainController?action=GetRoom&hotelId=${hotel.id}&hotelName=${hotel.name}&txtCheckIn=${requestScope.CHECK_IN}&txtCheckOut=${requestScope.CHECK_OUT}">${hotel.name}</a></h2>
                Address: ${hotel.address}
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

            <a href="MainController?action=Search Hotel&txtName=${param.txtName}&txtAddress=${param.txtAddress}&page=${page}&slPageSize=${requestScope.PAGE_SIZE}">${page}</a>  
            <c:set value="${page+1}" var="page"/>
        </c:forEach>
    </c:if>

</body>
</html>
